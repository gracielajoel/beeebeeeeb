package com.example.projectcafe.classes;

public class Order {
    private String menuName;
    private String iceLevel;
    private String sugarLevel;
    private Integer quantity;
    private String sizeChosen;

    private Double price;
    private String promoUsed;
    private Double afterPromoPrice;


    private Double tempTotal;

    public Double getTempTotal() {
        return tempTotal;
    }

    public void setTempTotal(Double tempTotal) {
        this.tempTotal = tempTotal;
    }

    public Order(String menuName, String iceLevel, String sugarLevel, Integer quantity, String sizeChosen, Double price, String promoUsed, Double afterPromoPrice, Double tempTotal) {
        this.menuName = menuName;
        this.iceLevel = iceLevel;
        this.sugarLevel = sugarLevel;
        this.quantity = quantity;
        this.sizeChosen = sizeChosen;
        this.price = price;
        this.promoUsed = promoUsed;
        this.afterPromoPrice = afterPromoPrice;
        this.tempTotal = tempTotal;
    }

    public String getIceLevel() {
        return iceLevel;
    }

    public void setIceLevel(String iceLevel) {
        this.iceLevel = iceLevel;
    }

    public String getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(String sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSizeChosen() {
        return sizeChosen;
    }

    public void setSizeChosen(String sizeChosen) {
        this.sizeChosen = sizeChosen;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPromoUsed() {
        return promoUsed;
    }

    public void setPromoUsed(String promoUsed) {
        this.promoUsed = promoUsed;
    }

    public Double getAfterPromoPrice() {
        return afterPromoPrice;
    }

    public void setAfterPromoPrice(Double afterPromoPrice) {
        this.afterPromoPrice = afterPromoPrice;
    }


}
