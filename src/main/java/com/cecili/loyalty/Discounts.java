/**
 * Copyright 2016 NCR Corporation
 */
package com.cecili.loyalty;

import java.util.HashMap;

/**
 * Database of discounts for specific categories
 * 
 * @author Cecili Reid (cr250220) on Jan 19, 2017
 */
public class Discounts {
    // TODO: Can turn provider into object
    private final Discount[] discounts = {
            new Discount(ProductType.ANIMAL_CARE, 20.00, "Saturn's Petco"),
            new Discount(ProductType.CLEANING_SUPPLIES, 10.11, "Poseidon's Loyalty Provider"),
            new Discount(ProductType.DRINKS, 50.50, "99 bottles bar")
    };

    private HashMap<ProductType, Discount> map;

    public Discounts() {
        map = new HashMap<ProductType, Discount>();
        for (Discount discount : discounts) {
            map.put(discount.getType(), discount);
        }
    }

    /**
     * @return Number of discounts in map/database
     */
    public int getSize() {
        return map.size();
    }

    public Discount getDiscount(ProductType type) {
        return map.get(type);
    }
}
