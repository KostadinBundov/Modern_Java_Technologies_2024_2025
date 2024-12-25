package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;

public class GameStore implements StoreAPI{
    static final String[] promoCodes = {"VAN40", "100YO"};
    static final BigDecimal hundredPercents = new BigDecimal(100);
    static final BigDecimal[] percentages = {new BigDecimal(40), new BigDecimal(100)};
    StoreItem[] availableItems;
    boolean isPromoCodeApplied = false;

    public GameStore(StoreItem[] availableItems) {
        this.availableItems = availableItems;
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        StoreItem[] filteredItems = new StoreItem[getMatchFiltersItemsCount(itemFilters)];
        int index = 0;

        for(StoreItem item : availableItems) {
            boolean matchFilters = true;

            for (ItemFilter filter : itemFilters) {
                if(!filter.matches(item)) {
                    matchFilters = false;
                    break;
                }
            }

            if(matchFilters) {
                filteredItems[index++] = item;
            }
        }

        return filteredItems;
    }

    @Override
    public void applyDiscount(String promoCode) {
        if(isPromoCodeApplied) {
            return;
        }

        int promoCodeIndex = getPromoCodeIndex(promoCode);

        if(promoCodeIndex == -1) {
            return;
        }

        BigDecimal multiplier = hundredPercents.subtract(percentages[promoCodeIndex]);
        multiplier = multiplier.divide(hundredPercents);

        for(StoreItem var : availableItems) {
            var.setPrice(var.getPrice().multiply(multiplier));
        }

        isPromoCodeApplied = true;
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if(rating < 1 || rating > 5) {
            return false;
        }

        int index = getItemIndex(item);

        if(index == -1) {
            return false;
        }

        availableItems[index].rate(rating);
        return true;
    }

    private int getPromoCodeIndex(String promoCode) {
        for (int i = 0; i < promoCodes.length; i++) {
            if(promoCode.equals(promoCodes[i])) {
                return i;
            }
        }

        return -1;
    }

    private int getMatchFiltersItemsCount(ItemFilter[] itemFilters) {
        int matchesCount = 0;

        for (StoreItem item : availableItems) {
            boolean matchFilters = true;

            for (ItemFilter filter : itemFilters) {
                if (!filter.matches(item)) {
                    matchFilters = false;
                    break;
                }
            }
            if (matchFilters) {
                matchesCount++;
            }
        }
        return matchesCount;
    }

    private int getItemIndex(StoreItem item) {
        for(int i = 0; i < availableItems.length; i++) {
            if(item == availableItems[i]) {
                return i;
            }
        }

        return -1;
    }
}