package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapEntityException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapSizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapParserTest {
    @Test
    void testCreateMapThrowsExceptionForNullMap() {
        char[][] nullMap = null;

        assertThrows(IllegalArgumentException.class, () -> MapParser.createMap(nullMap),
            "Expected InvalidMapSizeException for null map");
    }

    @Test
    void testCreateMapThrowsExceptionForEmptyMap() {
        char[][] emptyMap = {};

        assertThrows(IllegalArgumentException.class, () -> MapParser.createMap(emptyMap),
            "Expected InvalidMapSizeException for empty map");
    }

    @Test
    void testCreateMapThrowsExceptionForEmptyRow() {
        char[][] mapWithEmptyRow = {
            {'#', '#', '#'},
            {},
            {'#', '#', '#'}
        };

        assertThrows(IllegalArgumentException.class, () -> MapParser.createMap(mapWithEmptyRow),
            "Expected InvalidMapSizeException for map with empty row");
    }

    @Test
    void testCreateMapThrowsExceptionForNullRow() {
        char[][] mapWithNullRow = {
            {'#', '#', '#'},
            null,
            {'#', '#', '#'}
        };

        assertThrows(IllegalArgumentException.class, () -> MapParser.createMap(mapWithNullRow),
            "Expected InvalidMapSizeException for map with null row");
    }

    @Test
    void testCreateMapThrowsExceptionForDifferentRowsSize() {
        char[][] mapWithNullRow = {
            {'#', '#', '#'},
            {'#', 'A'},
            {'#', '#', '#'}
        };

        assertThrows(InvalidMapSizeException.class, () -> MapParser.createMap(mapWithNullRow),
            "Expected InvalidMapSizeException for map with null row");
    }

    @Test
    void testCreateMapThrowsExceptionForInvalidMapEntity() {
        char[][] mapWithNullRow = {
            {'#', '#', '#'},
            {'#', '#', 'R'},
            {'#', '#', 'K'}
        };

        assertThrows(InvalidMapEntityException.class, () -> MapParser.createMap(mapWithNullRow),
            "Expected InvalidMapSizeException for map with null row");
    }
}