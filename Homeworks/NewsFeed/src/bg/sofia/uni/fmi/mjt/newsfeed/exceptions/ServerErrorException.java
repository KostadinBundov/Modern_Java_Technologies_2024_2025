package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class ServerErrorException extends NewsApiException {
    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}