package bg.sofia.uni.fmi.mjt.newsfeed.client;

import java.util.List;

public class Validator {

    public static void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key cannot be null or empty!");
        }
    }

    public static void validateKeywords(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Key words cannot be null or empty!");
        }
    }

    public static void validatePage(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be greater than 0!");
        }
    }

    public static void validatePageSize(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("Page size must be greater than 0!");
        }
    }
}