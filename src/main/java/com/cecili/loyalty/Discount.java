/**
 * Copyright 2016 NCR Corporation
 */
package com.cecili.loyalty;

import java.io.Serializable;

/**
 * @author Cecili Reid (cr250220) on Jan 19, 2017
 *
 */
public class Discount implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5022109524288686941L;

    private final ProductType type;

    private Double discount;

    private final String provider;

    public Discount(ProductType type, Double discount, String provider) {
        this.type = type;
        this.setDiscount(discount);
        this.provider = provider;
    }

    /**
     * @return the type of product the discount is to be applied
     */
    public ProductType getType() {
        return type;
    }

    /**
     * @return the discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * @return the provider of the discount
     */
    public String getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(">>Discount for ");
        builder.append(type.toString());
        builder.append(" from loyalty provider ");
        builder.append(provider);
        builder.append("<br> > $");
        builder.append(discount);
        builder.append("<br>");
        return builder.toString();
    }
}
