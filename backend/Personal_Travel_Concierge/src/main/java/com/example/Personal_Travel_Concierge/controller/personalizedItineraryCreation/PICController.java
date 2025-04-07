package com.example.Personal_Travel_Concierge.controller.personalizedItineraryCreation;

import com.example.Personal_Travel_Concierge.dto.personalizedItineraryCreationDto.UserPreferencesDTO;
import com.example.Personal_Travel_Concierge.service.personalizedItineraryCreation.impl.PICServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PICController {

    private final PICServiceImpl picServiceImpl;

    @Autowired
    public PICController(PICServiceImpl picServiceImpl) {
        this.picServiceImpl = picServiceImpl;
    }

    @PostMapping(path = "/generateItinerary")
    public CompletableFuture<ResponseEntity<?>> userItinerary(@RequestBody UserPreferencesDTO userPreferencesDTO){
        CompletableFuture<String> itinerary = picServiceImpl.enterUserData(userPreferencesDTO);

        CompletableFuture<String> suggestions = picServiceImpl.dynamicSuggestions(userPreferencesDTO.getDestination());

        return itinerary.thenCombine(suggestions, (itineraryResult, suggestionsResult)->{
            String finalItinerary = itineraryResult + "\n\nSuggestions:\n" + suggestionsResult;
            return ResponseEntity.ok(finalItinerary);
        });
    }

    @PostMapping(path = "/translate")
    public CompletableFuture<ResponseEntity<?>> TranslateText(@RequestBody Map<String, String> body){
        CompletableFuture<String> translatedText = picServiceImpl.translationText(body.get("text"), body.get("targetLanguage"));

        return translatedText.thenApply(ResponseEntity::ok);
    }
}
