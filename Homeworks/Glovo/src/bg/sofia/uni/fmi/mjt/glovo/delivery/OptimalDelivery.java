package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.utils.Pair;

public class OptimalDelivery {
    private final Pair<Location, Integer> pathToClient;
    private final Pair<Location, Integer> pathToRestaurant;
    private final DeliveryType deliveryType;
    private double price;
    private int time;

    public OptimalDelivery(Pair<Location, Integer> pathToClient,
                           Pair<Location, Integer> pathToRestaurant,
                           DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
        this.pathToClient = pathToClient;
        this.pathToRestaurant = pathToRestaurant;

        estimateDeliveryPrice();
        estimateDeliveryTime();
    }

    public double getPrice() {
        return price;
    }

    public int getTime() {
        return  time;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public Pair<Location, Integer> getPathToRestaurant() {
        return pathToRestaurant;
    }

    private void estimateDeliveryTime() {
        if (pathToRestaurant != null) {
            time = (pathToClient.value()  + pathToRestaurant.value()) * deliveryType.getTimePerKm();
        } else {
            price = Double.MAX_VALUE;
        }
    }

    private void estimateDeliveryPrice() {
        if (pathToRestaurant != null) {
            price = (pathToClient.value()  + pathToRestaurant.value()) * deliveryType.getPricePerKm();
        } else {
            time = Integer.MAX_VALUE;
        }
    }
}