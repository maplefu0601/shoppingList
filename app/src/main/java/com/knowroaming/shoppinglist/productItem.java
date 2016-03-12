package com.knowroaming.shoppinglist;

import java.io.Serializable;

/**
 * Created by Daddy on 2016-03-11.
 */
public class productItem implements Serializable {

    private static final long serialVersionUID = -5435670920302756945L;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private String productName = "";
    private double price = 0.0f;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id=0;

    public productItem(Integer id, String name, double price) {
        this.setId(id);
        this.setProductName(name);
        this.setPrice(price);
    }
}
