package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControlCenterTest {
    private ControlCenter controlCenter;

    @BeforeEach
    void setUp() {
        char[][] map = new char[][]{
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', '.', '.', '.', '.', '.', 'R', '.', '.', 'C', '.', '#'},
            {'#', '.', '#', '#', '.', '.', '.', '.', '#', '.', '.', '#'},
            {'#', 'B', '.', '.', 'A', '#', '.', '.', '.', '.', 'R', '#'},
            {'#', '.', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#'},
            {'#', 'C', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '#', '.', '#', '#', '#', '#', '.', '#', '#', '#'},
            {'#', '.', '.', '.', '.', '.', '.', '.', 'B', '.', '.', '#'},
            {'#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '#'},
            {'#', '.', '.', '.', 'R', '.', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };

        controlCenter = new ControlCenter(map);
    }

    @Test
    void testFindOptimalDeliveryGuyCheapest() {
        Location restaurantLocation = new Location(9, 4);
        Location clientLocation = new Location(1, 9);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            restaurantLocation, clientLocation, Double.MAX_VALUE, Integer.MAX_VALUE, ShippingMethod.CHEAPEST);

        assertEquals(new Location(7, 8), deliveryInfo.deliveryGuyLocation(), "Delivery guy location should match");
        assertEquals(57.0, deliveryInfo.price(), 0.01, "Delivery price should match expected value");
        assertEquals(95, deliveryInfo.estimatedTime(), "Estimated time should match expected value");
    }

    @Test
    void testFindOptimalDeliveryGuyFastest() {
        Location restaurantLocation = new Location(9, 4);
        Location clientLocation = new Location(1, 9);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            restaurantLocation, clientLocation, Double.MAX_VALUE, Integer.MAX_VALUE, ShippingMethod.FASTEST);

        assertEquals(new Location(3, 4), deliveryInfo.deliveryGuyLocation(), "Delivery guy location should match");
        assertEquals(125.0, deliveryInfo.price(), 0.01, "Delivery price should match expected value");
        assertEquals(75, deliveryInfo.estimatedTime(), "Estimated time should match expected value");
    }

    @Test
    void testFindOptimalDeliveryGuyFastestWithConstraints() {
        Location restaurantLocation = new Location(9, 4);
        Location clientLocation = new Location(1, 9);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            restaurantLocation, clientLocation, 60.0, 100, ShippingMethod.FASTEST);

        assertEquals(new Location(7, 8), deliveryInfo.deliveryGuyLocation(), "Delivery guy location should match");
        assertEquals(57.0, deliveryInfo.price(), 0.01, "Delivery price should match expected value");
        assertEquals(95, deliveryInfo.estimatedTime(), "Delivery time should match expected value");
    }
}