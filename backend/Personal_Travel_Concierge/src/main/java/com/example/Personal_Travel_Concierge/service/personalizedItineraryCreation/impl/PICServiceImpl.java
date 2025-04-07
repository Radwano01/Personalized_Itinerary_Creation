package com.example.Personal_Travel_Concierge.service.personalizedItineraryCreation.impl;

import com.example.Personal_Travel_Concierge.config.AIItineraryService;
import com.example.Personal_Travel_Concierge.dto.personalizedItineraryCreationDto.UserPreferencesDTO;
import com.example.Personal_Travel_Concierge.service.personalizedItineraryCreation.PICService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PICServiceImpl implements PICService {

    private final AIItineraryService aiItineraryService;

    @Autowired
    public PICServiceImpl(AIItineraryService aiItineraryService) {
        this.aiItineraryService = aiItineraryService;
    }


    @Override
    public CompletableFuture<String> enterUserData(UserPreferencesDTO userPreferencesDTO) {
        String prompt = String.format("""
            I'm planning a trip to %s on %s. I'm interested in %s.
            Can you generate a personalized travel itinerary with places to visit, fun activities, and meal suggestions?
            Please format it in a structured way.
        """, userPreferencesDTO.getDestination(), userPreferencesDTO.getTravelDate(), userPreferencesDTO.getInterests());

        return CompletableFuture.completedFuture(aiItineraryService.generateAIResponse(prompt));
    }


    @Override
    public CompletableFuture<String> translationText(String text, String targetLanguage) {
        String prompt = String.format("""
        Translate the following text into %s:
        "%s"
    """, targetLanguage, text);

        return CompletableFuture.completedFuture(aiItineraryService.generateAIResponse(prompt));
    }


    @Override
    public CompletableFuture<String> dynamicSuggestions(String location) {
        String prompt = String.format("""
        Based on the location in %s, can you suggest fun activities or places to visit?
        Please also consider meal suggestions suitable for this location.
    """, location);

        String suggestions = aiItineraryService.generateAIResponse(prompt);
        return CompletableFuture.completedFuture(suggestions);
    }

}
