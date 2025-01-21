package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

import bg.sofia.uni.fmi.mjt.newsfeed.response.error.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExceptionFactoryTest {
    @Mock
    private ErrorResponse errorResponseMock;

    @Test
    public void testCreateExceptionForCode400ReturnBadRequestException() {
        Exception exception = ExceptionFactory.createException(400, errorResponseMock);

        assertInstanceOf(BadRequestException.class, exception,
            "Expected BadRequestException for status code 400.");
    }

    @Test
    public void testCreateExceptionForCode401ReturnUnauthorizedException() {
        Exception exception = ExceptionFactory.createException(401, errorResponseMock);

        assertInstanceOf(UnauthorizedException.class, exception,
            "Expected UnauthorizedException for status code 401.");
    }

    @Test
    public void testCreateExceptionForCode429ReturnTooManyRequestsException() {
        Exception exception = ExceptionFactory.createException(429, errorResponseMock);

        assertInstanceOf(TooManyRequestsException.class, exception,
            "Expected TooManyRequestsException for status code 429.");
    }

    @Test
    public void testCreateExceptionForCode500ReturnServerErrorException() {
        Exception exception = ExceptionFactory.createException(500, errorResponseMock);

        assertInstanceOf(ServerErrorException.class, exception,
            "Expected ServerErrorException for status code 500.");
    }

    @Test
    public void testCreateExceptionForUnknownCodeReturnNewsApiException() {
        Exception exception = ExceptionFactory.createException(999, errorResponseMock);

        assertInstanceOf(NewsApiException.class, exception,
            "Expected NewsApiException for unknown status code.");
    }

    @Test
    public void testCreateExceptionReturnExceptionWithValidMessage() {
        when(errorResponseMock.code()).thenReturn("Demo code");
        when(errorResponseMock.message()).thenReturn("Demo message");

        Exception exception = ExceptionFactory.createException(400, errorResponseMock);

        assertEquals("Error Code: [Demo code]" + System.lineSeparator() + "Message: Demo message", exception.getMessage(),
            "Expected correct error message.");
    }
}