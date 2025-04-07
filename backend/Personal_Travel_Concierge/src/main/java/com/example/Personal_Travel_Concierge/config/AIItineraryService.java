package com.example.Personal_Travel_Concierge.config;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIItineraryService {

    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    // Google Translate API Key

    // Constructor to inject the Cohere API key from properties
    public AIItineraryService(@Value("${cohere.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public String generateAIResponse(String prompt) {
        MediaType mediaType = MediaType.get("application/json");

        // Create the request body with the user's prompt
        String requestBody = String.format("""
            {
              "model": "xlarge",
              "prompt": "%s",
              "max_tokens": 500,
              "temperature": 0.7
            }
        """, prompt.replace("\"", "\\\"")); // Escape quotes in the prompt

        // Build the request to Cohere API
        Request request = new Request.Builder()
                .url("https://api.cohere.ai/v1/generate")  // Ensure you're using the correct API endpoint
                .post(RequestBody.create(requestBody, mediaType))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request and handle the response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();

                // Parse the response JSON to extract the generated text
                int start = json.indexOf("\"text\":\"") + 8;
                int end = json.indexOf("\"", start);

                // Return the generated text, replacing escaped newline characters with actual newlines
                return json.substring(start, end).replace("\\n", "\n");
            } else {
                // If the API call fails, return an error code
                return "AI API Error: " + response.code();
            }
        } catch (IOException e) {
            // Handle the exception and return a detailed error message
            return "AI API Exception: " + e.getMessage();
        }
    }
}
