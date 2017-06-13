package com.sample.rules;

import com.sample.dto.ComputedShoppingCart;
import com.sample.model.PromoCode;
import com.sample.model.PromoCodeEffectDuration;

import java.time.LocalDate;

abstract class AbstractPricingRule implements PricingRule {

    @Override
    public void compute(ComputedShoppingCart cart) {
        compute(cart, LocalDate.now());
    }

    public abstract void compute(ComputedShoppingCart cart, LocalDate dateOfComputation);

    protected boolean isPromoCodeApplicable(PromoCode promo, ComputedShoppingCart cart, LocalDate dateOfComputation){
        LocalDate cartDate = cart.getOriginalCart().getDate();
        LocalDate dateOfComputationCopy = dateOfComputation != null ? dateOfComputation :  LocalDate.now();

        //check if the promo is not expired
        if(promo == null || cartDate == null){
            throw new IllegalArgumentException("promo or shopping cart date cannot be null");
        } else if(promo.getExpirationDate() != null && dateOfComputationCopy.isAfter(promo.getExpirationDate())){
            return false;
        }

        //check if promo is within duration
        PromoCodeEffectDuration duration = promo.getDuration();
        if(duration != null){
            switch (duration.getUnit()){
                case MONTH:
                    return dateOfComputationCopy.getMonthValue() - cartDate.getMonthValue() <= duration.getValue();
                default: throw new UnsupportedOperationException(duration.getUnit() + "  checking not yet implemented.");
            }
        }

        return true;
    }
}
