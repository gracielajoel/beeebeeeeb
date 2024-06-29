package com.example.projectcafe.classes;

public class ProductSold {
    private String categoryName;
    private String menuName;
    private Integer menuSum;

    public ProductSold(String categoryName, String menuName, Integer menuSum) {
        this.categoryName = categoryName;
        this.menuName = menuName;
        this.menuSum = menuSum;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getMenuSum() {
        return menuSum;
    }

    public void setMenuSum(Integer menuSum) {
        this.menuSum = menuSum;
    }
}
