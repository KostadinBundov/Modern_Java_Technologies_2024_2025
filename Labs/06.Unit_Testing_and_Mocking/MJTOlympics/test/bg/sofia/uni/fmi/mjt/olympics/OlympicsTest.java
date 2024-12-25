package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OlympicsTest {
    @Mock
    private CompetitionResultFetcher competitionResultFetcher;

    @Mock
    private Competition competition;

    @Mock
    private Competitor firstCompetitor;

    @Mock
    private Competitor secondCompetitor;

    @Mock
    private Competitor thirdCompetitor;

    @Mock
    private Competitor fourthCompetitor;

    private MJTOlympics olympics;

    private TreeSet<Competitor> createRanking(Competitor... competitors) {
        TreeSet<Competitor> ranking = new TreeSet<>(Comparator.comparing(Competitor::getIdentifier));

        Collections.addAll(ranking, competitors);

        return ranking;
    }

    @Test
    void testRegisteredCompetitorsShouldReturnCorrectCount() {
        olympics = new MJTOlympics(
            Set.of(firstCompetitor),
            competitionResultFetcher
        );

        assertEquals(1, olympics.getRegisteredCompetitors().size(), "When creating olympics with one competitor the count of competitors should be 1");
    }

    @Test
    void testNotAllCompetitorsAreRegisteredForCompetition() {
        olympics = new MJTOlympics(
            Set.of(firstCompetitor, secondCompetitor),
            competitionResultFetcher
        );

        when(competition.competitors()).thenReturn(Set.of(firstCompetitor, secondCompetitor, thirdCompetitor));

        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(competition),
            "IllegalArgumentException is thrown if not all competitors are registered for the competition");
    }

    @Test
    void testUpdateMedalStatisticsAssignsCorrectMedals() {
        when(firstCompetitor.getIdentifier()).thenReturn("1");
        when(secondCompetitor.getIdentifier()).thenReturn("2");
        when(thirdCompetitor.getIdentifier()).thenReturn("3");

        olympics = new MJTOlympics(
            Set.of(firstCompetitor, secondCompetitor, thirdCompetitor),
            competitionResultFetcher
        );

        TreeSet<Competitor> ranking = createRanking(firstCompetitor, secondCompetitor, thirdCompetitor);

        when(competitionResultFetcher.getResult(competition)).thenReturn(ranking);
        when(competition.competitors()).thenReturn(ranking);

        olympics.updateMedalStatistics(competition);

        verify(firstCompetitor).addMedal(Medal.GOLD);
        verify(secondCompetitor).addMedal(Medal.SILVER);
        verify(thirdCompetitor).addMedal(Medal.BRONZE);
    }

    @Test
    void testFourthPlaceDoesNotReceiveMedal() {
        when(firstCompetitor.getIdentifier()).thenReturn("1");
        when(secondCompetitor.getIdentifier()).thenReturn("2");
        when(thirdCompetitor.getIdentifier()).thenReturn("3");
        when(fourthCompetitor.getIdentifier()).thenReturn("4");

        olympics = new MJTOlympics(
            Set.of(firstCompetitor, secondCompetitor, thirdCompetitor, fourthCompetitor),
            competitionResultFetcher
        );

        TreeSet<Competitor> ranking = createRanking(firstCompetitor, secondCompetitor, thirdCompetitor, fourthCompetitor);

        when(competitionResultFetcher.getResult(competition)).thenReturn(ranking);
        when(competition.competitors()).thenReturn(ranking);

        olympics.updateMedalStatistics(competition);

        verify(firstCompetitor).addMedal(Medal.GOLD);
        verify(secondCompetitor).addMedal(Medal.SILVER);
        verify(thirdCompetitor).addMedal(Medal.BRONZE);
        verify(fourthCompetitor, never()).addMedal(any());
    }

    @Test
    void testUpdateMedalTable() {
        when(firstCompetitor.getNationality()).thenReturn("Bulgarian");
        when(firstCompetitor.getIdentifier()).thenReturn("1");

        TreeSet<Competitor> ranking = createRanking(firstCompetitor);

        when(competitionResultFetcher.getResult(competition)).thenReturn(ranking);
        when(competition.competitors()).thenReturn(ranking);

        olympics = new MJTOlympics(
            Set.of((firstCompetitor)),
            competitionResultFetcher
        );

        olympics.updateMedalStatistics(competition);

        Map<String, EnumMap<Medal, Integer>> medalTable = olympics.getNationsMedalTable();

        assertTrue(medalTable.containsKey("Bulgarian"),
            "When there is competitor from country A, medal table should contains country A");
        assertEquals(1, medalTable.get("Bulgarian").size(), "When there is single competitor from country A, for country A expected single medal");
    }

    @Test
    void testGetNationsRanklist() {
        when(firstCompetitor.getNationality()).thenReturn("Bulgarian");
        when(secondCompetitor.getNationality()).thenReturn("German");
        when(thirdCompetitor.getNationality()).thenReturn("Bulgarian");

        when(firstCompetitor.getIdentifier()).thenReturn("1");
        when(secondCompetitor.getIdentifier()).thenReturn("2");
        when(thirdCompetitor.getIdentifier()).thenReturn("3");

        olympics = new MJTOlympics(
            Set.of(firstCompetitor, secondCompetitor, thirdCompetitor),
            competitionResultFetcher
        );

        TreeSet<Competitor> ranking = createRanking(firstCompetitor, secondCompetitor, thirdCompetitor);

        when(competitionResultFetcher.getResult(competition)).thenReturn(ranking);
        when(competition.competitors()).thenReturn(ranking);

        olympics.updateMedalStatistics(competition);
        List<String> nationsRankList = new ArrayList<>(olympics.getNationsRankList());

        assertEquals(List.of("Bulgarian", "German"), nationsRankList,
            "The rank list should be sorted by total medal counts in descending order");
    }

    @Test
    void testGetTotalMedals() {
        when(firstCompetitor.getNationality()).thenReturn("Bulgarian");
        when(secondCompetitor.getNationality()).thenReturn("German");
        when(thirdCompetitor.getNationality()).thenReturn("Bulgarian");

        when(firstCompetitor.getIdentifier()).thenReturn("1");
        when(secondCompetitor.getIdentifier()).thenReturn("2");
        when(thirdCompetitor.getIdentifier()).thenReturn("3");

        olympics = new MJTOlympics(
            Set.of(firstCompetitor, secondCompetitor, thirdCompetitor),
            competitionResultFetcher
        );

        TreeSet<Competitor> ranking = createRanking(firstCompetitor, secondCompetitor, thirdCompetitor);

        when(competitionResultFetcher.getResult(competition)).thenReturn(ranking);
        when(competition.competitors()).thenReturn(ranking);

        olympics.updateMedalStatistics(competition);

        int totalMedalsForBulgaria = olympics.getTotalMedals("Bulgarian");

        assertEquals(2, olympics.getTotalMedals("Bulgarian"),
            "When nationality has won two medals, the total count of medals for this nationality should be 2");

        assertEquals(1, olympics.getTotalMedals("German"),
            "When nationality has won one medal, the total count of medals for this nationality should be 1");
    }

    @Test
    void testInvalidNationality() {
        when(firstCompetitor.getNationality()).thenReturn("Bulgarian");
        when(secondCompetitor.getNationality()).thenReturn("German");

        when(firstCompetitor.getIdentifier()).thenReturn("1");
        when(secondCompetitor.getIdentifier()).thenReturn("2");

        olympics = new MJTOlympics(
            Set.of(firstCompetitor, secondCompetitor),
            competitionResultFetcher
        );

        TreeSet<Competitor> ranking = createRanking(firstCompetitor, secondCompetitor);

        when(competitionResultFetcher.getResult(competition)).thenReturn(ranking);
        when(competition.competitors()).thenReturn(ranking);

        olympics.updateMedalStatistics(competition);

        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals("Greek"),
            "Get total medals for nationality, that is not in the competition should throw IllegalArgumentException");
    }
}