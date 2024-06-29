package com.example.projectcafe.classes;

public class CashierSales {
    private String cashierName;

    private Integer countSales;

    public CashierSales(String cashierName, Integer countSales) {
        this.cashierName = cashierName;
        this.countSales = countSales;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public Integer getCountSales() {
        return countSales;
    }

    public void setCountSales(Integer countSales) {
        this.countSales = countSales;
    }
}
