package com.example.capstoneproject;

import android.os.Parcel;
import android.os.Parcelable;

public class PetSitter implements Parcelable {
    private String fName, lName, email, imageUrl, petSitterId;
    private boolean isSitter;

    public PetSitter() {
    }

    public PetSitter(String fName, String lName, String email, String imageUrl, boolean isSitter) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.isSitter = isSitter;
        this.imageUrl = imageUrl;

    }

    protected PetSitter(Parcel in) {
        fName = in.readString();
        lName = in.readString();
        email = in.readString();
        isSitter = in.readByte() != 0;
        imageUrl = in.readString();
        petSitterId = in.readString();
    }

    public static final Creator<PetSitter> CREATOR = new Creator<PetSitter>() {
        @Override
        public PetSitter createFromParcel(Parcel in) {
            return new PetSitter(in);
        }

        @Override
        public PetSitter[] newArray(int size) {
            return new PetSitter[size];
        }
    };

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

    public String getPetSitterId() {
        return petSitterId;
    }

    public void setPetSitterId(String petSitterId) {
        this.petSitterId = petSitterId;
    }

    public boolean isSitter() {
        return isSitter;
    }

    public void setSitter(boolean sitter) {
        isSitter = sitter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fName);
        dest.writeString(lName);
        dest.writeString(email);
        dest.writeByte((byte) (isSitter ? 1 : 0));
        dest.writeString(imageUrl);
        dest.writeString(petSitterId);
    }
}
