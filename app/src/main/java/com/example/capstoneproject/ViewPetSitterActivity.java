package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.lang.String.format;

public class ViewPetSitterActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference petSitterRef;
    public static final String EXTRA_PETSITTERID2 = "EXTRA_PETSITTERID2";

    ImageView messageIcon, profilePic;
    TextView fullName, myBio, askingPrice, milesAway;
    RatingBar ratingBar;
    Button requestBtn;
    String petSitterUserId;

    private DocumentReference userRef;
    //double milesAway;


    Location location1 = new Location("location1");
    Location location2 = new Location("location2");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pet_sitter);

        messageIcon = findViewById(R.id.messageIcon);
        profilePic = findViewById(R.id.profilePic);
        fullName = findViewById(R.id.fullName);
        milesAway = findViewById(R.id.milesAway);
        myBio = findViewById(R.id.myBio);
        askingPrice = findViewById(R.id.askingPrice);
        ratingBar = findViewById(R.id.ratingBar);
        requestBtn = findViewById(R.id.requestBtn);

        Intent intent = getIntent();
        petSitterUserId = intent.getStringExtra(HomeActivity.EXTRA_PETSITTERID);

        userRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    /*//latX = documentSnapshot.getDouble("latitude");
                    latX = 43.0;
                    longX = documentSnapshot.getDouble("longitude");*/

                    location1.setLatitude(documentSnapshot.getDouble("latitude"));
                    location1.setLongitude(documentSnapshot.getDouble("latitude"));
                } else {
                    Log.d("GeoPoint", "NotSet");
                }
            }
        });







        petSitterRef = db.collection("users").document(petSitterUserId);
        petSitterRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                location2.setLatitude(document.getDouble("latitude"));
                location2.setLongitude(document.getDouble("latitude"));
                fullName.setText(document.getString("fName") +  " " + document.getString("lName"));
                myBio.setText(document.getString("bio"));
                askingPrice.setText("$"+String.valueOf(document.getDouble("askingPrice")));
                milesAway.setText(format("%.0f",(location1.distanceTo(location2)*0.000621371)) + " mi");

            }
        });


        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPetSitterActivity.this, RequestActivity.class);
                intent.putExtra(EXTRA_PETSITTERID2, petSitterUserId);
                startActivity(intent);
            }
        });
    }
}