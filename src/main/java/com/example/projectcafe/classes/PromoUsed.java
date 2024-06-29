package com.example.projectcafe.classes;

public class PromoUsed {
    private String promoName;
    private Integer promoUsed;

    public PromoUsed(String promoName, Integer promoUsed) {
        this.promoName = promoName;
        this.promoUsed = promoUsed;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public Integer getPromoUsed() {
        return promoUsed;
    }

    public void setPromoUsed(Integer promoUsed) {
        this.promoUsed = promoUsed;
    }
}
