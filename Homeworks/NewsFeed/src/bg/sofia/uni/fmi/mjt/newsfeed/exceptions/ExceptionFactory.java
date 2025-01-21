package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

import bg.sofia.uni.fmi.mjt.newsfeed.response.error.ErrorResponse;
import bg.sofia.uni.fmi.mjt.newsfeed.util.StatusCodes;

public class ExceptionFactory {
    public static NewsApiException createException(int statusCode, ErrorResponse errorResponse) {
        String errorMessage = createExceptionMessage(errorResponse.message(), errorResponse.code());

        return switch (statusCode) {
            case StatusCodes.STATUS_CODE_BAD_REQUEST -> new BadRequestException(errorMessage);
            case StatusCodes.STATUS_CODE_UNAUTHORIZED -> new UnauthorizedException(errorMessage);
            case StatusCodes.STATUS_CODE_TOO_MANY_REQUESTS -> new TooManyRequestsException(errorMessage);
            case StatusCodes.STATUS_CODE_SERVER_ERROR -> new ServerErrorException(errorMessage);
            default -> new NewsApiException(errorMessage);
        };
    }

    private static String createExceptionMessage(String message, String code) {
        return String.format("Error Code: [%s]" + System.lineSeparator() + "Message: %s", code, message);
    }
}