package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExpandRequestActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference petRef;

    private DocumentReference acceptedRequestsRef;
    private DocumentReference currentUserRef;

    TextView petName, startDate, endDate, totalPay, petType, petSex, petInfo;
    Button accept, deny;

    String fName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_request);

        
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        totalPay = findViewById(R.id.totalPay);
        petName = findViewById(R.id.petName);
        petType = findViewById(R.id.type);
        petSex = findViewById(R.id.sex);
        petInfo = findViewById(R.id.petInfo);
        accept = findViewById(R.id.accept);
        deny = findViewById(R.id.deny);

        Intent intent = getIntent();
        final String userThatRequested = intent.getStringExtra(ViewRequestsActivity.EXTRA_USERID);
        final String petNameStr = intent.getStringExtra(ViewRequestsActivity.EXTRA_PETNAME);
        final String startDateStr = intent.getStringExtra(ViewRequestsActivity.EXTRA_STARTDATE);
        final String endDateStr = intent.getStringExtra(ViewRequestsActivity.EXTRA_ENDDATE);
        final String totalPayStr = intent.getStringExtra(ViewRequestsActivity.EXTRA_TOTALPAY);

        currentUserRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        currentUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fName = documentSnapshot.getString("fName");
                lName = documentSnapshot.getString("lName");
            }
        });

        petRef = db.collection("users").document(userThatRequested).collection("pets").document(petNameStr);
        petRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                petType.setText(getResources().getString(R.string.type) +": "+document.getString("type"));
                petSex.setText(getResources().getString(R.string.sex) +": " +document.getString("sex"));
                petName.setText(getResources().getString(R.string.take_care_of)+" " +document.getString("name"));
                petInfo.setText("\n"+document.getString("specialCareNeeds"));
                startDate.setText(startDateStr);
                endDate.setText(endDateStr);
                totalPay.setText(totalPayStr);
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
                acceptedRequestsRef = db.collection("users").document(userThatRequested).collection("acceptedRequests")
                        .document(currentDate+" "+System.currentTimeMillis());

                acceptedRequestsRef = db.collection("users").document(userThatRequested).collection("acceptedRequests")
                        .document(currentDate+" "+System.currentTimeMillis());

                Map<String, Object> acceptedRequests = new HashMap<>();

                acceptedRequests.put("userThatAcceptedId", FirebaseAuth.getInstance().getUid());
                acceptedRequests.put("fName", fName);
                acceptedRequests.put("lName", lName);
                acceptedRequests.put("startDate", startDateStr);
                acceptedRequests.put("endDate", endDateStr);
                acceptedRequests.put("totalPay", totalPayStr);
                acceptedRequests.put("petName", petNameStr);

                acceptedRequestsRef.set(acceptedRequests).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure" + e.toString());
                    }
                });

                //acceptedRequests.put("userThatAcceptedId", curren);

                Intent intent = new Intent(ExpandRequestActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpandRequestActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}