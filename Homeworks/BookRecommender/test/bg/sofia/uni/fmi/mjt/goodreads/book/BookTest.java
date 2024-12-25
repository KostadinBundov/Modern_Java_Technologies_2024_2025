package bg.sofia.uni.fmi.mjt.goodreads.book;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    void testParsingBookWithInvalidArgumentsCountThrowsException() {
        String[] bookRecord = {"Id", "Title", "Author", "Genres", "2.0", "2", "URL"};

        assertThrows(IllegalArgumentException.class, () -> Book.of(bookRecord),
            "Parsing a book should throws exception when the size of line is not equal to the count of arguments.");
    }

    @Test
    void testNoGenresParseCorrectly() {
        String[] bookRecord = {"Id", "Title", "Author", "Description", "[]", "2.0", "2", "URL"};
        Book book = Book.of(bookRecord);

        assertEquals(book.genres().size(), 0, "The count of genres for a book without genres should be 0.");
    }

    @Test
    void testGenresParseCorrectly() {
        String[] bookRecord = {"Id", "Title", "Author", "Description", "['GenreA', 'GenreB']", "2.0", "2", "URL"};
        Book book = Book.of(bookRecord);

        List<String> expectedGenres = List.of("GenreA", "GenreB");
        assertEquals(expectedGenres, book.genres(),
            "Genres in input text should match the parsed book genres.");
    }

    @Test
    void testRatingNumberParseCorrectly() {
        String[] bookRecord = {"Id", "Title", "Author", "Description", "['GenreA']", "2.0", "1,000,000", "URL"};
        Book book = Book.of(bookRecord);

        assertEquals(1_000_000, book.ratingCount(), "Commas in rating count should be removed after parsing.");
    }
}