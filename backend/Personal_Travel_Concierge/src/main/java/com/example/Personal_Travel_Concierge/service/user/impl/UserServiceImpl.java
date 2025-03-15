package com.example.Personal_Travel_Concierge.service.user.impl;

import com.example.Personal_Travel_Concierge.dto.userDto.*;
import com.example.Personal_Travel_Concierge.entity.user.RoleEntity;
import com.example.Personal_Travel_Concierge.entity.user.UserEntity;
import com.example.Personal_Travel_Concierge.repository.user.RoleRepository;
import com.example.Personal_Travel_Concierge.repository.user.UserRepository;
import com.example.Personal_Travel_Concierge.security.JWTGenerator;
import com.example.Personal_Travel_Concierge.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

import static com.example.Personal_Travel_Concierge.Utilities.ErrorUtils.alreadyValidException;
import static com.example.Personal_Travel_Concierge.Utilities.ErrorUtils.badRequestException;
import static com.example.Personal_Travel_Concierge.lib.MyLib.checkIfSentEmptyData;
import static com.example.Personal_Travel_Concierge.lib.MyLib.isStrongPassword;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final JavaMailSender javaMailSender;

    @Value("${VERIFY_LINK_TO_USER}")
    private String verifyLink;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           JWTGenerator jwtGenerator,
                           JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.javaMailSender = javaMailSender;
    }


    @Async("userServiceTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> registerUser(@NonNull RegisterDto registerDto) {
        if(isStrongPassword(registerDto.getPassword())){
            return CompletableFuture.completedFuture(badRequestException("Password is weak"));
        }

        ResponseEntity<String> checkIfEmailANDUsernameExistResult =
                checkIfEmailANDUsernameAreAlreadyExist(registerDto.getEmail(), registerDto.getUsername());
        if(!checkIfEmailANDUsernameExistResult.getStatusCode().equals(HttpStatus.OK)){
            return CompletableFuture.completedFuture(checkIfEmailANDUsernameExistResult);
        }

        UserEntity userEntity = prepareUserEntity(registerDto);

        userRepository.save(userEntity);

        return CompletableFuture.completedFuture(ResponseEntity.ok(userEntity.toString()));
    }

    @Async("userServiceTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> loginUser(LoginDto loginUserDto) {
        Authentication authentication = authenticateUser(loginUserDto.getUsername(), loginUserDto.getPassword());

        String token = jwtGenerator.generateToken(authentication);

        UserEntity user = findUserByUsername(loginUserDto.getUsername());

        if(user != null){
            EssentialUserDto essentialUserDto = new EssentialUserDto(
                    user.getId(),
                    user.getUsername(),
                    user.isVerificationStatus(),
                    user.getRole().getRole()
            );

            return CompletableFuture.completedFuture
                    (ResponseEntity.ok(new AuthResponseDto(token,essentialUserDto)));
        }

        return null;
    }

    @Async("userServiceTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> deleteUser(long userId) {
        UserEntity user = getUserById(userId);

        userRepository.delete(user);

        return CompletableFuture.completedFuture(ResponseEntity.ok("Account deleted successfully"));
    }

    @Override
    public CompletableFuture<ResponseEntity<?>> getUserIdByEmail(String email) {
        return CompletableFuture.completedFuture(ResponseEntity.ok(userRepository.findUserIdByEmail(email)));
    }

    @Async("userServiceTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> editUserPassword(long userId, EditPasswordDto editUserDto) {
        if(!checkIfSentEmptyData(editUserDto)){
            return CompletableFuture.completedFuture(badRequestException("you sent an empty data to change"));
        }

        if(isStrongPassword(editUserDto.getPassword())){
            return CompletableFuture.completedFuture(badRequestException("password is weak"));
        }

        UserEntity user = getUserById(userId);

        updateToNewData(user, editUserDto);

        userRepository.save(user);

        return CompletableFuture.completedFuture(ResponseEntity.ok(user.toString()));
    }

    @Async("userServiceTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> verifyUser(String email) {
        UserEntity userEntity = findUserByEmail(email);

        userEntity.setVerificationStatus(true);

        userRepository.save(userEntity);

        return CompletableFuture.completedFuture(ResponseEntity.ok("User has been verified"));
    }

    @Async("userServiceTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> sendVerificationLink(long userId, String token) throws MessagingException {
        UserEntity user = getUserById(userId);

        ResponseEntity<String> checkResult = checkIfUserAlreadyVerified(user);
        if(!checkResult.getStatusCode().equals(HttpStatus.OK)){
            return CompletableFuture.completedFuture(checkResult);
        }

        sendVerificationMessage(user.getEmail(), token);

        return CompletableFuture.completedFuture(ResponseEntity.ok("Verification email sent"));
    }

    @Override
    public CompletableFuture<ResponseEntity<?>> getUserDetails(long userId) {
        return null;
    }

    @Override
    public CompletableFuture<ResponseEntity<?>> editUserDetails(long userId, EditDetailsDto editUserDetailsDto) {
        return null;
    }

    private Authentication authenticateUser(String username, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private ResponseEntity<String> checkIfUserAlreadyVerified(UserEntity user){
        if(user.isVerificationStatus()){
            return badRequestException("This account has verified already");
        }
        return ResponseEntity.ok("OK");
    }

    private void sendVerificationMessage(String email, String token) throws MessagingException {
        String verificationUrl = verifyLink + "/" + email + "/" + token;
        String subject = "Email Verification From Tourism Project";
        String message = "<html><body>"
                + "<h1>Email Verification</h1>"
                + "<p>Please click the link below to verify your account:</p>"
                + "<a href=\"" + verificationUrl + "\">Verify Email</a>"
                + "</body></html>";

        sendMessageToEmail(
                prepareTheMessageEmail(email, subject, message)
        );
    }

    private MimeMessage prepareTheMessageEmail(String email, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(message, true);
        return mimeMessage;
    }

    private void sendMessageToEmail(MimeMessage mimeMessage){
        javaMailSender.send(mimeMessage);
    }

    private ResponseEntity<String> checkIfEmailANDUsernameAreAlreadyExist(String email, String username){
        if (existsByEmail(email) || existsUsernameByUsername(username)) {
            return alreadyValidException("Email or Username is already valid");
        }
        return ResponseEntity.ok("OK");
    }

    private UserEntity getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User id not found"));
    }

    private UserEntity prepareUserEntity(RegisterDto registerDto) {
        return new UserEntity(
                registerDto.getUsername(),
                registerDto.getEmail(),
                passwordEncoder.encode(registerDto.getPassword()),
                registerDto.getFullName(),
                registerDto.getCountry(),
                (registerDto.getPhoneNumber().isEmpty() ? null : registerDto.getPhoneNumber()),
                registerDto.getAddress(),
                registerDto.getDateOfBirth(),
                findRoleByRole()
        );
    }

    private RoleEntity findRoleByRole(){
        return roleRepository.findByRole("USER")
                .orElseThrow(()-> new EntityNotFoundException("Role not found"));
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean existsUsernameByUsername(String username) {
        return userRepository.existsUsernameByUsername(username);
    }

    private UserEntity findUserByUsername(String usernameFromJWT) {
        return userRepository.findUserByUsername(usernameFromJWT)
                .orElseThrow(()-> new EntityNotFoundException("username not found"));
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("Email not found"));
    }

    private void updateToNewData(UserEntity user, EditPasswordDto editPasswordDto) {
        if (editPasswordDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(editPasswordDto.getPassword()));
        }
    }

    private void updateToNewData(UserEntity user, EditDetailsDto editUserDetailsDto){
        if (editUserDetailsDto.getFullName() != null) {
            user.setFullName(editUserDetailsDto.getFullName());
        }
        if (editUserDetailsDto.getCountry() != null) {
            user.setCountry(editUserDetailsDto.getCountry());
        }
        if (editUserDetailsDto.getPhoneNumber() != null) {
            user.setPhoneNumber(editUserDetailsDto.getPhoneNumber());
        }
        if (editUserDetailsDto.getAddress() != null) {
            user.setAddress(editUserDetailsDto.getAddress());
        }
        if (editUserDetailsDto.getDateOfBirth() != null) {
            user.setDateOfBirth(editUserDetailsDto.getDateOfBirth());
        }
    }
}
