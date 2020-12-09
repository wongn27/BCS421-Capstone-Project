package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewPetSitterActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;

    ImageView messageIcon, profilePic;
    TextView fullName, myBio, askingPrice;
    RatingBar ratingBar;
    Button requestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pet_sitter);

        messageIcon = findViewById(R.id.messageIcon);
        profilePic = findViewById(R.id.profilePic);
        fullName = findViewById(R.id.fullName);
        myBio = findViewById(R.id.myBio);
        askingPrice = findViewById(R.id.askingPrice);
        ratingBar = findViewById(R.id.ratingBar);
        requestBtn = findViewById(R.id.requestBtn);

        Intent intent = getIntent();
        String petSitterUserId = intent.getStringExtra(HomeActivity.EXTRA_PETSITTERID);

        docRef = db.collection("users").document(petSitterUserId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                fullName.setText(document.getString("fName") +  " " + document.getString("lName"));

            }
        });


        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPetSitterActivity.this, RequestActivity.class);
                startActivity(intent);
            }
        });
    }
}