package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class UnauthorizedException extends NewsApiException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}