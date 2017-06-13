package com.sample.dto;

import com.sample.ShoppingCart;
import com.sample.model.Product;
import com.sample.model.PromoCode;
import com.sample.rules.PricingRule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ComputedShoppingCart {

    private final ShoppingCart originalCart;
    private final Map<ComputedProduct, Integer> computedProducts = new HashMap<>();
    private final Set<PromoCode> appliedPromoCodes = new HashSet<>();

    public ComputedShoppingCart(ShoppingCart originalCart){
        this.originalCart = new ShoppingCart(originalCart);

        for(Map.Entry<Product, Integer> entry: this.originalCart.getCart().entrySet()){
            ComputedProduct c = new ComputedProduct(entry.getKey());
            computedProducts.put(c, entry.getValue());
        }
    }

    public ShoppingCart getOriginalCart() {
        return originalCart;
    }

    public Map<ComputedProduct, Integer> getComputedProducts() {
        return computedProducts;
    }

    public Set<PromoCode> getAppliedPromoCodes() {
        return appliedPromoCodes;
    }

    public boolean isPromoCodeApplied(PromoCode promoCode){
        return appliedPromoCodes.contains(promoCode);
    }

    public void compute(){
        compute(null);
    }

    public void compute(LocalDate dateOfComputation){
        for(PricingRule rule : originalCart.getRules()){
            rule.compute(this, dateOfComputation);
        }
    }

    public BigDecimal getTotal(){
        BigDecimal total  = BigDecimal.ZERO;
        for(Map.Entry<ComputedProduct, Integer> entry : computedProducts.entrySet()){
            total = total.add(entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue())));
        }
        return total;
    }

    public void addAppliedPromo(PromoCode promo){
        if(promo == null){
            throw new IllegalArgumentException("promo code cannot be null");
        }
        appliedPromoCodes.add(promo);
    }
}
