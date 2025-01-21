package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class BadRequestException extends NewsApiException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}