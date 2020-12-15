package com.example.capstoneproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class PetSitter implements Parcelable {
    private String fName, lName, email, imageUrl, petSitterId;
    double longitude, latitude;
    private boolean isSitter;
    private GeoPoint geoPoint;

    public PetSitter() {
    }

    public PetSitter(String fName, String lName, String email, String imageUrl, double longitude, double latitude, boolean isSitter) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isSitter = isSitter;
        geoPoint = new GeoPoint(latitude, longitude);
    }


    protected PetSitter(Parcel in) {
        fName = in.readString();
        lName = in.readString();
        email = in.readString();
        imageUrl = in.readString();
        petSitterId = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        isSitter = in.readByte() != 0;
        geoPoint = new GeoPoint(latitude, longitude);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fName);
        dest.writeString(lName);
        dest.writeString(email);
        dest.writeString(imageUrl);
        dest.writeString(petSitterId);
        dest.writeDouble(geoPoint.getLongitude());
        dest.writeDouble(geoPoint.getLatitude());
        dest.writeByte((byte) (isSitter ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isSitter() {
        return isSitter;
    }

    public void setSitter(boolean sitter) {
        isSitter = sitter;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
