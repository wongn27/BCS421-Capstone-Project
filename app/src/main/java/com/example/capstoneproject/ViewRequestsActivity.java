package com.example.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.List;

public class ViewRequestsActivity extends AppCompatActivity {

    public static String EXTRA_USERID = "EXTRA_USERID";
    public static String EXTRA_PETNAME = "EXTRA_PETNAME";
    public static String EXTRA_PETTYPE = "EXTRA_PETTYPE";
    public static String EXTRA_PETINFO = "EXTRA_PETINFO";
    public static String EXTRA_PETSEX = "EXTRA_PETSEX";
    public static String EXTRA_STARTDATE = "EXTRA_STARTDATE";
    public static String EXTRA_ENDDATE = "EXTRA_ENDDATE";
    public static String EXTRA_TOTALPAY = "EXTRA_TOTALPAY";
    public static String EXTRA_STATUS = "EXTRA_STATUS";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference requestsRef = db.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("requests");

    private RequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        setUpRecyclerView();
    }


    private void setUpRecyclerView() {
        Query query = requestsRef.orderBy("startDate", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Request> options = new FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        adapter = new RequestAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();

                String petName = documentSnapshot.getString("nameOfPet");
                String petInfo = documentSnapshot.getString("petInfo");
                String startDate = documentSnapshot.getString("startDate");
                String endDate = documentSnapshot.getString("endDate");
                String totalPay = documentSnapshot.getString("totalPay");
                String petSex = documentSnapshot.getString("petSex");
                String petType = documentSnapshot.getString("petType");
                String statusOfRequest = documentSnapshot.getString("statusOfRequest");
                String userThatRequested = documentSnapshot.getString("userThatRequested");

                Intent intent = new Intent(ViewRequestsActivity.this, ExpandRequestActivity.class);
                //intent.putExtra(EXTRA_USERID, id);
                intent.putExtra(EXTRA_STARTDATE, startDate);
                intent.putExtra(EXTRA_ENDDATE, endDate);
                intent.putExtra(EXTRA_TOTALPAY, totalPay);
                intent.putExtra(EXTRA_PETNAME, petName);
                intent.putExtra(EXTRA_PETINFO, petInfo);
                intent.putExtra(EXTRA_PETSEX, petSex);
                intent.putExtra(EXTRA_PETTYPE, petType);
                intent.putExtra(EXTRA_STATUS, statusOfRequest);
                intent.putExtra(EXTRA_USERID, userThatRequested);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}

