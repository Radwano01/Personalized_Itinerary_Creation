package com.example.Personal_Travel_Concierge.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class RegisterDto {
    private String email;
    private String username;
    private String password;
    private String fullName;
    private String country;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
}
