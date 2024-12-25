package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Book(
    String ID,
    String title,
    String author,
    String description,
    List<String> genres,
    double rating,
    int ratingCount,
    String URL
) {
    private static final int ID_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int AUTHOR_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;
    private static final int GENRES_INDEX = 4;
    private static final int RATING_INDEX = 5;
    private static final int RATING_COUNT_INDEX = 6;
    private static final int URL_INDEX = 7;

    private static final int EMPTY_ARR_STR_LENGTH = 2;
    private static final int PROPERTIES_COUNT = 8;

    public static Book of(String[] tokens) {
        if (tokens.length != PROPERTIES_COUNT) {
            throw new IllegalArgumentException("Book record cannot be correctly created!");
        }

        String id = tokens[ID_INDEX];
        String title = tokens[TITLE_INDEX];
        String author = tokens[AUTHOR_INDEX];
        String description = tokens[DESCRIPTION_INDEX];
        List<String> genres = extractGenres(tokens[GENRES_INDEX]);
        double rating = Double.parseDouble(tokens[RATING_INDEX]);
        int ratingCount = extractRatingCount(tokens[RATING_COUNT_INDEX]);
        String url = tokens[URL_INDEX];

        return new Book(id, title, author, description, genres, rating, ratingCount, url);
    }

    private static List<String> extractGenres(String genres) {
        if (genres.length() == EMPTY_ARR_STR_LENGTH) {
            return List.of();
        }

        return Stream
            .of(genres.substring(2, genres.length() - 2).split(","))
            .map(g -> g.replace("'", "").trim())
            .collect(Collectors.toList());
    }

    private static int extractRatingCount(String ratingCount) {
        return Integer.parseInt(
            ratingCount
                .replaceAll(",", "")
                .trim()
        );
    }
}