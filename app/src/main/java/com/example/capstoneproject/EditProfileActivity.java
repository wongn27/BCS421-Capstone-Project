package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    //member variables
    private String userID;
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText emailET;
    private EditText addressET, phoneET;
    private ImageView profilePic;
    private Uri imageUri;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private DocumentReference userRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;




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
                            firstNameET.setText(documentSnapshot.getString("fName"));
                            lastNameET.setText(documentSnapshot.getString("lName"));
                            emailET.setText(documentSnapshot.getString("email"));
                            addressET.setText(documentSnapshot.getString("address"));
                            phoneET.setText(documentSnapshot.getString("phone"));
                            if (storage.getInstance().getReference().child("profilepics/" + userID) != null) {
                                imageReference = storage.getInstance().getReference().child("profilepics/" + userID);
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

        //when you touch the picture select it
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });


        //hiiiiiii
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
        if (isValidEmail(email)) {
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

