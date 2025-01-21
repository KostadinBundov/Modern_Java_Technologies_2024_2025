package bg.sofia.uni.fmi.mjt.newsfeed.response;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsApiException;
import bg.sofia.uni.fmi.mjt.newsfeed.response.error.ErrorResponse;
import bg.sofia.uni.fmi.mjt.newsfeed.response.success.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.response.success.Source;
import bg.sofia.uni.fmi.mjt.newsfeed.response.success.SuccessfulResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ResponseHandlerTest {
    private final Gson gson = new Gson();

    @Test
    public void testProcessResponseSuccessful() throws NewsApiException {
        SuccessfulResponse successfulResponse = new SuccessfulResponse(
            "ok",
            1,
            List.of(new Article(
                new Source("source-id", "Source Name"),
                "Author",
                "Title",
                "Description",
                "Url",
                "UrlToImage",
                "Date",
                "Content"
            ))
        );

        String jsonResponse = gson.toJson(successfulResponse);

        List<Article> articles = ResponseHandler.processResponse(200, jsonResponse);
        Assertions.assertIterableEquals(successfulResponse.articles(), articles);
    }

    @Test
    public void testProcessResponseError() {
        ErrorResponse errorResponse = new ErrorResponse(
            "error",
            "apiKeyInvalid",
            "Your API key is invalid or missing."
        );

        String errorResponseJson = gson.toJson(errorResponse);

        assertThrows(NewsApiException.class, () -> ResponseHandler.processResponse(401, errorResponseJson),
            "Error should be thrown when status code is not OK.");
    }
}