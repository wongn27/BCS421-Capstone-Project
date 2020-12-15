package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    //member variables
    private String userID;
    private boolean sitter;
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText emailET;
    private EditText addressET, phoneET, bioET, askingPriceET;
    private TextView askingPriceTV;
    private ImageView profilePic;
    private Button updateButton;
    private Switch sitterSwitch;
    private Uri imageUri;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private DocumentReference userRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;

    private Geocoder geocoder;
    GeoPoint geoPoint;
    double longX = 0;
    double latX = 0;

    //field variables
    String firstName, lastName, email, address, bio, phone;
    Long askingPrice;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //set views
        firstNameET = findViewById(R.id.firstNameEditText);
        lastNameET = findViewById(R.id.lastNameEditText);
        emailET = findViewById(R.id.emailEditText);
        addressET = findViewById(R.id.addressEditText);
        profilePic = findViewById(R.id.profilePicImageView);
        phoneET = findViewById(R.id.phoneEditText);
        bioET = findViewById(R.id.bioEditText);
        updateButton = findViewById(R.id.updateButton);
        sitterSwitch = findViewById(R.id.sitterSwitch);
        askingPriceET = findViewById(R.id.askingPriceEditText);
        askingPriceTV = findViewById(R.id.askingPriceTextView);


        //prepare firestore instance
        fAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userID = fAuth.getCurrentUser().getUid();

        fStore  = FirebaseFirestore.getInstance();
        userRef = fStore.collection("users").document(userID);

        //set fields to appropriate values
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            firstName = documentSnapshot.getString("fName");
                            firstNameET.setText(firstName);
                            lastName = documentSnapshot.getString("lName");
                            lastNameET.setText(lastName);
                            email = documentSnapshot.getString("email");
                            emailET.setText(email);
                            address = documentSnapshot.getString("address");
                            addressET.setText(address);
                            phone = documentSnapshot.getString("phone");
                            phoneET.setText(phone);
                            bio = documentSnapshot.getString("bio");
                            bioET.setText(bio);
                            askingPrice = documentSnapshot.getLong("askingPrice");
                            sitter = documentSnapshot.getBoolean("sitter");
                            askingPriceET.setText(askingPrice.toString());

                            if (sitter) {
                                sitterSwitch.setChecked(true);
                            }
                            else {
                                askingPriceET.setVisibility(View.GONE);
                                askingPriceTV.setVisibility(View.GONE);
                            }
                            if (storage.getInstance().getReference().child("profilepics/" + userID) != null) {
                                imageReference = storage.getInstance().getReference().child("profilepics/" + userID);
                                Toast.makeText(EditProfileActivity.this, imageReference.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("IMAGEREf", imageReference.toString());
                            }

                            try {
                                final File localFile = File.createTempFile("profpic", "jpg");
                                imageReference.getFile(localFile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                profilePic.setImageBitmap(bitmap);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfileActivity.this, "Image download unsuccessful.", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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



        sitterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sitterSwitch.isChecked()) {
                    askingPriceET.setVisibility(View.VISIBLE);
                    askingPriceTV.setVisibility(View.VISIBLE);
                    sitter = true;
                }
                else {
                    sitter = false;
                    askingPriceET.setVisibility(View.GONE);
                    askingPriceTV.setVisibility(View.GONE);
                }
            }
        });


        //when you touch the picture select it
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

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
            uploadPicture();
        }
    }

    //upload the picture
    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("profilepics/" + userID);

        riversRef.putFile(imageUri)
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
    }

    //function to update the users profile
    public void updateUser() {


        //get the current values
        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        email = emailET.getText().toString();
        address = addressET.getText().toString();
        phone = phoneET.getText().toString();
        bio = bioET.getText().toString();
        askingPrice = Long.valueOf(askingPriceET.getText().toString());


        geocoder = new Geocoder(this);

        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            Address theAddress = addresses.get(0);
            longX = theAddress.getLongitude();
            latX = theAddress.getLatitude();
            geoPoint = new GeoPoint(latX, longX);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //put info in user object
        Map<String, Object> user = new HashMap<>();

        user.put("fName", firstName);
        user.put("lName", lastName);
        user.put("email", email);
        user.put("address", address);
        user.put("phone", phone);
        user.put("bio", bio);
        user.put("sitter", sitter);

        user.put("geoPoint", geoPoint);
        user.put("askingPrice", askingPrice);
        user.put("longitude", longX);
        user.put("latitude", latX);


        userRef = fStore.collection("users").document(userID);

        //check to make sure values are valid and set user object to user reference
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


}

