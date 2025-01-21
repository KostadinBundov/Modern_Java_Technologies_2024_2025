package bg.sofia.uni.fmi.mjt.newsfeed.response;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ExceptionFactory;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsApiException;
import bg.sofia.uni.fmi.mjt.newsfeed.response.error.ErrorResponse;
import bg.sofia.uni.fmi.mjt.newsfeed.response.success.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.response.success.SuccessfulResponse;
import bg.sofia.uni.fmi.mjt.newsfeed.util.StatusCodes;
import com.google.gson.Gson;

import java.util.List;

public class ResponseHandler {
    public static List<Article> processResponse(int statusCode, String responseBody) throws NewsApiException {
        Gson gson = new Gson();

        if (statusCode != StatusCodes.STATUS_CODE_OK) {
            ErrorResponse errorResponse = gson.fromJson(responseBody, ErrorResponse.class);
            throw ExceptionFactory.createException(statusCode, errorResponse);
        }

        SuccessfulResponse response = gson.fromJson(responseBody, SuccessfulResponse.class);
        return response.articles();
    }
}