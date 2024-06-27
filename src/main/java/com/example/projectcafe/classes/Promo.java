package com.example.projectcafe.classes;

public class Promo {
    private String periode;
    private String paymentType;
    private Double totalDiscount;
    private Integer category;
    private Integer menu;
    private String promoName;




    public Promo(String periode, String paymentType, Double totalDiscount, Integer category, Integer menu, String promoName) {
        this.periode = periode;
        this.paymentType = paymentType;
        this.totalDiscount = totalDiscount;
        this.category = category;
        this.menu = menu;
        this.promoName = promoName;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getMenu() {
        return menu;
    }

    public void setMenu(Integer menu) {
        this.menu = menu;
    }
}
