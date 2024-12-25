package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class GlovoTest {
    private char[][] map;
    private Glovo glovo;

    @BeforeEach
    void setUp() {
        map = new char[][]{
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

        glovo = new Glovo(map);
    }

    @Test
    void testGetCheapestDelivery() {
        MapEntity client = new MapEntity(new Location(5, 1), MapEntityType.CLIENT); // Клиент
        MapEntity restaurant = new MapEntity(new Location(1, 6), MapEntityType.RESTAURANT); // Ресторант

        try {
            Delivery delivery = glovo.getCheapestDelivery(client, restaurant, "Pizza");

            assertEquals(new Location(3, 1), delivery.deliveryGuy(), "Delivery guy location should match");

            assertEquals(48.0, delivery.price(), 0.01, "Delivery price should match expected value");
            assertEquals(80, delivery.estimatedTime(), "Delivery time should match expected value");
        } catch (NoAvailableDeliveryGuyException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetFastestDelivery() {
        MapEntity client = new MapEntity(new Location(1, 9), MapEntityType.CLIENT); // Клиент
        MapEntity restaurant = new MapEntity(new Location(1, 6), MapEntityType.RESTAURANT); // Ресторант

        try {
            Delivery delivery = glovo.getFastestDelivery(client, restaurant, "Burger");

            assertEquals(new Location(3, 4), delivery.deliveryGuy(), "Delivery guy location should match");

            assertEquals(35.0, delivery.price(), 0.01, "Delivery price should match expected value");
            assertEquals(21, delivery.estimatedTime(), "Delivery time should match expected value");
        } catch (NoAvailableDeliveryGuyException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetFastestDeliveryUnderPrice() {
        MapEntity client = new MapEntity(new Location(5, 1), MapEntityType.CLIENT); // Клиент
        MapEntity restaurant = new MapEntity(new Location(1, 6), MapEntityType.RESTAURANT); // Ресторант

        try {
            Delivery delivery = glovo.getFastestDeliveryUnderPrice(client, restaurant, "Sushi", 70.0);

            assertEquals(new Location(3, 4), delivery.deliveryGuy(), "Delivery guy location should match");

            assertEquals(65, delivery.price(), 0.01, "Delivery price should match expected value");
            assertEquals(39, delivery.estimatedTime(), "Delivery time should match expected value");
        } catch (NoAvailableDeliveryGuyException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetCheapestDeliveryWithinTimeLimit() {
        MapEntity client = new MapEntity(new Location(1, 9), MapEntityType.CLIENT); // Клиент
        MapEntity restaurant = new MapEntity(new Location(1, 6), MapEntityType.RESTAURANT); // Ресторант

        try {
            Delivery delivery = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, "Pizza", 25);

            assertEquals(new Location(3, 4), delivery.deliveryGuy(), "Delivery guy location should match");

            assertEquals(35.0, delivery.price(), 0.01, "Delivery price should match expected value");
            assertEquals(21, delivery.estimatedTime(), "Delivery time should match expected value");
        } catch (NoAvailableDeliveryGuyException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetCheapestDeliveryThrowsInvalidOrderExceptionForMissingClient() {
        MapEntity client = new MapEntity(new Location(0, 0), MapEntityType.CLIENT); // Няма клиент на (0, 0)
        MapEntity restaurant = new MapEntity(new Location(1, 6), MapEntityType.RESTAURANT); // Ресторант

        assertThrows(InvalidOrderException.class, () -> {
            glovo.getCheapestDelivery(client, restaurant, "Pizza");
        }, "Expected InvalidOrderException when client is missing at the specified location");
    }

    @Test
    void testGetCheapestDeliveryThrowsInvalidOrderExceptionForMissingRestaurant() {
        MapEntity client = new MapEntity(new Location(5, 1), MapEntityType.CLIENT); // Клиент
        MapEntity restaurant = new MapEntity(new Location(0, 0), MapEntityType.RESTAURANT); // Няма ресторант на (0, 0)

        assertThrows(InvalidOrderException.class, () -> {
            glovo.getCheapestDelivery(client, restaurant, "Pizza");
        }, "Expected InvalidOrderException when restaurant is missing at the specified location");
    }

    @Test
    void testGetCheapestDeliveryThrowsNoAvailableDeliveryGuyException() {
        MapEntity client = new MapEntity(new Location(3, 1), MapEntityType.CLIENT); // Клиент
        MapEntity restaurant = new MapEntity(new Location(1, 3), MapEntityType.RESTAURANT); // Ресторант

        char[][] emptyDeliveryMap = {
            {'#', '#', '#', '.', '#'},
            {'#', '.', '.', 'R', '.'},
            {'.', '.', '#', '.', '#'},
            {'#', 'C', '.', '.', '.'},
            {'#', '.', '#', '#', '#'}
        };

        Glovo glovoWithoutDeliveryGuys = new Glovo(emptyDeliveryMap);

        assertThrows(NoAvailableDeliveryGuyException.class, () -> {
            glovoWithoutDeliveryGuys.getCheapestDelivery(client, restaurant, "Pizza");
        }, "Expected NoAvailableDeliveryGuyException when no delivery guys are available");
    }

    @Test
    void testGetCheapestDeliveryThrowsInvalidOrderExceptionForOutOfBoundsLocation() {
        MapEntity client = new MapEntity(new Location(-1, -1), MapEntityType.CLIENT); // Позиция извън границите
        MapEntity restaurant = new MapEntity(new Location(1, 6), MapEntityType.RESTAURANT); // Ресторант

        assertThrows(InvalidOrderException.class, () -> {
            glovo.getCheapestDelivery(client, restaurant, "Pizza");
        }, "Expected InvalidOrderException for out-of-bounds location");
    }

    @Test
    void testGetCheapestDeliveryThrowsInvalidOrderExceptionForInvalidFoodItem() {
        MapEntity client = new MapEntity(new Location(5, 1), MapEntityType.CLIENT); // Клиент
        MapEntity restaurant = new MapEntity(new Location(1, 6), MapEntityType.RESTAURANT); // Ресторант

        assertThrows(IllegalArgumentException.class, () -> {
            glovo.getCheapestDelivery(client, restaurant, null);
        }, "Expected InvalidOrderException when food item is null");

        assertThrows(IllegalArgumentException.class, () -> {
            glovo.getCheapestDelivery(client, restaurant, "");
        }, "Expected InvalidOrderException when food item is empty");
    }

    @Test
    void testGetCheapestDeliveryThrowsInvalidOrderExceptionForBlankFoodItem() {
        MapEntity client = new MapEntity(new Location(5, 1), MapEntityType.CLIENT); // Клиент
        MapEntity restaurant = new MapEntity(new Location(1, 6), MapEntityType.RESTAURANT); // Ресторант

        assertThrows(IllegalArgumentException.class, () -> {
            glovo.getCheapestDelivery(client, restaurant, "");
        }, "Expected InvalidOrderException when food item is null");

        assertThrows(IllegalArgumentException.class, () -> {
            glovo.getCheapestDelivery(client, restaurant, "");
        }, "Expected InvalidOrderException when food item is empty");
    }

    @Test
    void testGetFastestDeliveryUnderPriceThrowsNoAvailableDeliveryGuyExceptionForUnreachableConstraint() {
        MapEntity client = new MapEntity(new Location(5, 1), MapEntityType.CLIENT); // Клиент
        MapEntity restaurant = new MapEntity(new Location(1, 6), MapEntityType.RESTAURANT); // Ресторант

        assertThrows(NoAvailableDeliveryGuyException.class, () -> {
            glovo.getFastestDeliveryUnderPrice(client, restaurant, "Pizza", 10.0); // Цената е твърде ниска
        }, "Expected NoAvailableDeliveryGuyException when no delivery meets the price constraint");
    }

    @Test
    void testNoValidDeliveryGuyNoPathToClient() {
        char[][] map = new char[][]{
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', 'R', '.', '.', '.', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '#', 'C', '#', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}
        };
        Glovo glovo = new Glovo(map);

        MapEntity client = new MapEntity(new Location(3, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 1), MapEntityType.RESTAURANT);

        assertThrows(NoAvailableDeliveryGuyException.class, () ->
                glovo.getCheapestDelivery(client, restaurant, "Pizza"),
            "Expected NoAvailableDeliveryGuyException for no path to client"
        );
    }

    @Test
    void testNoValidDeliveryGuyNoDeliveryGuysOnMap() {
        char[][] map = new char[][]{
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', 'R', '.', '.', '.', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '#', 'C', '#', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}
        };
        Glovo glovo = new Glovo(map);

        MapEntity client = new MapEntity(new Location(3, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 1), MapEntityType.RESTAURANT);

        assertThrows(NoAvailableDeliveryGuyException.class, () ->
                glovo.getFastestDelivery(client, restaurant, "Pizza"),
            "Expected NoAvailableDeliveryGuyException for no delivery guys on map"
        );
    }

    @Test
    void testNoValidDeliveryGuyNoPathToDeliveryGuy() {
        char[][] map = new char[][]{
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', 'R', '.', '.', '.', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '#', 'C', '#', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '.', '#', 'A', '#', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}
        };
        Glovo glovo = new Glovo(map);

        MapEntity client = new MapEntity(new Location(3, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 1), MapEntityType.RESTAURANT);

        assertThrows(NoAvailableDeliveryGuyException.class, () ->
                glovo.getCheapestDelivery(client, restaurant, "Pizza"),
            "Expected NoAvailableDeliveryGuyException for no path to delivery guy"
        );
    }

    @Test
    void testNoValidDeliveryGuyNoDeliveryMeetsConstraints() {
        char[][] map = new char[][]{
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', 'R', '.', '.', '.', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '#', 'C', '#', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', 'A', '.', '.', '.', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}
        };
        Glovo glovo = new Glovo(map);

        MapEntity client = new MapEntity(new Location(3, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 1), MapEntityType.RESTAURANT);

        assertThrows(NoAvailableDeliveryGuyException.class, () ->
                glovo.getFastestDeliveryUnderPrice(client, restaurant, "Pizza", 10.0),
            "Expected NoAvailableDeliveryGuyException for no delivery meeting constraints"
        );
    }

    @Test
    void testNoRestaurantOnMap() {
        char[][] map = new char[][]{
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', '.', '.', '.', '.', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '#', 'C', '#', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}
        };
        Glovo glovo = new Glovo(map);

        MapEntity client = new MapEntity(new Location(3, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 1), MapEntityType.RESTAURANT);

        assertThrows(InvalidOrderException.class, () ->
                glovo.getCheapestDelivery(client, restaurant, "Pizza"),
            "Expected InvalidOrderException when no restaurant is on the map"
        );
    }

    @Test
    void testNullRestaurantArgument() {
        char[][] map = new char[][]{
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', 'R', '.', '.', '.', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '#', 'C', '#', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}
        };
        Glovo glovo = new Glovo(map);

        MapEntity client = new MapEntity(new Location(3, 3), MapEntityType.CLIENT);

        assertThrows(IllegalArgumentException.class, () ->
                glovo.getCheapestDelivery(client, null, "Pizza"),
            "Expected InvalidOrderException when restaurant argument is null"
        );
    }

    @Test
    void testNullClientArgument() {
        char[][] map = new char[][]{
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', 'R', '.', '.', '.', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '#', 'C', '#', '.', '#'},
            {'#', '.', '#', '#', '#', '.', '#'},
            {'#', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}
        };
        Glovo glovo = new Glovo(map);

        MapEntity restaurant = new MapEntity(new Location(1, 1), MapEntityType.RESTAURANT);

        assertThrows(IllegalArgumentException.class, () ->
                glovo.getFastestDelivery(null, restaurant, "Pizza"),
            "Expected InvalidOrderException when client argument is null"
        );
    }
}