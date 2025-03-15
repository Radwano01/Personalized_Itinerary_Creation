package com.example.Personal_Travel_Concierge.Utilities;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorUtils {

    public static ResponseEntity<String> serverErrorException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    public static ResponseEntity<String> serverErrorException(String message){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    public static ResponseEntity<String> notFoundException(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    public static ResponseEntity<String> notFoundException(String message){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    public static ResponseEntity<String> alreadyValidException(String message){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    public static ResponseEntity<String> badRequestException(String message){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    public static ResponseEntity<IllegalAccessException> badRequestException(IllegalAccessException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }
}