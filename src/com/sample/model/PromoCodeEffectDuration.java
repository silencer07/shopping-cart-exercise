package com.sample.model;

public class PromoCodeEffectDuration {

    private final int value;
    private final UNIT unit;

    public PromoCodeEffectDuration(int value, UNIT unit){
        this.value = value;
        this.unit = unit;
    }

    public enum UNIT {
        DAY, //will not implement since not part of requirements yet. TODO
        MONTH,
        YEAR //will not implement since not part of requirements yet. TODO
    }

    public int getValue() {
        return value;
    }

    public UNIT getUnit() {
        return unit;
    }
}
