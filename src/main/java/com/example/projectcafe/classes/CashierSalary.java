package com.example.projectcafe.classes;

public class CashierSalary {
    private String cashierName;
    private Integer countSalaryHistory;

    public CashierSalary(String cashierName, Integer countSalaryHistory) {
        this.cashierName = cashierName;
        this.countSalaryHistory = countSalaryHistory;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public Integer getCountSalaryHistory() {
        return countSalaryHistory;
    }

    public void setCountSalaryHistory(Integer countSalaryHistory) {
        this.countSalaryHistory = countSalaryHistory;
    }
}
