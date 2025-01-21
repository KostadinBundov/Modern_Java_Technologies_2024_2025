package bg.sofia.uni.fmi.mjt.newsfeed.request;

import bg.sofia.uni.fmi.mjt.newsfeed.client.Category;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class RequestBuilder {
    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public static HttpRequest createRequest(String apiKey, List<String> keywords, Category category,
                                            String country, Integer page, Integer pageSize) {
        String queryParams = concatRequestArguments(keywords, category, country, page, pageSize);
        URI uri = URI.create(BASE_URL + "?" + queryParams);

        return HttpRequest.newBuilder()
            .uri(uri)
            .header(AUTHORIZATION_HEADER, apiKey)
            .build();
    }

    private static String concatRequestArguments(List<String> keywords, Category category, String country,
                                                 Integer page, Integer pageSize) {
        StringBuilder sb = new StringBuilder();

        sb.append("q=").append(String.join("+", keywords));

        if (category != null) {
            appendText(sb, category.getValue(), "category");
        }

        if (country != null) {
            appendText(sb, country, "country");
        }

        if (pageSize != null) {
            appendText(sb, pageSize.toString(), "pageSize");
        }

        if (page != null) {
            appendText(sb, page.toString(), "page");
        }

        return sb.toString();
    }

    private static void appendText(StringBuilder sb, String text, String nameProperty) {
        if (text != null && !text.isEmpty()) {
            if (!sb.isEmpty()) {
                sb.append("&");
            }
            sb.append(nameProperty).append("=").append(text);
        }
    }
}