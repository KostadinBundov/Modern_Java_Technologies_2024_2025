package bg.sofia.uni.fmi.mjt.olympics.competitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AthleteTest {
    private Athlete athlete;

    @BeforeEach
    void setup() {
        athlete = new Athlete("Default identifier", "Default name", "Default nationality");
    }

    @Test
    void testAthleteAddSingleGoldMedal() {
        athlete.addMedal(Medal.GOLD);

        assertTrue(athlete.getMedals().contains(Medal.GOLD),
            "The medals after adding single GOLD medal should contain GOLD");
        assertEquals(1, athlete.getMedals().size(), "The number of medals after adding single medal should be 1");
    }

    @Test
    void testAthleteAddSingleSilverMedal() {
        athlete.addMedal(Medal.SILVER);

        assertTrue(athlete.getMedals().contains(Medal.SILVER),
            "The medals after adding single SILVER medal should contain SILVER");
        assertEquals(1, athlete.getMedals().size(), "The number of medals after adding single medal should be 1");
    }

    @Test
    void testAthleteAddSingleBronzeMedal() {
        athlete.addMedal(Medal.BRONZE);

        assertTrue(athlete.getMedals().contains(Medal.BRONZE),
            "The medals after adding single BRONZE medal should contain BRONZE");
        assertEquals(1, athlete.getMedals().size(), "The number of medals after adding single medal should be 1");
    }

    @Test
    void testAthleteAddDuplicatedMedals() {
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);

        assertEquals(2, athlete.getMedals().size(), "The medals count after adding the same medal twice should be 2");
    }

    @Test
    void testAthleteAddMultipleMedals() {
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.SILVER);
        athlete.addMedal(Medal.BRONZE);

        assertEquals(3, athlete.getMedals().size(), "The medals count after adding 3 different medals should be 3");
        assertTrue(athlete.getMedals().containsAll(Set.of(Medal.SILVER, Medal.GOLD, Medal.BRONZE)),
            "The medals after adding GOLD, SILVER, and BRONZE must contain GOLD, SILVER, BRONZE");
    }

    @Test
    void testAthleteEqualsAnotherAthlete() {
        Athlete other = new Athlete("Default identifier", "Default name", "Default nationality");

        assertEquals(athlete, other, "The athlete with the same fields data should be equal");
    }

    @Test
    void testAthleteNotEqualAnotherAthlete() {
        Athlete other = new Athlete("Default identifier==", "Another name", "Default nationality");

        assertNotEquals(athlete, other, "Athletes with different names should not be equal");
    }

    @Test
    void testAthleteNotEqualAnotherBasedOnMedals() {
        Athlete other = new Athlete("Default identifier", "Default name", "Default nationality");
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.BRONZE);
        other.addMedal(Medal.BRONZE);
        other.addMedal(Medal.SILVER);

        assertNotEquals(athlete, other, "Athletes with different medals should not be equal");
    }

    @Test
    void testGetIdentifier() {
        assertEquals("Default identifier", athlete.getIdentifier(),
            "The identifier of the athlete should match the one provided during creation");
    }

    @Test
    void testGetName() {
        assertEquals("Default name", athlete.getName(),
            "The name of the athlete should match the one provided during creation");
    }

    @Test
    void testGetNationality() {
        assertEquals("Default nationality", athlete.getNationality(),
            "The nationality of the athlete should match the one provided during creation");
    }

    @Test
    void testGetMedalsIsImmutable() {
        athlete.addMedal(Medal.GOLD);

        List<Medal> medals = athlete.getMedals();
        assertThrows(UnsupportedOperationException.class, () -> medals.add(Medal.BRONZE),
            "The medals list should be immutable and throw an exception when trying to modify it");
    }

    @Test
    void testAddNullMedalThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> athlete.addMedal(null),
            "Adding a null medal should throw IllegalArgumentException");
    }

    @Test
    void testHashCodeConsistency() {
        Athlete other = new Athlete("Default identifier", "Default name", "Default nationality");
        assertEquals(athlete.hashCode(), other.hashCode(),
            "Hash codes for equal athletes should be the same");
    }
}
