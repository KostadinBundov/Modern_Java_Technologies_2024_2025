package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsApiException;
import bg.sofia.uni.fmi.mjt.newsfeed.response.Page;
import bg.sofia.uni.fmi.mjt.newsfeed.response.error.ErrorResponse;
import bg.sofia.uni.fmi.mjt.newsfeed.response.success.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.response.success.SuccessfulResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsApiClientTest {
    private static List<Article> expectedArticles;
    private static String jsonResponse;
    private static String errorResponseJson;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    private NewsApiClient newsApiClient;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        newsApiClient = NewsApiClient.newBuilder(mockHttpClient, "testApiKey", List.of("technology"))
            .setCategory(Category.GENERAL)
            .setPageSize(10)
            .build();

        when(mockHttpClient.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(mockHttpResponse);
    }

    @Test
    public void testLoadNewsSuccessfulResponse() throws NewsApiException {
        setUpSuccessfulResponse();

        when(mockHttpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockHttpResponse.body()).thenReturn(jsonResponse);

        Page page = newsApiClient.loadPageNews();

        Assertions.assertNotNull(page, "Page should not be null.");
        Assertions.assertIterableEquals(expectedArticles, page.articles(), "The articles list is not as expected.");
    }

    @Test
    public void testLoadNewsHandlesError() {
        setUpErrorResponse();

        when(mockHttpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
        when(mockHttpResponse.body()).thenReturn(errorResponseJson);

        assertThrows(NewsApiException.class, () -> newsApiClient.loadPageNews(),
            "Expected NewsApiException to be thrown when status code is not OK.");
    }

    @Test
    public void testLoadPageNewsHandlesEmptyResponse() throws NewsApiException, IOException, InterruptedException {
        when(mockHttpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockHttpResponse.body()).thenReturn("{\"status\":\"ok\",\"totalResults\":0,\"articles\":[]}");

        Page page = newsApiClient.loadPageNews();

        Assertions.assertNull(page, "The page should be null when the response contains no articles.");
    }

    @Test
    public void testLoadAllNewsReturnsMultiplePages() throws NewsApiException, IOException, InterruptedException {
        setUpMultiplePagesResponse();

        when(mockHttpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockHttpResponse.body()).thenReturn(jsonResponse);

        newsApiClient = NewsApiClient.newBuilder(mockHttpClient, "testApiKey", List.of("technology"))
            .setPageSize(2)
            .build();

        List<Page> pages = newsApiClient.loadAllNews();

        Assertions.assertEquals(2, pages.size(), "The number of pages is incorrect.");
    }


    @Test
    public void testLoadAllNewsHandlesEmptyResponse() throws NewsApiException, IOException, InterruptedException {
        when(mockHttpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockHttpResponse.body()).thenReturn("{\"status\":\"ok\",\"totalResults\":0,\"articles\":[]}");

        newsApiClient = NewsApiClient.newBuilder(mockHttpClient, "testApiKey", List.of("technology"))
            .setPageSize(10)
            .build();

        List<Page> pages = newsApiClient.loadAllNews();

        Assertions.assertTrue(pages.isEmpty(), "The returned pages list should be empty.");
    }

    private void setUpSuccessfulResponse() {
        expectedArticles = List.of(
            new Article(
                null,
                "Author",
                "Title",
                "Description",
                "url",
                "imageUrl",
                "date",
                "Content"
            )
        );

        SuccessfulResponse successfulResponse = new SuccessfulResponse(
            "ok",
            1,
            expectedArticles
        );

        jsonResponse = new Gson().toJson(successfulResponse);
    }

    private void setUpMultiplePagesResponse() {
        expectedArticles = List.of(
            new Article(
                null,
                "Author1",
                "Title1",
                "Description1",
                "url1",
                "imageUrl1",
                "date1",
                "Content1"
            ),
            new Article(
                null,
                "Author2",
                "Title2",
                "Description2",
                "url2",
                "imageUrl2",
                "date2",
                "Content2"
            ),
            new Article(
                null,
                "Author3",
                "Title3",
                "Description3",
                "url3",
                "imageUrl3",
                "date3",
                "Content3"
            )
        );

        SuccessfulResponse successfulResponse = new SuccessfulResponse(
            "ok",
            2,
            expectedArticles
        );

        jsonResponse = new Gson().toJson(successfulResponse);
    }

    private void setUpErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse(
            "error",
            "notFound",
            "Requested resource could not be found."
        );
        errorResponseJson = new Gson().toJson(errorResponse);
    }
}