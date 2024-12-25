package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompetitionTest {
    private Competition firstCompetition;
    private Competition secondCompetition;

    @Mock
    private Competitor firstCompetitor;

    @Mock
    private Competitor secondCompetitor;

    private Set<Competitor> setupCompetitors(Competitor... competitors) {
        Set<Competitor> competitorsSet = new HashSet<>();
        for (Competitor competitor : competitors) {
            competitorsSet.add(competitor);
        }
        return competitorsSet;
    }

    @Test
    void testEqualCompetitions() {
        Set<Competitor> firstCompetitors = setupCompetitors(firstCompetitor);

        firstCompetition = new Competition("First competition", "First discipline", firstCompetitors);
        secondCompetition = new Competition("First competition", "First discipline", firstCompetitors);

        assertEquals(firstCompetition, secondCompetition,
            "Two competitions with same names and discipline should be equal");
    }

    @Test
    void testNotEqualCompetitions() {
        Set<Competitor> firstCompetitors = setupCompetitors(firstCompetitor);
        Set<Competitor> secondCompetitors = setupCompetitors(secondCompetitor);

        firstCompetition = new Competition("First competition", "First discipline", firstCompetitors);
        secondCompetition = new Competition("Second competition", "SecondDiscipline", secondCompetitors);

        assertNotEquals(firstCompetition, secondCompetition,
            "Two competitions with different names should not be equal");
    }

    @Test
    void testCreateCompetitionWithNullNameThrowsException() {
        Set<Competitor> competitors = setupCompetitors(firstCompetitor);

        assertThrows(IllegalArgumentException.class, () -> new Competition(null, "Athletics", competitors),
            "Creating a competition with null name should throw IllegalArgumentException");
    }

    @Test
    void testCreateCompetitionWithNullDisciplineThrowsException() {
        Set<Competitor> competitors = setupCompetitors(firstCompetitor);

        assertThrows(IllegalArgumentException.class, () -> new Competition("100m Sprint", null, competitors),
            "Creating a competition with null discipline should throw IllegalArgumentException");
    }

    @Test
    void testCreateCompetitionWithBlankNameThrowsException() {
        Set<Competitor> competitors = setupCompetitors(firstCompetitor);

        assertThrows(IllegalArgumentException.class, () -> new Competition("   ", "Athletics", competitors),
            "Creating a competition with blank name should throw IllegalArgumentException");
    }

    @Test
    void testCreateCompetitionWithEmptyCompetitorsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("100m Sprint", "Athletics", Set.of()),
            "Creating a competition with empty competitors should throw IllegalArgumentException");
    }
}
