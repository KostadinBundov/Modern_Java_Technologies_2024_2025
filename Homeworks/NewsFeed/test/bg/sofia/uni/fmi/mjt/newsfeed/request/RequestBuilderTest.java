package bg.sofia.uni.fmi.mjt.newsfeed.request;

import bg.sofia.uni.fmi.mjt.newsfeed.client.Category;
import org.junit.jupiter.api.Test;

import java.net.http.HttpRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestBuilderTest {
    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines";
    private static final String API_KEY = "testApiKey";

    @Test
    public void testKeywordsAreIncludedInQuery() {
        List<String> keywords = List.of("technology", "science");
        HttpRequest request = RequestBuilder.createRequest(API_KEY, keywords, null, null, null, null);

        String query = request.uri().getQuery();
        String joinedKeywords = String.join("+", keywords);
        String expectedQueryPart = "q=" + joinedKeywords;

        assertTrue(query.contains(expectedQueryPart), "Query does not contain the expected keywords part.");
    }

    @Test
    public void testCategoryIsIncludedInQuery() {
        Category category = Category.TECHNOLOGY;
        HttpRequest request = RequestBuilder.createRequest(API_KEY, List.of("news"), category, null, null, null);

        String query = request.uri().getQuery();
        String expectedQueryPart = "category=tech";

        assertTrue(query.contains(expectedQueryPart), "Query does not contain the expected category part.");
    }

    @Test
    public void testCountryIsIncludedInQuery() {
        String country = "us";
        HttpRequest request = RequestBuilder.createRequest(API_KEY, List.of("news"), null, country, null, null);

        String query = request.uri().getQuery();
        String expectedQueryPart = "country=us";

        assertTrue(query.contains(expectedQueryPart), "Query does not contain the expected country part.");
    }

    @Test
    public void testPageSizeIsIncludedInQuery() {
        int pageSize = 20;
        HttpRequest request = RequestBuilder.createRequest(API_KEY, List.of("news"), null, null, null, pageSize);

        String query = request.uri().getQuery();
        String expectedQueryPart = "pageSize=20";

        assertTrue(query.contains(expectedQueryPart), "Query does not contain the expected page size part.");
    }

    @Test
    public void testPageIsIncludedInQuery() {
        int page = 2;
        HttpRequest request = RequestBuilder.createRequest(API_KEY, List.of("news"), null, null, page, null);

        String query = request.uri().getQuery();
        String expectedQueryPart = "page=2";

        assertTrue(query.contains(expectedQueryPart), "Query does not contain the expected page part.");
    }

    @Test
    public void testFullQuery() {
        List<String> keywords = List.of("technology", "science");
        Category category = Category.TECHNOLOGY;
        String country = "us";
        int page = 2;
        int pageSize = 20;

        HttpRequest request = RequestBuilder.createRequest(API_KEY, keywords, category, country, page, pageSize);

        String fullUri = request.uri().toString();
        String expectedUri = buildExpectedUri(keywords, category.getValue(), country, page, pageSize);

        assertEquals(expectedUri, fullUri, "Full URI does not match the expected URI.");
    }

    @Test
    public void testAuthorizationHeaderValueIsAdded() {
        List<String> keywords = List.of("news");
        HttpRequest request = RequestBuilder.createRequest(API_KEY, keywords, null, null, null, null);

        String actualAuthorizationHeader = request.headers().firstValue("Authorization").orElse(null);

        assertEquals(API_KEY, actualAuthorizationHeader,
            "Authorization header value is incorrect or missing.");
    }

    private String buildExpectedUri(List<String> keywords, String category, String country, Integer page, Integer pageSize) {
        StringBuilder query = new StringBuilder();

        if (keywords != null && !keywords.isEmpty()) {
            String joinedKeywords = String.join("+", keywords);
            query.append("q=").append(joinedKeywords).append("&");
        }
        if (category != null && !category.isEmpty()) {
            query.append("category=").append(category).append("&");
        }
        if (country != null && !country.isEmpty()) {
            query.append("country=").append(country).append("&");
        }
        if (pageSize != null) {
            query.append("pageSize=").append(pageSize).append("&");
        }
        if (page != null) {
            query.append("page=").append(page).append("&");
        }

        String finalQuery = query.toString();
        if (finalQuery.endsWith("&")) {
            finalQuery = finalQuery.substring(0, finalQuery.length() - 1);
        }

        return BASE_URL + "?" + finalQuery;
    }
}