package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.AbstractStoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.RoundingMode;

public class DLC extends AbstractStoreItem {

    private Game game;

    public DLC(String title, BigDecimal price, LocalDateTime releaseDate, Game game) {
        super(title, price, releaseDate);
        this.game = game;
    }
}
