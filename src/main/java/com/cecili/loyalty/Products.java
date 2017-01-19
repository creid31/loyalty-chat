/**
 * Copyright 2016 NCR Corporation
 */
package com.cecili.loyalty;

import java.util.HashMap;

/**
 * Database of products
 * 
 * @author Cecili Reid (cr250220) on Jan 19, 2017
 */
public class Products {
    private final Product[] products = {
            new Product("UPC 758149170596", "Sims 4 Themed Tide", 12.00, ProductType.CLEANING_SUPPLIES),
            new Product("UPC 023100314785", "Alien Dog Food", 50.00, ProductType.ANIMAL_CARE),
            new Product("UPC 858629001027", "Best Jamaican Drink Ever", 1000.10, ProductType.DRINKS),
            new Product("UPC 077345094452", "Peanut Butter Jelly Time", 1.20, ProductType.ANIMAL_CARE),
            new Product("UPC 014800582239", "Sugarless lemonade", 12.25, ProductType.CLEANING_SUPPLIES),
            new Product("", "Premature wine", 2.14, ProductType.DRINKS)
    };

    private HashMap<String, Product> map;

    public Products() {
        map = new HashMap<String, Product>();
        for (Product product : products) {
            map.put(product.getBarcodeId(), product);
        }
    }

    /**
     * @return the number of products in database/map
     */
    public int getSize() {
        return map.size();
    }

    /**
     * @param Barcode id
     * @return Product with the given barcode id
     */
    public Product getProduct(String id) {
        return map.get(id);
    }

}
