package com.sample.model;

import java.math.BigDecimal;

public class Product {
    private final String code;
    private final String name;
    private final BigDecimal price;

    public Product(String code,String name,BigDecimal price){
        this.code = code;
        this.name = name;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return getCode().equalsIgnoreCase(product.getCode());

    }

    @Override
    public int hashCode() {
        return getCode().toLowerCase().hashCode();
    }

    public String getCode() {

        return code;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
