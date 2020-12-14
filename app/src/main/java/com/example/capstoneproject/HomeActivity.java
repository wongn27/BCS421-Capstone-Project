package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PetSitterLoadListener, PetSitterAdapter.OnPetSitterListener {

    private static final String TESTLOG = "TESTLOG";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef;

    PetSitterLoadListener petSitterLoadListener;
    String sitterId;

    //private Geocoder geocoder;

    RecyclerView recyclerView;

    Context context = this;

    List<PetSitter> list = new ArrayList<>();

    public static final String EXTRA_PETSITTERID = "EXTRA_PETSITTERID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usersRef = db.collection("users");
        petSitterLoadListener = this;
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pet Nanny");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        recyclerView = findViewById(R.id.recycler_view);

        initView();
        loadAllPetSitters();
    }


    private void initView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void loadAllPetSitters(){
        usersRef = db.collection("users");

        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        PetSitter petSitter = documentSnapshot.toObject(PetSitter.class);
                        petSitter.setPetSitterId(documentSnapshot.getId());
                        sitterId = petSitter.getPetSitterId();
                        //filter by isPetSitter
                        if(petSitter.isSitter() && !petSitter.getPetSitterId().equals(FirebaseAuth.getInstance().getUid())) {
                            list.add(petSitter);
                        }
                    }

                    petSitterLoadListener.onAllPetSitterLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                petSitterLoadListener.onAllPetSitterLoadFailed(e.getMessage());
            }
        });
    }


    @Override
    public void onAllPetSitterLoadSuccess(List<PetSitter> petSitterList) {
        PetSitterAdapter adapter = new PetSitterAdapter(petSitterList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAllPetSitterLoadFailed(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        //dialog.dismiss();
    }

    @Override
    public void onPetSitterClick(int position) {
        Intent intent = new Intent(HomeActivity.this, ViewPetSitterActivity.class);
        intent.putExtra(EXTRA_PETSITTERID, list.get(position).getPetSitterId());
        startActivity(intent);
    }


    /**
     * Handles the Back button: closes the nav drawer.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * Inflates the options menu.
     *
     * @param menu Menu to inflate
     * @return Returns true if menu is inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handles a click on the Settings item in the options menu.
     *
     * @param item Item in options menu that was clicked.
     * @return Returns true if the item was Settings.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles a navigation drawer item click. It detects which item was
     * clicked and displays a toast message showing which item.
     *
     * @param item Item in the navigation drawer
     * @return Returns true after closing the nav drawer
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_editProfile:
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
                return true;
            case R.id.nav_editPetProfile:
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(HomeActivity.this, EditPetProfileActivity.class));
                return true;
            case R.id.nav_request:
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(HomeActivity.this, ViewRequestsActivity.class));
                return true;
            case R.id.nav_feedback:
                //drawer.closeDrawer(GravityCompat.START);
                //FirebaseAuth.getInstance().signOut();
                //startActivity(new Intent(HomeActivity.this, GiveFeedbackActivity.class));
                return true;
            case R.id.nav_logout:
                drawer.closeDrawer(GravityCompat.START);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return false;
        }
    }

    /**
     * Displays a toast message.
     *
     * @param message Message to display in toast
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}