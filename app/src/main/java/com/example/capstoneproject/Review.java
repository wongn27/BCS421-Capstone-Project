package com.example.capstoneproject;

public class Review {
    private String userThatAcceptedId, fName, lName, startDate, endDate, totalPay, petName;

    public Review(String userThatAcceptedId, String fName, String lName, String startDate, String endDate, String totalPay, String petName) {
        this.userThatAcceptedId = userThatAcceptedId;
        this.fName = fName;
        this.lName = lName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPay = totalPay;
        this.petName = petName;
    }

    public Review() {}

    public String getUserThatAcceptedId() {
        return userThatAcceptedId;
    }

    public void setUserThatAcceptedId(String userThatAcceptedId) {
        this.userThatAcceptedId = userThatAcceptedId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(String totalPay) {
        this.totalPay = totalPay;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }
}
