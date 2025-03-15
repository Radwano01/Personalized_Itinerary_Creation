package com.example.Personal_Travel_Concierge.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EssentialUserDto {

    private long id;
    private String username;
    private boolean verificationStatus;
    private String role;

    public EssentialUserDto(long id,
                            String username,
                            boolean verificationStatus,
                            String role) {
        this.id = id;
        this.username = username;
        this.verificationStatus = verificationStatus;
        this.role = role;
    }
}