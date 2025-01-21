package bg.sofia.uni.fmi.mjt.newsfeed.response.success;

import java.util.List;

public record SuccessfulResponse(String status, int totalResults, List<Article> articles) { }