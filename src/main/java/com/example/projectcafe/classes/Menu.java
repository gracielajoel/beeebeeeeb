package com.example.projectcafe.classes;

public class Menu {
    private String menuName;
    private Double smallPrice;
    private Double mediumPrice;
    private Double largePrice;
    private Integer category;

    public Menu(String menuName, Double smallPrice, Double mediumPrice, Double largePrice, Integer category) {
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

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
