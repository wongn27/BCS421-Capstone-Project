package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    //member variables
    private String userID;
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText emailET;
    private EditText addressET;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private DocumentReference userRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //set views
        firstNameET = findViewById(R.id.firstNameEditText);
        lastNameET = findViewById(R.id.lastNameEditText);
        emailET = findViewById(R.id.emailEditText);
        addressET = findViewById(R.id.addressEditText);


        //prepare firestore instance
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        fStore  = FirebaseFirestore.getInstance();
        userRef = fStore.collection("users").document(userID);

        //set fields to appropriate values
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            firstNameET.setText(documentSnapshot.getString("fName"));
                            lastNameET.setText(documentSnapshot.getString("lName"));
                            emailET.setText(documentSnapshot.getString("email"));
                            addressET.setText(documentSnapshot.getString("address"));
                        }
                        else {
                            Toast.makeText(EditProfileActivity.this, "Error loading document.", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Error loading document.", Toast.LENGTH_SHORT).show();
                    }
                });
        

        //hiiiiiii
    }

    //function to update the users profile
    public void updateUser(View v) {


        //field variables
        String firstName;
        String lastName;
        String email;
        String address;

        //get the current values
        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        email = emailET.getText().toString();
        address = addressET.getText().toString();

        Map<String, Object> user = new HashMap<>();

        user.put("fName", firstName);
        user.put("lName", lastName);
        user.put("email", email);
        user.put("address", address);

        userRef = fStore.collection("users").document(userID);

        //check to make sure values are valid
        if (isValidEmail(email) && isValidName(firstName) && isValidName(lastName)) {
            userRef.set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditProfileActivity.this, "User updated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        else {
            Toast.makeText(EditProfileActivity.this, "Info validation error. Check all fields", Toast.LENGTH_SHORT).show();
        }

    }

    //check email
    public boolean isValidEmail(String e) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return e.matches(regex);
    }

    //check name
    public boolean isValidName(String n) {
        String regexName = "\\p{Upper}(\\p{Lower}+\\s?)";
        String patternName = "(" + regexName + "){1,2}";
        return n.matches(patternName);
    }

    //check address
    public boolean isValidAddress(String a) {
        String regex = "[A-Za-z0-9'\\.\\-\\s\\,]";
        return a.matches(regex);
    }


}

