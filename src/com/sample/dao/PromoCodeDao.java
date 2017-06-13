package com.sample.dao;

import com.sample.model.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PromoCodeDao {

    private final ProductDao productDao;

    //initialize just for the sample code. in general should not be present in real dao
    private Set<PromoCode> promos = new HashSet<>();

    public PromoCodeDao(ProductDao dao) {
        this.productDao = dao;
        initializePromos();
    }

    private void initializePromos(){
        promos.add(
                PromoCode.Builder.newInstance("Three_for_Two", "3 for 2 deal")
                    .discount(new BigDecimal("0.3333"))
                    .duration(new PromoCodeEffectDuration(1, PromoCodeEffectDuration.UNIT.MONTH))
                    .productsThatShouldBeInCart(
                            createProductMap(
                                    productDao.findByCode("ult_small"), new Integer(3)
                            )
                    )
                    .usage(PromoCodeUsage.MULTIPLE)
                    .eligibility(PromoCodeEligibility.ONE_PRODUCT)
                    .eligibleProducts(
                            createProductMap(
                                    productDao.findByCode("ult_small"), new Integer(3)
                            )
                    )
                .build()
        );

        promos.add(
                PromoCode.Builder.newInstance("5gb_Bulk_Discount", "Unlimited 5GB bulk discount")
                        .discount(new BigDecimal(5))
                        .discountUnit(PromoCodeDiscountUnit.EXACT)
                        .duration(new PromoCodeEffectDuration(1, PromoCodeEffectDuration.UNIT.MONTH))
                        .productsThatShouldBeInCart(
                                createProductMap(
                                        productDao.findByCode("ult_large"), new Integer(3)
                                )
                        )
                        .countMatching(PromoCodeCountMatching.GREATER_THAN)
                        .usage(PromoCodeUsage.MULTIPLE)
                        .eligibility(PromoCodeEligibility.ONE_PRODUCT)
                        .eligibleProducts(
                                createProductMap(
                                    productDao.findByCode("ult_large"), new Integer(0) //zero means apply to all in the cart
                                )
                        )
                        .build()
        );

        promos.add(
                PromoCode.Builder.newInstance("1gb_DataPack_Free", " For every Unlimited 2gb sold")
                        .freebies(
                                createProductMap(
                                        productDao.findByCode("1gb"), new Integer(1)
                                )
                        )
                        .productsThatShouldBeInCart(
                                createProductMap(
                                        productDao.findByCode("ult_medium"), new Integer(1)
                                )
                        )
                        .usage(PromoCodeUsage.MULTIPLE)
                        .eligibility(PromoCodeEligibility.ONE_PRODUCT)
                        .eligibleProducts(
                                createProductMap(
                                    productDao.findByCode("ult_medium"), new Integer(1)
                                )
                        )
                        .build()
        );

        promos.add(
                PromoCode.Builder.newInstance("I<3AMAYSIM", "10% discount across the board")
                        .discount(new BigDecimal("0.1"))
                        .trigger(PromoCodeTrigger.MANUAL)
                        .build()
        );
    }

    //NOTE: simple sugar syntax to make builder easier to use. for internal use of this class only.
    private Map<Product, Integer> createProductMap(Object... args){
        Map<Product, Integer> map = new HashMap<>();
        Product lastProduct = null;
        for(Object arg: args){
            if(arg == null){
              throw new IllegalArgumentException("cannot add null to the product map");
            } else if(arg instanceof Product){
                lastProduct =(Product) arg;
                map.put(lastProduct, 0);
            } else if(arg instanceof Integer){
                map.put(lastProduct, (Integer) arg);
            }
        }
        return map;
    }

    public Set<PromoCode> list(){
        return promos;
    }

    public Optional<PromoCode> findByCode(String code){
        if(code == null || code.isEmpty()) {
            throw new IllegalArgumentException("code cannot be blank");
        }

        return promos.stream().filter(promo -> promo.getCode().equalsIgnoreCase(code)).findFirst();
    }

    public Set<PromoCode> findAllAutomaticPromoCodes(){
        return promos.stream().filter(p -> p.getTrigger() == PromoCodeTrigger.AUTOMATIC)
                .collect(Collectors.toSet());
    }
}
