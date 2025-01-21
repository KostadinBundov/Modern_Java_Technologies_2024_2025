package bg.sofia.uni.fmi.mjt.newsfeed.response;

import bg.sofia.uni.fmi.mjt.newsfeed.response.success.Article;

import java.util.List;

public record Page(int pageNumber, int articlesCount, List<Article> articles) { }