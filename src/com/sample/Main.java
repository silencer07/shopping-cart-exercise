package com.sample;

import com.sample.dao.ProductDao;
import com.sample.dao.PromoCodeDao;
import com.sample.dto.ComputedShoppingCart;
import com.sample.rules.PricingRule;

import java.math.BigDecimal;

public class Main {

    private ProductDao productDao = new ProductDao();
    private PromoCodeDao promoCodeDao = new PromoCodeDao(productDao);

    public static void main(String[] args) {
        Main main = new Main();
        main.execute();
    }

    public void execute(){
        //scenario 1
        try {
            ShoppingCart shoppingCart = new ShoppingCart(PricingRule.DEFAULT_RULES);
            shoppingCart.add(productDao.findByCode("ult_small"));
            shoppingCart.add(productDao.findByCode("ult_small"));
            shoppingCart.add(productDao.findByCode("ult_small"));
            shoppingCart.add(productDao.findByCode("ult_large"));

            ComputedShoppingCart computedShoppingCart = new ComputedShoppingCart(shoppingCart);

            computedShoppingCart.compute();

            System.out.println("Total for scenario 1 is: " + computedShoppingCart.getTotal().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        } catch (PromoNotApplicableException e){
            System.out.println(e.getMessage());
        }

        //scenario 4
        try {
            ShoppingCart shoppingCart = new ShoppingCart(PricingRule.DEFAULT_RULES);
            shoppingCart.add(productDao.findByCode("ult_small"));
            shoppingCart.add(productDao.findByCode("1gb"), promoCodeDao.findByCode("I<3AMAYSIM").get());

            ComputedShoppingCart computedShoppingCart = new ComputedShoppingCart(shoppingCart);

            computedShoppingCart.compute();

            System.out.println("Total for scenario 4 is: " + computedShoppingCart.getTotal().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        } catch (PromoNotApplicableException e){
            System.out.println(e.getMessage());
        }
    }
}
