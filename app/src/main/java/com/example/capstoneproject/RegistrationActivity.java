package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {
    EditText firstName, lastName, email, password, address, phone, confirmPassword;
    private ImageView profilePic;
    Button btnRegister;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    String imageUrl;

    //Firebase db
    FirebaseAuth mFirebaseAuth;
    //Firestore db
    FirebaseFirestore fStore;
    //Firestore user id
    String userID;

    private Geocoder geocoder;
    GeoPoint geoPoint;

    double longX = 0;
    double latX = 0;

    Map<String, Object> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Get Firebase Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Get Firestore Instance
        fStore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        geocoder = new Geocoder(this);



        imageUri = null;
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        profilePic = findViewById(R.id.profilePic);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstX = firstName.getText().toString();
                final String lastX = lastName.getText().toString();
                final String emailX = email.getText().toString();
                final String phoneX = phone.getText().toString();
                final String addressX = address.getText().toString();
                final String pwdX = password.getText().toString();
                final String confX = confirmPassword.getText().toString();
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

                if(!isValidName(firstX)){
                    firstName.setError("Not a valid first name");
                    flags++;
                }

                if(isValidAddress(addressX)) {
                    address.setError("Not a valid address");
                }

                if(!isValidName(lastX)){
                    lastName.setError("Not a valid first name");
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

                if (addressX.isEmpty()){
                    address.setError("Please enter an address");
                    flags++;
                }

                if (phoneX.isEmpty()){
                    phone.setError("Please enter a phone number");
                    flags++;
                }

                if (!confX.equals(pwdX)) {
                    confirmPassword.setError("Passwords do not match.");
                    flags++;
                }

                if (imageUri == null) {
                    Toast.makeText(RegistrationActivity.this, "Please choose a profile picture.", Toast.LENGTH_SHORT).show();
                    flags++;
                }


                if (flags == 0){
                    //turn given address into lat/ long
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(addressX, 1);
                        Address address = addresses.get(0);
                        longX = address.getLongitude();
                        latX = address.getLatitude();
                        geoPoint = new GeoPoint(latX, longX);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


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
                                //Map<String, Object> user = new HashMap<>();
                                user.put("fName", firstX);
                                user.put("lName", lastX);
                                user.put("email", emailX);
                                user.put("address", addressX);
                                user.put("phone", phoneX);
                                user.put("bio", "");
                                user.put("askingPrice", 0);
                                user.put("sitter", false);
                                user.put("geoPoint", geoPoint);
                                user.put("longitude", longX);
                                user.put("latitude", latX);




                                final ProgressDialog pd = new ProgressDialog(RegistrationActivity.this);
                                pd.setTitle("Uploading image...");
                                pd.show();

                                imageReference = storageReference.child("profilepics/" + userID);
                                //user.put("imageUri", imageReference.getDownloadUrl());

                                imageReference.putFile(imageUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                pd.dismiss();
                                                Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();


                                                //imageUrl = imageReference.getDownloadUrl().toString();

                                                //Log.d("TestURL", imageReference.toString());
                                                //Toast.makeText(RegistrationActivity.this, imageReference.getDownloadUrl().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(), "Failed to upload.", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                                double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                pd.setMessage("Progress: " + (int) progressPercent + "&");

                                            }
                                        });



                                documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "onFailure" + e.toString());
                                    }
                                });
                                // Return to Login after Successfully being registered
                                /*Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                startActivity(intent);*/
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

    //method to select image from gallery
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            //uploadPicture();
        }
    }

    //upload the picture
/*    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        imageReference = storageReference.child("temp/" + randomKey);

        imageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to upload.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "&");

                    }
                });
    }*/
}