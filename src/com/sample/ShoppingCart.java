package com.sample;

import com.sample.model.Product;
import com.sample.model.PromoCode;
import com.sample.rules.PricingRule;

import java.time.LocalDate;
import java.util.*;

public class ShoppingCart {
    private LocalDate date = LocalDate.now();

    private final Map<Product, Integer> cart = new HashMap<>();
    private final Set<PromoCode> promoCodes = new HashSet<>();

    private Set<PricingRule> rules = new HashSet<>();

    public ShoppingCart(Collection<PricingRule> rules){
        if(rules == null || rules.isEmpty()){
            throw new IllegalArgumentException("rules cannot be null or empty");
        }
        this.rules.addAll(rules);
    }

    public ShoppingCart(ShoppingCart other){
        this.date = other.date;
        this.promoCodes.addAll(other.promoCodes);
        this.cart.putAll(other.cart);
        this.rules.addAll(other.rules);
    }


    public void add(Product product, PromoCode promoCode) throws PromoNotApplicableException{
        if(product == null){
            throw new IllegalArgumentException("product cannot be null");
        }

        cart.put(product, cart.getOrDefault(product, 0) + 1);

        if(promoCode != null){
            checkIfPromoCodeApplicable(promoCode);
            promoCodes.add(promoCode);
        }
    }

    public void add(Product product) throws PromoNotApplicableException{
        add(product, null);
    }

    public Map<Product, Integer> getCart(){
        return this.cart;
    }

    public Set<PromoCode> getPromoCodes() {
        return promoCodes;
    }

    public LocalDate getDate() {
        return date;
    }

    private void checkIfPromoCodeApplicable(PromoCode promo) throws PromoNotApplicableException{
        Map<Product, Integer> products = promo.getProductsThatShouldBeInCart();
        for(Map.Entry<Product, Integer> entry : products.entrySet()){
            int count = cart.getOrDefault(entry.getKey(), 0);
            if(count == 0){
                throw new PromoNotApplicableException(
                        String.format("Cart must contain %d of %s", entry.getKey(), entry.getValue())
                );
            }
        }
    }

    public Set<PricingRule> getRules() {
        return rules;
    }
}
