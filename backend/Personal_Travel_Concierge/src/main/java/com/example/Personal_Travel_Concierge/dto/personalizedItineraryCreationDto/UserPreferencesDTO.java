package com.example.Personal_Travel_Concierge.dto.personalizedItineraryCreationDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserPreferencesDTO {
    private String destination;
    private String interests;
    private LocalDate travelDate;
}
