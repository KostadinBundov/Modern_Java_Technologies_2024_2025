package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class NewsApiException extends Exception {
    public NewsApiException(String message) {
        super(message);
    }

    public NewsApiException(String message, Throwable cause) {
        super(message, cause);
    }
}