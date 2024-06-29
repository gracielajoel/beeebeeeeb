package com.example.projectcafe.classes;

public class Periode {
    private String month;
    private String year;

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

    public Periode(String month, String year) {
        this.month = month;
        this.year = year;
    }
}
