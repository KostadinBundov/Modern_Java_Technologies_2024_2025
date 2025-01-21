import bg.sofia.uni.fmi.mjt.newsfeed.client.NewsApiClient;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsApiException;
import bg.sofia.uni.fmi.mjt.newsfeed.response.Page;
import bg.sofia.uni.fmi.mjt.newsfeed.response.success.Article;

import java.net.http.HttpClient;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();

        String apiKey = "85aed6a7d88f482584b9294a26c3b8bb";

        List<String> keywords = List.of("trump");

        NewsApiClient client = NewsApiClient.newBuilder(httpClient, apiKey, keywords)
            .setPageSize(20)
            .build();

        try {
            System.out.println("=== Testing loadNews() ===");
            Page page = client.loadPageNews();
            System.out.println("Page Number: " + page.pageNumber());
            System.out.println("Articles Count: " + page.articlesCount());
            for (Article article : page.articles()) {
                System.out.println("- " + article.title());
            }

            System.out.println("\n=== Testing loadAllNews() ===");
            List<Page> allPages = client.loadAllNews();
            for (Page p : allPages) {
                System.out.println("Page Number: " + p.pageNumber());
                System.out.println("Articles on this page: " + p.articlesCount());
                for (Article article : p.articles()) {
                    System.out.println("  - " + article.title());
                }
            }
        } catch (NewsApiException e) {
            System.err.println("Error while fetching news: " + e.getMessage());
        }
    }
}