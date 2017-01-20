/**
 * Copyright 2016 NCR Corporation
 */
package com.cecili.loyalty;

import java.io.Serializable;

/**
 * Describes attributes of a product for retail
 * 
 * @author Cecili Reid (cr250220) on Jan 19, 2017
 */
public class Product implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Barcode id of product i.e UPC758149170596
     */
    private final String barcodeId;

    /**
     * Name of the product
     */
    private final String name;

    /**
     * Current price of product before discount
     */
    private Double price;

    /**
     * The category to which the product belongs
     */
    private ProductType type;

    public Product(String barcodeId, String name, Double price, ProductType type) {
        super();
        this.barcodeId = barcodeId;
        this.name = name;
        this.price = price;
        this.setType(type);
    }

    /**
     * @return the current price of product
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @return the name of product
     */
    public String getName() {
        return name;
    }

    /**
     * @return the barcode id of product (i.e. UPC758149170596)
     */
    public String getBarcodeId() {
        return barcodeId;
    }

    /**
     * New price for product
     * 
     * @param price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return type the product's category type
     */
    public ProductType getType() {
        return type;
    }

    /**
     * @param type the new category type for product
     */
    public void setType(ProductType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(">>Product ");
        builder.append(name);
        builder.append(" with barcode ");
        builder.append(barcodeId);
        builder.append(" of type ");
        builder.append(type.toString());
        builder.append("<br> > $");
        builder.append(price);
        builder.append("<br>");
        return builder.toString();
    }
}
