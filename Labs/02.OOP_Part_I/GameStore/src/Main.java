import bg.sofia.uni.fmi.mjt.gameplatform.store.GameStore;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.Game;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.DLC;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        Game game1 = new Game("The Witcher 3", new BigDecimal("49.99"), LocalDateTime.of(2015, 5, 19, 0, 0), "RPG");
        Game game2 = new Game("Cyberpunk 2077", new BigDecimal("59.99"), LocalDateTime.of(2020, 12, 10, 0, 0), "RPG");
        Game game3 = new Game("Minecraft", new BigDecimal("19.99"), LocalDateTime.of(2011, 11, 18, 0, 0), "Sandbox");

        DLC dlc1 = new DLC("Hearts of Stone", new BigDecimal("9.99"), LocalDateTime.of(2015, 10, 13, 0, 0), game1);

        StoreItem[] items = {game1, game2, game3, dlc1};

        GameStore gameStore = new GameStore(items);

        System.out.println("\nApplying promo code VAN40 (40% discount):");
        gameStore.applyDiscount("VAN40");

        for (StoreItem item : items) {
            System.out.println(item.getTitle() + " - Discounted Price after VAN40: " + item.getPrice());
        }

        System.out.println("\nApplying promo code 100YO (100% discount):");
        gameStore.applyDiscount("100YO");

        for (StoreItem item : items) {
            System.out.println(item.getTitle() + " - Discounted Price after 100YO: " + item.getPrice());
        }
    }

}
