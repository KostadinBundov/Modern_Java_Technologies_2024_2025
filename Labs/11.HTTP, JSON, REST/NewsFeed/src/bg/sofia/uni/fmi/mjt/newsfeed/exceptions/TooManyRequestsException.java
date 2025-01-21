package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class TooManyRequestsException extends NewsApiException {
    public TooManyRequestsException(String message) {
        super(message);
    }

    public TooManyRequestsException(String message, Throwable cause) {
        super(message, cause);
    }
}