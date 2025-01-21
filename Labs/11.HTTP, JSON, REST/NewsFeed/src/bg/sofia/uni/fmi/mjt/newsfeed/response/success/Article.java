package bg.sofia.uni.fmi.mjt.newsfeed.response.success;

import com.google.gson.annotations.SerializedName;

public record Article(
    Source source,
    String author,
    String title,
    String description,
    String url,
    @SerializedName("urlToImage") String image,
    @SerializedName("publishedAt") String publishedDate,
    String content) { }