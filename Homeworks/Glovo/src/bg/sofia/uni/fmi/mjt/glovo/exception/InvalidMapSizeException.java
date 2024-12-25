package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidMapSizeException extends RuntimeException {
    public InvalidMapSizeException(String message) {
        super(message);
    }

    public InvalidMapSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
