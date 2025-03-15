package com.example.Personal_Travel_Concierge.dto.userDto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditPasswordDto {
    private String password;

    public EditPasswordDto(String password) {
        this.password = password;
    }
}
