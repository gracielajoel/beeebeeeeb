package com.example.projectcafe.classes;

public class Menu {
    private String menuName;
    private Double smallPrice;
    private Double mediumPrice;
    private Double largePrice;
    private String category;

    public Menu(String menuName, Double smallPrice, Double mediumPrice, Double largePrice, String category) {
        this.menuName = menuName;
        this.smallPrice = smallPrice;
        this.mediumPrice = mediumPrice;
        this.largePrice = largePrice;
        this.category = category;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Double getSmallPrice() {
        return smallPrice;
    }

    public void setSmallPrice(double smallPrice) {
        this.smallPrice = smallPrice;
    }

    public Double getMediumPrice() {
        return mediumPrice;
    }

    public void setMediumPrice(Double mediumPrice) {
        this.mediumPrice = mediumPrice;
    }

    public Double getLargePrice() {
        return largePrice;
    }

    public void setLargePrice(Double largePrice) {
        this.largePrice = largePrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
