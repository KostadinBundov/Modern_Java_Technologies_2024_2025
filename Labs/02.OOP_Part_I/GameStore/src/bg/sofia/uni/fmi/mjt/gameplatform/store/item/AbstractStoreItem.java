package bg.sofia.uni.fmi.mjt.gameplatform.store.item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public abstract class AbstractStoreItem implements StoreItem {
    protected String title;
    protected BigDecimal price;
    protected LocalDateTime releaseDate;
    protected double rating;
    protected int peopleVoting = 0;

    public AbstractStoreItem(String title, BigDecimal price, LocalDateTime releaseDate) {
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public double getRating() {
        if(peopleVoting == 0) {
            return 0;
        }

        return rating / (double)peopleVoting;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void rate(double rating) {
        this.rating+= rating;
        this.peopleVoting++;
    }
}