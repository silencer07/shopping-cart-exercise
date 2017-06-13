package com.sample.rules;

import com.sample.dao.ProductDao;
import com.sample.dao.PromoCodeDao;
import com.sample.dto.ComputedProduct;
import com.sample.dto.ComputedShoppingCart;
import com.sample.model.Product;
import com.sample.model.PromoCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AutomaticPricingRule extends AbstractPricingRule {

    private ProductDao productDao = new ProductDao();
    private PromoCodeDao promoCodeDao = new PromoCodeDao(productDao);

    @Override
    public void compute(ComputedShoppingCart cart, LocalDate dateOfComputation) {
        if(cart == null){
            throw new IllegalArgumentException("computed shopping cart cannot be null");
        }

        Map<ComputedProduct, Integer> currentComputedProducts = cart.getComputedProducts();
        Map<ComputedProduct, Integer> computedProductsToAdd = new HashMap<>();
        for(PromoCode promo : promoCodeDao.findAllAutomaticPromoCodes()){
            if(hasTheProductsForPromoToApply(promo, cart) && isPromoCodeApplicable(promo, cart, dateOfComputation)){
                cart.addAppliedPromo(promo);

                switch (promo.getEligibility()){
                    case ONE_PRODUCT:
                        for(ComputedProduct computedProduct: cart.getComputedProducts().keySet()){
                            if(promo.getEligibleProducts().containsKey(computedProduct.getProduct())){
                                applyDiscount(cart, currentComputedProducts, computedProductsToAdd, promo, computedProduct);
                                addFreebies(currentComputedProducts, computedProductsToAdd, promo, computedProduct);
                            }
                        }
                        break;
                    case ACROSS_THE_BOARD:
                        //TODO call the AcrossTheBoardPricingRule. not part of the specs
                        throw new UnsupportedOperationException("call AcrossTheBoardPricingRuleHere");
                    default:
                        throw new UnsupportedOperationException(promo.getEligibility() + " not yet supported");
                }
            }
        }
        currentComputedProducts.putAll(computedProductsToAdd);

    }

    private void addFreebies(Map<ComputedProduct, Integer> currentComputedProducts, Map<ComputedProduct, Integer>
            computedProductsToAdd, PromoCode promo, ComputedProduct computedProduct) {
        if(promo.getFreebies() != null){
            for(Map.Entry<Product, Integer> entry: promo.getFreebies().entrySet()){
                ComputedProduct freebie = new ComputedProduct(entry.getKey());
                freebie.setPrice(BigDecimal.ZERO);
                switch(promo.getUsage()) {
                    case MULTIPLE:
                        int eligibleCount = promo.getEligibleProducts().get(computedProduct.getProduct());
                        //cart count / eligible count * number of freebies specified
                        int freebieCount = (currentComputedProducts.get(computedProduct) / eligibleCount) * entry.getValue();
                        computedProductsToAdd.put(freebie, freebieCount);
                        break;
                    case ONCE:
                        computedProductsToAdd.put(freebie, entry.getValue());
                        break;
                    default:
                        throw new UnsupportedOperationException(promo.getUsage() + " not supported");

                }
            }
        }
    }

    private void applyDiscount(ComputedShoppingCart cart, Map<ComputedProduct, Integer> currentComputedProducts,
                               Map<ComputedProduct, Integer> computedProductsToAdd, PromoCode promo, ComputedProduct computedProduct) {
        if(promo.getDiscount() != null && !promo.getDiscount().equals(BigDecimal.ZERO)){
            BigDecimal price = computedProduct.getPrice();
            BigDecimal finalPrice;

            switch (promo.getDiscountUnit()){
                case EXACT:
                    finalPrice = price.subtract(promo.getDiscount());
                    break;
                case PERCENTAGE:
                    BigDecimal discount = price.multiply(promo.getDiscount());
                    finalPrice = price.subtract(discount);
                    break;
                default:
                    throw new UnsupportedOperationException(promo.getDiscountUnit() + " type of unit implementation not defined");
            }
            finalPrice = finalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);

            int eligibleCount = promo.getEligibleProducts().get(computedProduct.getProduct());
            int productCount = cart.getComputedProducts().get(computedProduct);
            //zero in qty eligible means every product is eligible for discount
            if(eligibleCount == 0 || eligibleCount >= productCount){
                computedProduct.setPrice(finalPrice);
            } else {
                ComputedProduct newComputedProduct = new ComputedProduct(computedProduct.getProduct());
                newComputedProduct.setPrice(finalPrice);
                switch(promo.getUsage()){
                    case MULTIPLE:

                        if(productCount % eligibleCount == 0){
                            computedProduct.setPrice(finalPrice);
                        } else {
                            int toBeDiscounted = (productCount / eligibleCount) * eligibleCount;
                            newComputedProduct.setPrice(finalPrice);
                            //add a new computed product with discount applied
                            computedProductsToAdd.put(newComputedProduct, toBeDiscounted);

                            //subtract the count that are not eligible to discount
                            int newCount = productCount - toBeDiscounted;
                            currentComputedProducts.replace(computedProduct, newCount);
                        }
                        break;
                    case ONCE:
                        int toBeDiscounted = productCount - eligibleCount;
                        newComputedProduct.setPrice(finalPrice);
                        //add a new computed product with discount applied
                        computedProductsToAdd.put(newComputedProduct, toBeDiscounted);

                        //subtract the count that are not eligible to discount
                        int newCount = productCount - toBeDiscounted;
                        currentComputedProducts.replace(computedProduct, newCount);
                        break;
                    default: throw new UnsupportedOperationException(promo.getUsage() + " not supported");
                }
            }
        }
    }

    private boolean hasTheProductsForPromoToApply(PromoCode promo, ComputedShoppingCart computedShoppingCart){
        Map<Product, Integer> itemsInCart = computedShoppingCart.getOriginalCart().getCart();
        Map<Product, Integer> products = promo.getProductsThatShouldBeInCart();

        boolean result = true;
        for(Map.Entry<Product, Integer> entry : products.entrySet()){
            int count = itemsInCart.getOrDefault(entry.getKey(), 0);

            switch_statement:
            switch (promo.getCountMatching()){
                case EQUALS_OR_GREATER_THAN:
                    result = result && count >= entry.getValue();
                    break switch_statement;
                case GREATER_THAN:
                    result = result && count > entry.getValue();
                    break switch_statement;
                default:
                    throw new IllegalArgumentException(promo.getCountMatching() + " not yet supported");
            }
        }

        //if applicable regardless of the cart contents, let say shipping discounts
        return result;
    }
}
