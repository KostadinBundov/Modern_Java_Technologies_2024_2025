package bg.sofia.uni.fmi.mjt.olympics.comparator;


import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NationMedalComparatorTest {
    @Mock
    private MJTOlympics olympics;

    @Test
    void testCompareWithDifferentTotalMedals() {
        NationMedalComparator comparator = new NationMedalComparator(olympics);
        when(olympics.getTotalMedals("Bulgarian")).thenReturn(5);
        when(olympics.getTotalMedals("German")).thenReturn(3);

        int result = comparator.compare("Bulgarian", "German");

        assertEquals(-1, result, "Bulgarian should come before German when it has more medals");
    }
}
