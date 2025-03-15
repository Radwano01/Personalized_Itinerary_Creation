package com.example.Personal_Travel_Concierge.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EditDetailsDto {
    private String fullName;
    private String country;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;

    public EditDetailsDto(String fullName,
                              String country,
                              String phoneNumber,
                              String address,
                              LocalDate dateOfBirth) {
        this.fullName = fullName;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
