package bg.sofia.uni.fmi.mjt.newsfeed.util;

import bg.sofia.uni.fmi.mjt.newsfeed.client.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    public void testValidateApiKeyWithNotNullOrEmptyDoesNotThrow() {
        assertDoesNotThrow(() -> Validator.validateApiKey("valid key"),
            "Valid API key should not throw an exception.");
    }

    @Test
    public void testValidateApiKeyWithNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validateApiKey(null),
            "Null API key should throw IllegalArgumentException.");
    }

    @Test
    public void testValidateApiKeyWithEmptyKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validateApiKey(""),
            "Empty API key should throw IllegalArgumentException.");
    }

    @Test
    public void testValidateKeyWordsWithNotNullOrEmptySetOfKeysDoesNotThrowException() {
        assertDoesNotThrow(() -> Validator.validateKeywords(List.of("first", "second")),
            "Valid keywords should not throw an exception.");
    }

    @Test
    public void testValidateKeyWordsWithNullSetOfKeyWordsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validateKeywords(null),
            "Null keywords set should throw IllegalArgumentException.");
    }

    @Test
    public void testValidateKeyWordsWithEmptySetOfKeyWordsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validateKeywords(List.of()),
            "Empty keywords set should throw IllegalArgumentException.");
    }

    @Test
    public void testValidatePageWithValidValueDoesNotThrow() {
        assertDoesNotThrow(() -> Validator.validatePage(1),
            "Valid page value should not throw an exception.");
    }

    @Test
    public void testValidatePageWithZeroValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePage(0),
            "Page value of 0 should throw IllegalArgumentException.");
    }

    @Test
    public void testValidatePageWithNegativeValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePage(-1),
            "Negative page value should throw IllegalArgumentException.");
    }

    @Test
    public void testValidatePageSizeWithValidValueDoesNotThrow() {
        assertDoesNotThrow(() -> Validator.validatePageSize(1),
            "Valid page size should not throw an exception.");
    }

    @Test
    public void testValidatePageSizeWithZeroValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePageSize(0),
            "Page size value of 0 should throw IllegalArgumentException.");
    }

    @Test
    public void testValidatePageSizeWithNegativeValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePageSize(-1),
            "Negative page size value should throw IllegalArgumentException.");
    }
}