package com.sample.dto;

import com.sample.model.Product;

import java.math.BigDecimal;

public class ComputedProduct {

    private final Product product;
    private BigDecimal price;

    public ComputedProduct(Product product){
        if(product == null){
            throw new  IllegalArgumentException("product cannot be null");
        }

        this.product = product;
        this.price = product.getPrice();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product getProduct() {

        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComputedProduct that = (ComputedProduct) o;

        if (!getProduct().equals(that.getProduct())) return false;
        return getPrice().equals(that.getPrice());

    }

    @Override
    public int hashCode() {
        int result = getProduct().hashCode();
        result = 31 * result + getPrice().hashCode();
        return result;
    }
}
