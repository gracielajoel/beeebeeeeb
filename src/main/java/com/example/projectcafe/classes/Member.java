package com.example.projectcafe.classes;

public class Member {
    private String memberName;
    private Integer memberPoint;
    private String birthDate;
    private String email;
    private String address;

    public Member(String memberName, Integer memberPoint, String birthDate, String email, String address) {
        this.memberName = memberName;
        this.memberPoint = memberPoint;
        this.birthDate = birthDate;
        this.email = email;
        this.address = address;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getMemberPoint() {
        return memberPoint;
    }

    public void setMemberPoint(Integer memberPoint) {
        this.memberPoint = memberPoint;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
