package com.example.Personal_Travel_Concierge.controller.user;

import com.example.Personal_Travel_Concierge.dto.userDto.EditDetailsDto;
import com.example.Personal_Travel_Concierge.dto.userDto.EditPasswordDto;
import com.example.Personal_Travel_Concierge.dto.userDto.LoginDto;
import com.example.Personal_Travel_Concierge.dto.userDto.RegisterDto;
import com.example.Personal_Travel_Concierge.service.user.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.example.Personal_Travel_Concierge.Utilities.ErrorUtils.notFoundException;
import static com.example.Personal_Travel_Concierge.Utilities.ErrorUtils.serverErrorException;


@RestController
@RequestMapping(path = "${BASE_API}")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServicesImpl) {
        this.userServiceImpl = userServicesImpl;
    }

    @PostMapping(path="${USER_REGISTER_PATH}")
    public CompletableFuture<ResponseEntity<?>> registerUserDetails(@RequestBody RegisterDto registerUserDto){
        try{
            return userServiceImpl.registerUser(registerUserDto);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @PostMapping(path = "${USER_VERIFICATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> verifyUserDetails(@PathVariable("email") String email,
                                                                  @PathVariable("token") String token){
        try {
            return userServiceImpl.verifyUser(email);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture((notFoundException(e)));
        }catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @PostMapping(path = "${USER_SEND_VERIFICATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> sendVerificationLink(@PathVariable("userId") long userId,
                                                                     @PathVariable("token") String token){
        try {
            return userServiceImpl.sendVerificationLink(userId, token);
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @PostMapping(path="${USER_LOGIN_PATH}")
    public CompletableFuture<ResponseEntity<?>> loginUser(@RequestBody LoginDto loginDto) {
        try{
            return userServiceImpl.loginUser(loginDto);
        }catch(AuthenticationException e){
            return CompletableFuture.completedFuture(
                    (new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED)));
        }
    }

    @DeleteMapping(path="${USER_DELETE_PATH}")
    public CompletableFuture<ResponseEntity<?>> deleteUserDetails(@PathVariable("userId") long userId){
        try{
            return userServiceImpl.deleteUser(userId);
        }catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @GetMapping(path = "${GET_USERID_BY_EMAIL_PATH}")
    public CompletableFuture<ResponseEntity<?>> getUserIdByEmail(@RequestParam("email") String email){
        try{
            return userServiceImpl.getUserIdByEmail(email);
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture(notFoundException(e));
        }catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @PutMapping(path="${USER_EDIT_PATH}")
    public CompletableFuture<ResponseEntity<?>> editUserPassword(@PathVariable("userId") long userId,
                                                                 @RequestBody EditPasswordDto editPasswordDto){
        try {
            return userServiceImpl.editUserPassword(userId, editPasswordDto);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }

    }

    @GetMapping(path = "${GET_USER_DETAILS_PATH}")
    public CompletableFuture<ResponseEntity<?>> getUserDetails(@PathVariable("userId") long userId){
        try {
            return userServiceImpl.getUserDetails(userId);
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture(notFoundException(e));
        }catch (Exception e){
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @PutMapping(path = "${EDIT_USER_DETAILS_PATH}")
    public CompletableFuture<ResponseEntity<?>> editUserDetails(@PathVariable("userId") long userId,
                                                                @ModelAttribute EditDetailsDto editDetailsDto){
        try {
            return userServiceImpl.editUserDetails(userId, editDetailsDto);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }
}