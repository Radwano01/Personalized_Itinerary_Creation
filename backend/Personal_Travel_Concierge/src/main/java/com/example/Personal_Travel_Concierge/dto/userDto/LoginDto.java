package com.example.Personal_Travel_Concierge.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
    private String username;
    private String password;

    public LoginDto(String username,
                    String password) {
        this.username = username;
        this.password = password;
    }
}
