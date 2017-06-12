package com.sample.rules;

import com.sample.dto.ComputedShoppingCart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface PricingRule {
    void compute(ComputedShoppingCart cart);
    void compute(ComputedShoppingCart cart, LocalDate dateOfComputation);

    List<PricingRule> DEFAULT_RULES = new ArrayList<>(Arrays.asList(
            new AcrossTheBoardPricingRule()
    ));
}
