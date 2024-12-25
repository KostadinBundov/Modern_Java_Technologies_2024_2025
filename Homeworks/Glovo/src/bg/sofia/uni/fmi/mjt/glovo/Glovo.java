package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Glovo implements GlovoApi {
    private final ControlCenter controlCenter;

    public Glovo(char[][] mapLayout) {
        controlCenter = new ControlCenter(mapLayout);
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {
        validateClient(client);
        validateRestaurant(restaurant);
        validateFoodItem(foodItem);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(),
            client.location(),
            -1.0,
            -1,
            ShippingMethod.CHEAPEST);

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("There is no available delivery guy!");
        }

        return createDelivery(client, restaurant, foodItem, deliveryInfo);
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {
        validateClient(client);
        validateRestaurant(restaurant);
        validateFoodItem(foodItem);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(),
            client.location(),
            -1.0,
            -1,
            ShippingMethod.FASTEST);

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("There is no available delivery guy!");
        }

        return createDelivery(client, restaurant, foodItem, deliveryInfo);
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant, String foodItem,
                                                 double maxPrice) throws NoAvailableDeliveryGuyException {
        validateClient(client);
        validateRestaurant(restaurant);
        validateFoodItem(foodItem);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(),
            client.location(),
            maxPrice,
            -1,
            ShippingMethod.FASTEST);

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("There is no available delivery guy!");
        }

        return createDelivery(client, restaurant, foodItem, deliveryInfo);
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant, String foodItem,
                                                       int maxTime) throws NoAvailableDeliveryGuyException {
        validateClient(client);
        validateRestaurant(restaurant);
        validateFoodItem(foodItem);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(),
            client.location(),
            -1.0,
            maxTime,
            ShippingMethod.CHEAPEST);

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("There is no available delivery guy!");
        }

        return createDelivery(client, restaurant, foodItem, deliveryInfo);
    }

    private Delivery createDelivery(MapEntity client, MapEntity restaurant,
                                    String foodItem, DeliveryInfo deliveryInfo) {
        return new Delivery(
            client.location(),
            restaurant.location(),
            deliveryInfo.deliveryGuyLocation(),
            foodItem,
            deliveryInfo.price(),
            deliveryInfo.estimatedTime());
    }

    private void validateClient(MapEntity client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null!");
        }
    }

    private void validateRestaurant(MapEntity restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null!");
        }
    }

    private void validateFoodItem(String foodItem) {
        if (foodItem == null || foodItem.isBlank()) {
            throw new IllegalArgumentException("Food item cannot be null or blank!");
        }
    }
}