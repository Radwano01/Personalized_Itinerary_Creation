package com.example.Personal_Travel_Concierge.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String tokenType = "Bearer ";
    private EssentialUserDto essentialUserDto;

    public AuthResponseDto(String accessToken,
                           EssentialUserDto essentialUserDto) {
        this.accessToken = accessToken;
        this.essentialUserDto = essentialUserDto;
    }
}
