package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class GiveFeedbackActivity extends AppCompatActivity {

    //variables
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private DocumentReference userRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    private String userID, sitterID, review;
    private float rating;
    private DocumentReference reviewRef, sitterRef;
    RatingBar ratingBar;
    Button submitButton;
    EditText reviewTextBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);

        //set views
        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submitButton);
        reviewTextBox = findViewById(R.id.feedbackEditText);

        rating = 0;

        //prepare firestore instance
        fAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userID = fAuth.getCurrentUser().getUid();
        sitterID = "hi";

        fStore  = FirebaseFirestore.getInstance();
        reviewRef = fStore.collection("users").document(sitterID).collection("reviews").document(userID);

        //submit the review when user clicks
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = String.valueOf(ratingBar.getRating());
                rating = Float.valueOf(s).floatValue();

                review = reviewTextBox.getText().toString();

                //fill up review reference
                Map<String, Object> newReview = new HashMap<>();

                newReview.put("review", review);
                newReview.put("rating", rating);
                newReview.put("userID", userID);
                newReview.put("sitterID", sitterID);

                //check to make sure values are valid
                if (review != null && rating != 0) {
                    reviewRef.set(newReview)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(GiveFeedbackActivity.this, "Review updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GiveFeedbackActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}