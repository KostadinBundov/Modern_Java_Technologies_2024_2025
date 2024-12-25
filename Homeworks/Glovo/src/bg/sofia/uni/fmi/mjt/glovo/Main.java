package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Main {
    public static void main(String[] args) {
        char[][] singleFieldLayout = {
            {'R', 'C', 'A'}
        };

        Glovo glovoSingleField = new Glovo(singleFieldLayout);

        try {
            MapEntity client = new MapEntity(new Location(0, 1), MapEntityType.CLIENT);
            MapEntity restaurant = new MapEntity(new Location(0, 0), MapEntityType.RESTAURANT);

            Delivery fastestDelivery = glovoSingleField.getFastestDelivery(client, restaurant, "Pizza");
            System.out.println("Fastest Delivery (Single Field): " + fastestDelivery);
        } catch (InvalidOrderException | NoAvailableDeliveryGuyException e) {
            System.out.println("Error in single field map: " + e.getMessage());
        }
    }
}
