package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.request.RequestBuilder;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsApiException;
import bg.sofia.uni.fmi.mjt.newsfeed.response.Page;
import bg.sofia.uni.fmi.mjt.newsfeed.response.ResponseHandler;
import bg.sofia.uni.fmi.mjt.newsfeed.response.success.Article;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class NewsApiClient implements NewsApi {
    private static final int MAX_ARTICLES_COUNT = 100;

    private final String apiKey;
    private final List<String> keyWords;
    private final HttpClient httpClient;

    private final Category category;
    private final String country;
    private final Integer page;
    private final Integer pageSize;

    private NewsApiClient(ClientBuilder builder) {
        this.apiKey = builder.apiKey;
        this.keyWords = builder.keyWords;
        this.httpClient = builder.httpClient;
        this.category = builder.category;
        this.country = builder.country;
        this.page = builder.page;
        this.pageSize = builder.pageSize;
    }

    public static ClientBuilder newBuilder(HttpClient httpClient, String apiKey, List<String> keyWords) {
        return new ClientBuilder(httpClient, apiKey, keyWords);
    }

    @Override
    public Page loadPageNews() throws NewsApiException {
        return loadPage(page, pageSize);
    }

    @Override
    public List<Page> loadAllNews() throws NewsApiException {
        try {
            Page largePage = loadPage(ClientBuilder.DEFAULT_PAGE, MAX_ARTICLES_COUNT);

            if (largePage == null || largePage.articles().isEmpty()) {
                return List.of();
            }

            List<Article> allArticles = largePage.articles();
            return splitArticlesIntoPages(allArticles, pageSize);
        } catch (Exception e) {
            throw new NewsApiException("Error while loading all news");
        }
    }

    private Page loadPage(int page, int pageSize) throws NewsApiException {
        try {
            HttpRequest request = RequestBuilder.createRequest(apiKey, keyWords, category, country, page, pageSize);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<Article> articles = ResponseHandler.processResponse(response.statusCode(), response.body());

            if (articles == null || articles.isEmpty()) {
                return null;
            }

            return new Page(page, articles.size(), articles);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while loading page " + page);
        }
    }

    private List<Page> splitArticlesIntoPages(List<Article> allArticles, int pageSize) {
        int totalArticles = allArticles.size();
        int fullPagesCount = totalArticles / pageSize;
        int lastPageSize = totalArticles % pageSize;

        List<Page> pages = new ArrayList<>();

        for (int pageIndex = 0; pageIndex < fullPagesCount; pageIndex++) {
            int start = pageIndex * pageSize;
            int end = start + pageSize;

            List<Article> pageArticles = allArticles.subList(start, end);
            pages.add(new Page(pageIndex + 1, pageArticles.size(), pageArticles));
        }

        if (lastPageSize > 0) {
            int start = fullPagesCount * pageSize;
            List<Article> lastPageArticles = allArticles.subList(start, totalArticles);
            pages.add(new Page(fullPagesCount + 1, lastPageArticles.size(), lastPageArticles));
        }

        return pages;
    }

    public static class ClientBuilder {
        private static final int DEFAULT_PAGE = 1;
        private static final int DEFAULT_PAGE_SIZE = 20;
        private static final Category DEFAULT_CATEGORY = null;
        private static final String DEFAULT_COUNTRY = null;

        private final HttpClient httpClient;
        private final String apiKey;
        private final List<String> keyWords;

        private Category category = DEFAULT_CATEGORY;
        private String country = DEFAULT_COUNTRY;
        private Integer page = DEFAULT_PAGE;
        private Integer pageSize = DEFAULT_PAGE_SIZE;

        private ClientBuilder(HttpClient httpClient, String apiKey, List<String> keyWords) {
            Validator.validateApiKey(apiKey);
            Validator.validateKeywords(keyWords);

            this.httpClient = httpClient;
            this.apiKey = apiKey;
            this.keyWords = List.copyOf(keyWords);
        }

        public ClientBuilder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public ClientBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public ClientBuilder setPage(int page) {
            Validator.validatePage(page);
            this.page = page;
            return this;
        }

        public ClientBuilder setPageSize(int pageSize) {
            Validator.validatePageSize(pageSize);
            this.pageSize = pageSize;
            return this;
        }

        public NewsApiClient build() {
            return new NewsApiClient(this);
        }
    }
}