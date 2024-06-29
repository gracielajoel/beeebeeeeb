package com.example.projectcafe.classes;

public class SalaryHistory {
    private String cashierName;

    private Double lastSalary;

    private String lastDate;

    public SalaryHistory(String cashierName, Double lastSalary, String lastDate) {
        this.cashierName = cashierName;
        this.lastSalary = lastSalary;
        this.lastDate = lastDate;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public Double getLastSalary() {
        return lastSalary;
    }

    public void setLastSalary(Double lastSalary) {
        this.lastSalary = lastSalary;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
}
