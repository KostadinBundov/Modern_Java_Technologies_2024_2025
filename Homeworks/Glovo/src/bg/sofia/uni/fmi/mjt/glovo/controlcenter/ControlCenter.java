package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapParser;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.OptimalDelivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.utils.Pair;

import java.util.LinkedList;
import java.util.Queue;

public class ControlCenter implements ControlCenterApi {
    private final MapEntity[][] map;
    private static final double EPSILON_VALUE = 0.000000001;

    public ControlCenter(char[][] mapLayout) {
        map = MapParser.createMap(mapLayout);
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation, double maxPrice,
                                               int maxTime, ShippingMethod shippingMethod) {
        validateLocation(restaurantLocation);
        validateLocation(clientLocation);
        validateEntity(restaurantLocation, MapEntityType.RESTAURANT);
        validateEntity(clientLocation, MapEntityType.CLIENT);

        if (Math.abs(-1 - maxPrice) < EPSILON_VALUE) {
            maxPrice = Double.MAX_VALUE;
        }

        if (maxTime == -1) {
            maxTime = Integer.MAX_VALUE;
        }

        return estimateDelivery(restaurantLocation, clientLocation, maxPrice, maxTime, shippingMethod);
    }

    @Override
    public MapEntity[][] getLayout() {
        return map.clone();
    }

    private Pair<Location, Integer> findShortestPath(Location restaurant,
                                                     MapEntityType searchedEntity,
                                                     Location searchedLocation) {
        boolean[][] visited = new boolean[map.length][map[0].length];
        Queue<Pair<Location, Integer>> queue = new LinkedList<>();

        queue.add(new Pair<>(restaurant, 0));
        visited[restaurant.x()][restaurant.y()] = true;

        while (!queue.isEmpty()) {
            Pair<Location, Integer> currPair = queue.poll();

            if (currPair.key().equals(searchedLocation)) {
                return currPair;
            }

            if (searchedLocation == null) {
                if (searchedEntity == map[currPair.key().x()][currPair.key().y()].type()) {
                    return currPair;
                }
            }

            addNeighbours(queue, currPair, visited);
        }

        return null;
    }

    private void addNeighbours(Queue<Pair<Location, Integer>> queue,
                               Pair<Location, Integer> currPair,
                               boolean[][] visited) {
        int currX = currPair.key().x();
        int currY = currPair.key().y();
        int nextStepsCount = currPair.value() + 1;

        Location[] possibleNewLocations = {
            new Location(currX + 1, currY),
            new Location(currX - 1, currY),
            new Location(currX, currY + 1),
            new Location(currX, currY - 1)
        };

        for (Location location : possibleNewLocations) {
            if (checkIfLocationIsInBounds(location) && !visited[location.x()][location.y()] &&
                map[location.x()][location.y()].type() != MapEntityType.WALL) {
                visited[location.x()][location.y()] = true;
                queue.add(new Pair<>(location, nextStepsCount));
            }
        }
    }

    private boolean checkIfLocationIsInBounds(Location location) {
        return location.x() >= 0 && location.x() < map.length
            && location.y() >= 0 && location.y() < map[0].length;
    }

    private DeliveryInfo estimateDelivery(Location restaurantLocation, Location clientLocation, double maxPrice,
                                          int maxTime, ShippingMethod shippingMethod) {
        Pair<Location, Integer> pathToClient =
            findShortestPath(restaurantLocation, MapEntityType.CLIENT, clientLocation);

        if (pathToClient == null) {
            return null;
        }

        Pair<Location, Integer> pathToCarDeliveryGuy =
            findShortestPath(restaurantLocation, MapEntityType.DELIVERY_GUY_CAR, null);

        Pair<Location, Integer> pathToBikeDeliveryGuy =
            findShortestPath(restaurantLocation, MapEntityType.DELIVERY_GUY_BIKE, null);

        if (pathToCarDeliveryGuy == null && pathToBikeDeliveryGuy == null) {
            return null;
        }

        return estimateOptimalDelivery(pathToClient, pathToCarDeliveryGuy, pathToBikeDeliveryGuy,
            maxPrice, maxTime, shippingMethod);
    }

    private DeliveryInfo estimateOptimalDelivery(Pair<Location, Integer> pathToClient,
                                                 Pair<Location, Integer> pathToCarDeliveryGuy,
                                                 Pair<Location, Integer> pathToBikeDeliveryGuy,
                                                 double maxPrice, int maxTime, ShippingMethod shippingMethod) {
        OptimalDelivery bikeDelivery = new OptimalDelivery(pathToClient, pathToBikeDeliveryGuy, DeliveryType.BIKE);
        OptimalDelivery carDelivery = new OptimalDelivery(pathToClient, pathToCarDeliveryGuy, DeliveryType.CAR);

        boolean isBikeDeliveryGuyInRange = checkIsDeliveryInRange(bikeDelivery, maxPrice, maxTime);
        boolean isCarDeliveryGuyInRange = checkIsDeliveryInRange(carDelivery, maxPrice, maxTime);

        if (isBikeDeliveryGuyInRange && isCarDeliveryGuyInRange) {
            if (shippingMethod == ShippingMethod.FASTEST) {
                return bikeDelivery.getTime() < carDelivery.getTime() ?
                    createDeliveryInfo(bikeDelivery, pathToBikeDeliveryGuy) :
                    createDeliveryInfo(carDelivery, pathToCarDeliveryGuy);
            } else if (shippingMethod == ShippingMethod.CHEAPEST) {
                return bikeDelivery.getPrice() < carDelivery.getPrice() ?
                    createDeliveryInfo(bikeDelivery, pathToBikeDeliveryGuy) :
                    createDeliveryInfo(carDelivery, pathToCarDeliveryGuy);
            }
        } else if (isBikeDeliveryGuyInRange) {
            return createDeliveryInfo(bikeDelivery, pathToBikeDeliveryGuy);
        } else if (isCarDeliveryGuyInRange) {
            return createDeliveryInfo(carDelivery, pathToCarDeliveryGuy);
        }

        return null;
    }

    private DeliveryInfo createDeliveryInfo(OptimalDelivery delivery, Pair<Location, Integer> deliveryGuyPath) {
        return new DeliveryInfo(deliveryGuyPath.key(), delivery.getPrice(),
            delivery.getTime(), delivery.getDeliveryType());
    }

    boolean checkIsDeliveryInRange(OptimalDelivery delivery, double maxPrice, int maxTime) {
        return delivery.getPathToRestaurant() != null &&
            delivery.getPrice() <= maxPrice &&
            delivery.getTime() <= maxTime;
    }

    private void validateLocation(Location location) {
        if (!checkIfLocationIsInBounds(location)) {
            throw new InvalidOrderException("The location is outside the map's defined boundaries!");
        }
    }

    private void validateEntity(Location location, MapEntityType entityType) {
        if (map[location.x()][location.y()].type() != entityType) {
            throw new InvalidOrderException("There is no such entity on the specified location!");
        }
    }
}
