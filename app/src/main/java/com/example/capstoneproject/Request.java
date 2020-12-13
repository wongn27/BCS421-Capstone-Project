package com.example.capstoneproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Request {
    private String startDate, endDate, totalPay, userThatRequested, specialCareNeeds, petSex, statusOfRequest, nameOfPet, firstNameOfUserThatRequested, lastNameOfUserThatRequested;

    DocumentReference userRef;
    String fullName, fName, lName;

    public Request() {
    }

    public Request(String startDate, String endDate, String totalPay, String userThatRequested, String specialCareNeeds, String petSex, String statusOfRequest, String nameOfPet, String firstNameOfUserThatRequested, String lastNameOfUserThatRequested) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPay = totalPay;
        this.userThatRequested = userThatRequested;
        this.specialCareNeeds = specialCareNeeds;
        this.petSex = petSex;
        this.statusOfRequest = statusOfRequest;
        this.nameOfPet = nameOfPet;
        this.firstNameOfUserThatRequested = firstNameOfUserThatRequested;
        this.lastNameOfUserThatRequested = lastNameOfUserThatRequested;
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

    public String getUserThatRequested() {
        return userThatRequested;
    }

    public void setUserThatRequested(String userThatRequested) {
        this.userThatRequested = userThatRequested;
    }

    public String getSpecialCareNeeds() {
        return specialCareNeeds;
    }

    public void setSpecialCareNeeds(String specialCareNeeds) {
        this.specialCareNeeds = specialCareNeeds;
    }

    public String getPetSex() {
        return petSex;
    }

    public void setPetSex(String petSex) {
        this.petSex = petSex;
    }

    public String getStatusOfRequest() {
        return statusOfRequest;
    }

    public void setStatusOfRequest(String statusOfRequest) {
        this.statusOfRequest = statusOfRequest;
    }

    public String getNameOfPet() {
        return nameOfPet;
    }

    public void setNameOfPet(String nameOfPet) {
        this.nameOfPet = nameOfPet;
    }

    public String getFirstNameOfUserThatRequested() {
        return firstNameOfUserThatRequested;
    }

    public void setFirstNameOfUserThatRequested(String firstNameOfUserThatRequested) {
        this.firstNameOfUserThatRequested = firstNameOfUserThatRequested;
    }

    public String getLastNameOfUserThatRequested() {
        return lastNameOfUserThatRequested;
    }

    public void setLastNameOfUserThatRequested(String lastNameOfUserThatRequested) {
        this.lastNameOfUserThatRequested = lastNameOfUserThatRequested;
    }



    public String getFullName() {
        Log.d("getFullName", "getFullName");

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        userRef = fStore.collection("users").document(userThatRequested);

        readData(new FirestoreCallback(){
            @Override
            public String onCallback(String str) {
                fullName = str;
                Log.d("smh1", fullName+"wth1");
                return fullName;
            }
        });

        Log.d("smh2", fullName+"wth2");
        return fullName;
    }

    private void readData(final FirestoreCallback firestoreCallback) {
        Log.d("READDATA", "Start of read data");
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                   fName = task.getResult().getString("fName");
                   lName = task.getResult().getString("lName");

                   firestoreCallback.onCallback(fName + " " + lName);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });


        /*userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    fName = documentSnapshot.getString("fName");
                    lName = documentSnapshot.getString("lName");

                    Log.d("CHECKFULLNAME1", fName + " " + lName);
                    firestoreCallback.onCallback(fName + " " + lName);

                }else {
                    Log.d(TAG, "Error getting documents: ");
                }
            }

        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d("CHECKFULLNAME2", fName + " " + lName);
            }
        });*/

    }

    private interface FirestoreCallback {
        String onCallback(String str);
    }
}

