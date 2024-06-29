package com.example.projectcafe.classes;

public class SalesPeriod {
    private String month;
    private String year;

    private Double totalSales;

    public SalesPeriod(String month, String year, Double totalSales) {
        this.month = month;
        this.year = year;
        this.totalSales = totalSales;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }
}
