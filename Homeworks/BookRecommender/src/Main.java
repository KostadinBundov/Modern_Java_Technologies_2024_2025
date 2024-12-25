import bg.sofia.uni.fmi.mjt.goodreads.BookLoader;
import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.finder.BookFinder;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.BookRecommender;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite.CompositeSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions.TFIDFSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Book> books;
        try (Reader reader = new FileReader("goodreads_data.csv")) {
            books = BookLoader.load(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> stopwords;
        TextTokenizer tokenizer;
        try (Reader reader = new FileReader("stopwords.txt")) {
            tokenizer = new TextTokenizer(reader);
            BookFinder bf = new BookFinder(books, tokenizer);

            //Set<String> genres = Set.of("Classics", "Fiction", "Dystopia", "Fantasy", "Politics", "School", "Literature");
            //Set<String> genres = Set.of("Classics", "Fiction");
            //System.out.println(bf.searchByGenres(genres, MatchOption.MATCH_ANY).size());

            //Set<String> keywords = Set.of("rights", "person");
            //System.out.println(bf.searchByKeywords(keywords, MatchOption.MATCH_ALL).size());
        } catch(FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SimilarityCalculator calc1 = new GenresOverlapSimilarityCalculator();
        SimilarityCalculator calc2 = new TFIDFSimilarityCalculator(books, tokenizer);
        SimilarityCalculator compositeCalc = new CompositeSimilarityCalculator(
            Map.of(
                calc1, 0.4,
                calc2, 0.6
            ));
        BookRecommender br = new BookRecommender(books, compositeCalc);

        Book book = books.stream().filter(b -> b.ID().equals("100")).findFirst().get();
        Map<Book, Double> recommended = br.recommendBooks(book, 10);

        //Book a = books.stream().filter(e -> e.ID().equals("4970")).findFirst().get();
        // Book b = books.stream().filter(e -> e.ID().equals("4971")).findFirst().get();

        // System.out.println(a.hashCode());
        //System.out.println(b.hashCode());

        System.out.println(recommended.size());
        System.out.println(
            recommended.entrySet().stream().map(es -> es.getKey().title() + ", id: " + es.getKey().ID() + ", val: " + es.getValue() + "\n").toList());
    }
}