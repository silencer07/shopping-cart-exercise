package com.sample.dao;

import com.sample.model.Product;

import java.math.BigDecimal;
import java.util.HashMap;

public class ProductDao {
    private static HashMap<String,Product> products = new HashMap<>();

    static {
        add(new Product("ult_small", "Unlimited 1GB", new BigDecimal("24.90")));
        add(new Product("ult_medium", "Unlimited 2GB", new BigDecimal("29.9")));
        add(new Product("ult_large", "Unlimited 5GB", new BigDecimal("44.90")));
        add(new Product("1gb", "Unlimited 5GB", new BigDecimal("9.90")));
    }

    private static void add(Product p){
        products.put(p.getCode().toLowerCase(), p);
    }

    public Product findByCode(String code){
        if(code == null || code.isEmpty()){
            throw new IllegalArgumentException("code cannot be blank");
        }
        return products.get(code);
    }
}
