package dev.julioestrada.katasolid;

class GildedRose {
    Item[] items;

    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert";
    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";

    private static final int AGED_BRIE_DOUBLE_QUALITY_DECREMENT_SELL_IN_THRESHOLD = 0;

    private static final int BACKSTAGE_PASSES_INCREASE_QUALITY_TO_DOUBLE_SELL_IN_THRESHOLD = 10;
    private static final int BACKSTAGE_PASSES_INCREASE_QUALITY_TO_TRIPLE_SELL_IN_THRESHOLD = 5;
    private static final int BACKSTAGE_PASSES_RESET_SELL_IN_THRESHOLD = 0;

    private static final int DEFAULT_ITEM_DOUBLE_QUALITY_DECREASE_SELL_IN_THRESHOLD = 0;

    private static final int MAX_QUALITY = 50;
    private static final int MIN_QUALITY = 0;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    private void setMinQuality(Item item) {
        item.quality = 0;
    }

    private void setMaxQuality(Item item) {
        item.quality = 50;
    }

    private void increaseQuality(Item item, int quality) {
        if (item.quality < MAX_QUALITY && (item.quality + quality) < 50) {
            item.quality += quality;
        } else {
            setMaxQuality(item);
        }
    }

    private void decreaseQuality(Item item, int quality) {
        if (item.quality > MIN_QUALITY && (item.quality - quality) > 0) {
            item.quality -= quality;
        } else {
            setMinQuality(item);
        }
    }

    private void decreaseSellIn(Item item) {
        item.sellIn -= 1;
    }

    private void updateBackstagePassesQuality(Item item) {
        if (item.sellIn == BACKSTAGE_PASSES_RESET_SELL_IN_THRESHOLD) {
            setMinQuality(item);
        } else if (item.sellIn <= BACKSTAGE_PASSES_INCREASE_QUALITY_TO_TRIPLE_SELL_IN_THRESHOLD) {
            increaseQuality(item, 3);
        } else if (item.sellIn <= BACKSTAGE_PASSES_INCREASE_QUALITY_TO_DOUBLE_SELL_IN_THRESHOLD) {
            increaseQuality(item, 2);
        } else {
            increaseQuality(item, 1);
        }
    }

    private void updateDefaultQuality(Item item) {
        if (item.sellIn == DEFAULT_ITEM_DOUBLE_QUALITY_DECREASE_SELL_IN_THRESHOLD) {
            decreaseQuality(item, 2);
        } else {
            decreaseQuality(item, 1);
        }
    }

    private void updateAgeBrieQuality(Item item) {
        increaseQuality(item, 1);
        if (item.sellIn < AGED_BRIE_DOUBLE_QUALITY_DECREMENT_SELL_IN_THRESHOLD) {
            decreaseQuality(item, 2);
        }
    }

    public void updateQuality() {
        for (Item item : items) {
            switch (item.name) {
                case AGED_BRIE:
                    updateAgeBrieQuality(item);
                    decreaseSellIn(item);
                    break;
                case BACKSTAGE_PASSES:
                    updateBackstagePassesQuality(item);
                    decreaseSellIn(item);
                    break;
                case SULFURAS:
                    break;
                default:
                    updateDefaultQuality(item);
                    decreaseSellIn(item);
                    break;
            }
        }
    }

}