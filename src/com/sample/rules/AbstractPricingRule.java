package com.sample.rules;

import com.sample.dto.ComputedShoppingCart;
import com.sample.model.PromoCode;
import com.sample.model.PromoCodeEffectDuration;
import static com.sample.model.PromoCodeEffectDuration.UNIT;

import java.time.LocalDate;

public abstract class AbstractPricingRule implements PricingRule {

    @Override
    public void compute(ComputedShoppingCart cart) {
        compute(cart, LocalDate.now());
    }

    public abstract void compute(ComputedShoppingCart cart, LocalDate dateOfComputation);

    protected boolean isPromoCodeApplicable(PromoCode promo, LocalDate cartDate, LocalDate dateOfComputation){
        LocalDate dateOfComputationCopy = dateOfComputation != null ? dateOfComputation :  LocalDate.now();
        if(promo == null || cartDate == null){
            throw new IllegalArgumentException("promo or shopping cart date cannot be null");
        } else if(promo.getExpirationDate() != null && dateOfComputationCopy.isAfter(promo.getExpirationDate())){
            return false;
        }

        PromoCodeEffectDuration duration = promo.getDuration();
        if(duration != null){
            switch (duration.getUnit()){
                case MONTH:
                    return dateOfComputationCopy.getMonthValue() - cartDate.getMonthValue() >= duration.getValue();
                default: throw new UnsupportedOperationException(duration.getUnit() + "  checking not yet implemented.");
            }
        }

        return true;
    }
}
