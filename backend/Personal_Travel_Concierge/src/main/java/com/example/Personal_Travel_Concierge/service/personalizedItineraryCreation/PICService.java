package com.example.Personal_Travel_Concierge.service.personalizedItineraryCreation;

import com.example.Personal_Travel_Concierge.dto.personalizedItineraryCreationDto.UserPreferencesDTO;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface PICService {

    CompletableFuture<String> enterUserData(UserPreferencesDTO userPreferencesDTO);

    CompletableFuture<String> translationText(String text, String targetLanguage);

    CompletableFuture<String> dynamicSuggestions(String location);
}
