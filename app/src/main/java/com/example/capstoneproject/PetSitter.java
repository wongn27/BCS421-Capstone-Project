package com.example.capstoneproject;

public class PetSitter {
    private String fName, lName, email, imageUrl;

    public PetSitter() {
    }

    public PetSitter(String fName, String lName, String email, String imageUrl) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.imageUrl = imageUrl;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
