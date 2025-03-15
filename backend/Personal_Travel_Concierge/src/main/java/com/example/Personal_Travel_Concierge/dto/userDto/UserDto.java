package com.example.Personal_Travel_Concierge.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private long id;
    private String username;
    private String email;
    private String image;
    private boolean verificationStatus;
    private String fullName;
    private String country;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;

    public UserDto(long id, String username, String email,
                   String image, boolean verificationStatus,
                   String fullName, String country,
                   String phoneNumber, String address,
                   LocalDate dateOfBirth) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.image = image;
        this.verificationStatus = verificationStatus;
        this.fullName = fullName;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
