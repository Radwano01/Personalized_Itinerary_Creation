package com.example.Personal_Travel_Concierge.service.user;

import com.example.Personal_Travel_Concierge.dto.userDto.EditDetailsDto;
import com.example.Personal_Travel_Concierge.dto.userDto.EditPasswordDto;
import com.example.Personal_Travel_Concierge.dto.userDto.LoginDto;
import com.example.Personal_Travel_Concierge.dto.userDto.RegisterDto;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<ResponseEntity<?>> registerUser(@NonNull RegisterDto registerUserDto);

    CompletableFuture<ResponseEntity<?>> loginUser(LoginDto loginUserDto);

    CompletableFuture<ResponseEntity<?>> deleteUser(long userId);

    CompletableFuture<ResponseEntity<?>> getUserIdByEmail(String email);

    CompletableFuture<ResponseEntity<?>> editUserPassword(long userId, EditPasswordDto editUserDto);

    CompletableFuture<ResponseEntity<?>> verifyUser(String email);

    CompletableFuture<ResponseEntity<?>> sendVerificationLink(long userId, String token) throws MessagingException, MessagingException;

    CompletableFuture<ResponseEntity<?>> getUserDetails(long userId);

    CompletableFuture<ResponseEntity<?>> editUserDetails(long userId, EditDetailsDto editUserDetailsDto);

}
