package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsApiException;
import bg.sofia.uni.fmi.mjt.newsfeed.response.Page;

import java.util.List;

public interface NewsApi {
    Page loadPageNews() throws NewsApiException;

    List<Page> loadAllNews() throws NewsApiException;
}