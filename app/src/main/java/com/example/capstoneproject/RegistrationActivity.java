package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    EditText firstName, lastName, email, password;
    Button btnRegister;

    //Firebase db
    FirebaseAuth mFirebaseAuth;
    //Firestore db
    FirebaseFirestore fStore;
    //Firestore user id
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Get Firebase Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Get Firestore Instance
        fStore = FirebaseFirestore.getInstance();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstX = firstName.getText().toString();
                final String lastX = lastName.getText().toString();
                final String emailX = email.getText().toString();
                String pwdX = password.getText().toString();
                int flags = 0;

                if(firstX.length() < 3 || firstX.length() > 30){
                    firstName.setError("Must be between 3-30 characters");
                    flags++;
                }

                if(lastX.length() < 3 || lastX.length() > 30){
                    lastName.setError("Must be between 3-30 characters");
                    flags++;
                }

                if(!isValidEmail(emailX)){
                    email.setError("Not a valid email address");
                    flags++;
                }

                if(pwdX.length() < 6){
                    password.setError("Must be at least 6 characters");
                    flags++;
                }

                if(firstX.isEmpty()){
                    firstName.setError("Please enter first name");
                    flags++;
                }
                if (lastX.isEmpty()){
                    lastName.setError("Please enter last name");
                    flags++;
                }
                if (emailX.isEmpty()){
                    email.setError("Please enter an email");
                    flags++;
                }
                if (pwdX.isEmpty()){
                    password.setError("Please enter a password");
                    flags++;
                }

                if (flags == 0){
                    //Register user in Firebase
                    mFirebaseAuth.createUserWithEmailAndPassword(emailX, pwdX).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this, "Registration unsuccessful, please try again.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "User Created!", Toast.LENGTH_SHORT).show();

                                //Add user information to Firestore
                                userID = mFirebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("fName", firstX);
                                user.put("lName", lastX);
                                user.put("email", emailX);
                                documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "onFailure" + e.toString());
                                    }
                                });
                                // Return to Login after Successfully being registered
                                finish();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isValidEmail(String e) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return e.matches(regex);
    }
}