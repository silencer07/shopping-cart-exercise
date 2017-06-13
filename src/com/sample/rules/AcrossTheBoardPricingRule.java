package com.sample.rules;

import com.sample.ShoppingCart;
import com.sample.dto.ComputedProduct;
import com.sample.dto.ComputedShoppingCart;
import com.sample.model.PromoCode;
import com.sample.model.PromoCodeDiscountUnit;
import com.sample.model.PromoCodeEligibility;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AcrossTheBoardPricingRule extends AbstractPricingRule {

    @Override
    public void compute(ComputedShoppingCart cart, LocalDate dateOfComputation) {
        if(cart == null){
            throw new IllegalArgumentException("computed shopping cart cannot be null");
        }

        ShoppingCart orig = cart.getOriginalCart();
        for(PromoCode promo: orig.getPromoCodes()){
            boolean applicable = isPromoCodeApplicable(promo, cart, dateOfComputation);
            if(applicable && promo.getEligibility() == PromoCodeEligibility.ACROSS_THE_BOARD && !cart.isPromoCodeApplied(promo)){
                for(ComputedProduct computedProduct: cart.getComputedProducts().keySet()){
                    BigDecimal price = computedProduct.getPrice();
                    if(promo.getDiscountUnit() == PromoCodeDiscountUnit.EXACT){
                        computedProduct.setPrice(price.subtract(promo.getDiscount()));
                    } else if((promo.getDiscountUnit() == PromoCodeDiscountUnit.PERCENTAGE)){
                        BigDecimal discount = price.multiply(promo.getDiscount());
                        computedProduct.setPrice(price.subtract(discount));
                    } else {
                        throw new UnsupportedOperationException(promo.getDiscountUnit() + " type of unit implementation not defined");
                    }
                }
            }
        }
    }
}
