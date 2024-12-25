package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookFinder implements BookFinderAPI {
    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.tokenizer = tokenizer;
    }

    public Set<Book> allBooks() {
        return Set.copyOf(books);
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        if (authorName == null) {
            throw new IllegalArgumentException("Author's name cannot be null!");
        }

        if (authorName.isEmpty()) {
            throw new IllegalArgumentException("Author's name cannot be empty!");
        }

        return books
            .stream()
            .filter(b -> b.author().equals(authorName))
            .toList();
    }

    @Override
    public Set<String> allGenres() {
        return books
            .stream()
            .map(Book::genres)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        validateNotNullSet(genres);

        return switch (option) {
            case MatchOption.MATCH_ALL -> books
                .stream()
                .filter(b -> new HashSet<>(b.genres()).containsAll(genres))
                .toList();
            case MatchOption.MATCH_ANY -> books
                .stream()
                .filter(b -> b
                    .genres()
                    .stream()
                    .anyMatch(genres::contains))
                .toList();
        };
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        validateNotNullSet(keywords);

        return switch (option) {
            case MatchOption.MATCH_ALL -> books
                .stream()
                .filter(b -> textContainsAllWords(b.description() + " " + b.title(), keywords)
                )
                .toList();
            case MatchOption.MATCH_ANY -> books
                .stream()
                .filter(b -> textContainsAnyWords(b.description() + " " + b.title(), keywords))
                .toList();
        };
    }

    private void validateNotNullSet(Set<String> genres) {
        if (genres == null) {
            throw new IllegalArgumentException("Given genres cannot be null!");
        }
    }

    private boolean textContainsAllWords(String text, Set<String> words) {
        return new HashSet<>(tokenizer.tokenize(text)).containsAll(words);
    }

    private boolean textContainsAnyWords(String text, Set<String> words) {
        Set<String> tokenizedWords = new HashSet<>(tokenizer.tokenize(text));
        return words.stream().anyMatch(tokenizedWords::contains);
    }
}