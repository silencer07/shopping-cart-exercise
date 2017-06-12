package com.sample.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class PromoCode {
    private final String code;
    private final String name;
    //if null no expiration
    private LocalDate expirationDate;
    private PromoCodeApplicability applicability = PromoCodeApplicability.CAN_BE_APPLIED_WITH_OTHER_PROMOS;

    private PromoCodeEligibility eligibility = PromoCodeEligibility.ACROSS_THE_BOARD;
    private Set<Product> eligibleProducts = new HashSet<>();

    private BigDecimal discount;
    private PromoCodeDiscountUnit discountUnit = PromoCodeDiscountUnit.PERCENTAGE;
    //integer is the qty
    private Map<Product, Integer> freebies = new HashMap<>();

    //if empty, code will apply regardless of the products in cart
    //integer is the qty
    private Map<Product, Integer> productsThatShouldBeInCart = new HashMap<>();
    private PromoCodeCountMatching countMatching = PromoCodeCountMatching.EQUALS_OR_GREATER_THAN;

    //if null applies indefinitely
    private PromoCodeEffectDuration duration;

    private PromoCodeTrigger trigger = PromoCodeTrigger.AUTOMATIC;

    private PromoCodeUsage usage = PromoCodeUsage.ONCE;

    public PromoCode(String code, String name){
        this.code = code;
        this.name = name;
    }

    private PromoCode(Builder builder) {
        code = builder.code;
        name = builder.name;
        setExpirationDate(builder.expirationDate);
        setApplicability(builder.applicability);
        setEligibility(builder.eligibility);
        setDiscount(builder.discount);
        setDiscountUnit(builder.discountUnit);
        setFreebies(builder.freebies);
        productsThatShouldBeInCart.putAll(builder.productsThatShouldBeInCart);
        countMatching = builder.countMatching;
        duration = builder.duration;
        trigger = builder.trigger;
        usage = builder.usage;
        eligibleProducts.addAll(builder.eligibleProducts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PromoCode promoCode = (PromoCode) o;

        return getCode().equals(promoCode.getCode());

    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }

    public String getCode() {
        return code;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public PromoCodeApplicability getApplicability() {
        return applicability;
    }

    public void setApplicability(PromoCodeApplicability applicability) {
        this.applicability = applicability;
    }

    public PromoCodeEligibility getEligibility() {
        return eligibility;
    }

    public void setEligibility(PromoCodeEligibility eligibility) {
        this.eligibility = eligibility;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Map<Product, Integer> getFreebies() {
        return freebies;
    }

    public void setFreebies(Map<Product, Integer> freebies) {
        this.freebies = freebies;
    }

    public Map<Product, Integer> getProductsThatShouldBeInCart() {
        return productsThatShouldBeInCart;
    }

    public void setProductsThatShouldBeInCart(Map<Product, Integer> productsThatShouldBeInCart) {
        this.productsThatShouldBeInCart = productsThatShouldBeInCart;
    }

    public String getName() {
        return name;
    }

    public PromoCodeEffectDuration getDuration() {
        return duration;
    }

    public void setDuration(PromoCodeEffectDuration duration) {
        this.duration = duration;
    }

    public PromoCodeTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(PromoCodeTrigger trigger) {
        this.trigger = trigger;
    }

    public PromoCodeCountMatching getCountMatching() {
        return countMatching;
    }

    public void setCountMatching(PromoCodeCountMatching countMatching) {
        this.countMatching = countMatching;
    }

    public PromoCodeUsage getUsage() {
        return usage;
    }

    public void setUsage(PromoCodeUsage usage) {
        this.usage = usage;
    }

    public Set<Product> getEligibleProducts() {
        return eligibleProducts;
    }

    public void setEligibleProducts(Collection<Product> eligibleProducts) {
        this.eligibleProducts.addAll(eligibleProducts);
    }

    public PromoCodeDiscountUnit getDiscountUnit() {
        return discountUnit;
    }

    public void setDiscountUnit(PromoCodeDiscountUnit discountUnit) {
        this.discountUnit = discountUnit;
    }

    public static final class Builder {
        private final String code;
        private final String name;
        private LocalDate expirationDate;
        private PromoCodeApplicability applicability = PromoCodeApplicability.CAN_BE_APPLIED_WITH_OTHER_PROMOS;
        private PromoCodeEligibility eligibility = PromoCodeEligibility.ACROSS_THE_BOARD;
        private BigDecimal discount;
        private PromoCodeDiscountUnit discountUnit = PromoCodeDiscountUnit.PERCENTAGE;;
        private Map<Product, Integer> freebies;
        private Map<Product, Integer> productsThatShouldBeInCart = new HashMap<>();
        private PromoCodeCountMatching countMatching = PromoCodeCountMatching.EQUALS_OR_GREATER_THAN;
        private PromoCodeEffectDuration duration;
        private PromoCodeTrigger trigger = PromoCodeTrigger.AUTOMATIC;
        private PromoCodeUsage usage = PromoCodeUsage.ONCE;;
        private Set<Product> eligibleProducts = new HashSet<>();

        public Builder(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Builder newInstance(String code, String name){
            return new Builder(code, name);
        }

        public Builder expirationDate(LocalDate val) {
            expirationDate = val;
            return this;
        }

        public Builder applicability(PromoCodeApplicability val) {
            applicability = val;
            return this;
        }

        public Builder eligibility(PromoCodeEligibility val) {
            eligibility = val;
            return this;
        }

        public Builder discount(BigDecimal val) {
            discount = val;
            return this;
        }

        public Builder discountUnit(PromoCodeDiscountUnit val) {
            discountUnit = val;
            return this;
        }

        public Builder freebies(Map<Product, Integer> val) {
            freebies = val;
            return this;
        }

        public Builder productsThatShouldBeInCart(Map<Product, Integer> val) {
            productsThatShouldBeInCart.putAll(val);
            return this;
        }

        public Builder countMatching(PromoCodeCountMatching val) {
            countMatching = val;
            return this;
        }

        public Builder duration(PromoCodeEffectDuration val) {
            duration = val;
            return this;
        }

        public Builder trigger(PromoCodeTrigger val) {
            trigger = val;
            return this;
        }

        public Builder usage(PromoCodeUsage val) {
            usage = val;
            return this;
        }

        public Builder eligibleProducts(Product... products){
            eligibleProducts.addAll(Arrays.asList(products));
            return this;
        }

        public PromoCode build() {
            return new PromoCode(this);
        }
    }
}
