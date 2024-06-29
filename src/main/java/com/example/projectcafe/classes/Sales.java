package com.example.projectcafe.classes;

public class Sales {
    private String menuName;

    private int totalQuantity;

    private double totalPrice;

    public Sales(String menuName, int totalQuantity, double totalPrice) {
        this.menuName = menuName;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
