package com.example.projectcafe.classes;

public class Cashier {
    private int cashierId;
    private String cashierName;
    private Double salary;
    private Double commission;
    private String password = "456";

    public String getPassword() {
        return password;
    }

    public Cashier(String cashierName, Double salary, Double commission) {
        this.cashierName = cashierName;
        this.salary = salary;
        this.commission = commission;
    }


    public Cashier() {
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommission() {
        return commission;
    }



    public int getCashierId() {
        return cashierId;
    }

    public void setCashierId(int cashierId) {
        this.cashierId = cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }
}
