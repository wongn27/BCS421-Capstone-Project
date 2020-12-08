package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

//    private TextView textViewName;
//    private TextView textViewSex;
//    private TextView textViewType;
//    private TextView textViewAge;
//    private TextView textViewWeight;
//    private TextView textViewSpecialCareNeeds;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//  private CollectionReference userPetsRef = db.collection("pets");
    private CollectionReference userPetsRef;

    private PetAdapter adapter;

    private DocumentReference petRef;
    private FirebaseAuth firebaseAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet_profile);

        setUpRecyclerView();

//        textViewName = findViewById(R.id.textViewPetName);
//        textViewSex = findViewById(R.id.textViewSex);
//        textViewType = findViewById(R.id.textViewType);
//        textViewAge = findViewById(R.id.textViewAge);
//        textViewWeight = findViewById(R.id.textViewWeight);
//        textViewSpecialCareNeeds = findViewById(R.id.textViewSpecialCareNeeds);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        userID = firebaseAuth.getCurrentUser().getUid();
//
//        petRef = db.collection("users").document(userID).collection("pets").document("Oreo");
//
//        petRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            textViewName.setText(documentSnapshot.getString("name"));
//                            textViewSex.setText(documentSnapshot.getString("sex"));
//                            textViewType.setText(documentSnapshot.getString("type"));
//                            textViewAge.setText(documentSnapshot.getString("age"));
//                            textViewWeight.setText(documentSnapshot.getString("weight"));
//                            textViewSpecialCareNeeds.setText(documentSnapshot.getString("specialCareNeeds"));
//                        }
//                        else {
//                            Toast.makeText(EditPetProfileActivity.this, "Error loading document.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(EditPetProfileActivity.this, "Error loading document.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//



        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        userPetsRef = db.collection("users").document(userID).collection("pets");

        FloatingActionButton floatingActionButton = findViewById(R.id.button_floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to add a pet
                Intent intent = new Intent(EditPetProfileActivity.this, AddPetProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = userPetsRef.orderBy("name", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Pet> options = new FirestoreRecyclerOptions.Builder<Pet>()
                .setQuery(query, Pet.class)
                .build();

        adapter = new PetAdapter(options);

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