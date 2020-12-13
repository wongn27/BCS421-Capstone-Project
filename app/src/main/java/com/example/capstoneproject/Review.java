package com.example.capstoneproject;

public class Review {
    private String userThatAcceptedId, fName, lName;

    public Review(String userThatAcceptedId, String fName, String lName) {
        this.userThatAcceptedId = userThatAcceptedId;
        this.fName = fName;
        this.lName = lName;
    }

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

}
