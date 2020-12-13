package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EditPetProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference userPetsRef;

    private PetAdapter adapter;

    private DocumentReference petRef;
    private FirebaseAuth firebaseAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet_profile);

        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        Log.v("Error1", userID);

        if (db != null)
        //userPetsRef = db.collection("users").document(userID).collection("pets");
            userPetsRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("pets");

        FloatingActionButton floatingActionButton = findViewById(R.id.button_floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to add a pet
                Intent intent = new Intent(EditPetProfileActivity.this, AddPetProfileActivity.class);
                startActivity(intent);
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        //if (userPetsRef != null) {
            Query query = userPetsRef.orderBy("name");//.orderBy("name", Query.Direction.ASCENDING);

            FirestoreRecyclerOptions<Pet> options = new FirestoreRecyclerOptions.Builder<Pet>()
                    .setQuery(query, Pet.class)
                    .build();

            adapter = new PetAdapter(options);
        //}
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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