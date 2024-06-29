package com.example.projectcafe.classes;

public class Promo {
    private String periode;
    private String paymentType;
    private Double totalDiscount;
    private String category;
    private String menu;
    private String promoName;


    public Promo(Promo p){
        this.periode = p.periode;
        this.paymentType = p.paymentType;
        this.totalDiscount = p.totalDiscount;
        this.category = p.category;
        this.menu = p.menu;
        this.promoName = p.promoName;
    }

    public Promo(String periode, String paymentType, Double totalDiscount, String category, String menu, String promoName) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
