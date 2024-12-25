package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookFinderTest {
    private BookFinder bookFinder;

    @Mock
    private TextTokenizer tokenizer;

    @Test
    void testSearchByAuthorThrowsExceptionForNull() {
        bookFinder = new BookFinder(Set.of(), tokenizer);

        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(null),
            "Should throw exception for null author name");
    }

    @Test
    void testSearchByAuthorThrowsExceptionForEmpty() {
        bookFinder = new BookFinder(Set.of(), tokenizer);

        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(""),
            "Should throw exception for empty author name");
    }

    @Test
    void testSearchByAuthor() {
        Book first = Mockito.mock(Book.class);
        Book second = Mockito.mock(Book.class);
        Book third = Mockito.mock(Book.class);

        when(first.author()).thenReturn("FirstAuthor");
        when(second.author()).thenReturn("SecondAuthor");
        when(third.author()).thenReturn("FirstAuthor");

        bookFinder = new BookFinder(Set.of(first, second, third), tokenizer);

        List<Book> result = bookFinder.searchByAuthor("FirstAuthor");

        assertEquals(result.size(), 2,
            "There should have 2 book by author.");
    }

    @Test
    void testAllGenres() {
        Book first = setUpBookForMatchGenres(List.of("GenreA", "GenreB", "GenreC"));
        Book second = setUpBookForMatchGenres(List.of("GenreA", "GenreD"));
        Book third = setUpBookForMatchGenres(List.of("GenreA", "GenreC", "GenreD"));

        bookFinder = new BookFinder(Set.of(first, second, third), tokenizer);

        Set<String> result = bookFinder.allGenres();

        assertEquals(result.size(), 4,
            "There should have 4 different genres.");
    }

    @Test
    void testAllGenresWithNoGenres() {
        Book first = setUpBookForMatchGenres(List.of());
        Book second = setUpBookForMatchGenres(List.of());
        Book third = setUpBookForMatchGenres(List.of());

        bookFinder = new BookFinder(Set.of(first, second, third), tokenizer);

        Set<String> result = bookFinder.allGenres();

        assertEquals(0, result.size(), "There should be no genres.");
    }

    @Test
    void testSearchByNullGenresThrowsException() {
        bookFinder = new BookFinder(Set.of(), tokenizer);

        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByGenres(null, MatchOption.MATCH_ALL),
            "Should throw exception for null genres list.");
    }

    @Test
    void testSearchByGenresMatchAll() {
        Book first = setUpBookForMatchGenres(List.of("GenreA", "GenreB"));
        Book second = setUpBookForMatchGenres(List.of("GenreA", "GenreC"));
        Book third = setUpBookForMatchGenres(List.of("GenreB", "GenreD"));

        bookFinder = new BookFinder(Set.of(first, second, third), tokenizer);

        List<Book> result = bookFinder.searchByGenres(Set.of("GenreA", "GenreB"), MatchOption.MATCH_ALL);

        assertEquals(1, result.size(), "Only 1 book should match all genres.");
    }

    @Test
    void testSearchByGenresMatchAny() {
        Book first = setUpBookForMatchGenres(List.of("GenreA", "GenreB"));
        Book second = setUpBookForMatchGenres(List.of("GenreA", "GenreC"));
        Book third = setUpBookForMatchGenres(List.of("GenreB", "GenreD"));

        bookFinder = new BookFinder(Set.of(first, second, third), tokenizer);

        List<Book> result = bookFinder.searchByGenres(Set.of("GenreA", "GenreB"), MatchOption.MATCH_ANY);

        assertEquals(3, result.size(), "3 books should match any genre.");
    }

    @Test
    void searchByNullKeyWordsThrowsException() {
        bookFinder = new BookFinder(Set.of(), tokenizer);

        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByKeywords(null, MatchOption.MATCH_ALL),
            "Should throw exception for null key words list.");

    }

    @Test
    void searchByKeyWordsMatchAll() {
        Book book = setUpBookForMatchKeyWords(
            "Second part of description",
            "First part",
            "Second part of description First part",
            List.of("second", "part", "description", "first"));

        bookFinder = new BookFinder(Set.of(book), tokenizer);

        List<Book> result = bookFinder.searchByKeywords(Set.of("second", "part", "description", "first"), MatchOption.MATCH_ALL);

        assertEquals(1, result.size(), "Book should match all keywords.");
    }

    @Test
    void searchByKeyWordsMatchAny() {
        Book book = setUpBookForMatchKeyWords(
            "Second",
            "First",
            "Second First",
            List.of("second", "first"));

        bookFinder = new BookFinder(Set.of(book), tokenizer);

        List<Book> result = bookFinder.searchByKeywords(Set.of("second", "anyWord"), MatchOption.MATCH_ANY);

        assertEquals(1, result.size(), "Book should match any of the keywords.");
    }

    private Book setUpBookForMatchGenres(List<String> genres) {
        Book book = Mockito.mock(Book.class);
        when(book.genres()).thenReturn(genres);
        return book;
    }

    private Book setUpBookForMatchKeyWords(String description, String title, String text, List<String> tokens) {
        Book book = Mockito.mock(Book.class);

        when(book.description()).thenReturn(description);
        when(book.title()).thenReturn(title);
        when(tokenizer.tokenize(text)).thenReturn(tokens);

        return book;
    }
}