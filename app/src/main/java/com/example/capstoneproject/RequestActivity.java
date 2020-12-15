package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RequestActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "RequestActivity";

    private ImageButton buttonDatePicker;
    private ImageButton buttonStartDatePicker;
    private ImageButton buttonEndDatePicker;
    DatePickerDialog datePickerDialog;
    private TextView textViewEndDate;
    private TextView textViewStartDate;

    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_TOTAL_PAY = "totalPay";
    private static final String KEY_STATUS_OF_REQUEST = "statusOfRequest";
    private static final String KEY_USER_THAT_REQUESTED = "userThatRequested";
    private static final String KEY_FIRST_NAME_OF_USER_THAT_REQUESTED = "firstNameOfUserThatRequested";
    private static final String KEY_LAST_NAME_OF_USER_THAT_REQUESTED = "lastNameOfUserThatRequested";
    private static final String KEY_EMAIL_OF_USER_THAT_REQUESTED = "emailOfUserThatRequested";
    private static final String KEY_NAME_OF_PET = "nameOfPet";
    private static final String KEY_TYPE_OF_PET = "typeOfPet";
    private static final String KEY_AGE_OF_PET = "ageOfPet";
    private static final String KEY_WEIGHT_OF_PET = "weightOfPet";
    private static final String KEY_SPECIAL_CARE_NEEDS_OF_PET = "specialCareNeedsOfPet";

    private DocumentReference requestRef;
    private DocumentReference userRef;
    private DocumentReference petRef;
    private String petId;
    private String petSitterUserId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    private TextInputLayout textInputStartDate;
    private TextInputLayout textInputEndDate;
    private TextInputLayout textInputTotalPay;

    private CollectionReference userPetRef;
    private Spinner spinnerPetName;

    private Button buttonSubmit;

    private CollectionReference pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        //textInputStartDate = findViewById(R.id.text_input_start_date);
        //textInputEndDate = findViewById(R.id.text_input_end_date);
        textInputTotalPay = findViewById(R.id.text_input_total_pay);

        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        petSitterUserId = intent.getStringExtra(ViewPetSitterActivity.EXTRA_PETSITTERID2);
        userRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());

        userPetRef = userRef.collection("pets");

        petId = userPetRef.document().getId();
        //petRef = userPetRef.document(petId);

        //pet = db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("pets");


        spinnerPetName = (Spinner) findViewById(R.id.spinner_pet_name);
        final List<String> petNames = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, petNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPetName.setAdapter(adapter);
        userPetRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String petName = document.getString("name");
                        petNames.add(petName);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(buttonSubmitClickListener);


        //buttonDatePicker = findViewById(R.id.buttonDatePicker);
        textViewStartDate = findViewById(R.id.textViewStartDate);
        buttonStartDatePicker = findViewById(R.id.buttonStartDatePicker);
        buttonStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        textViewEndDate = findViewById(R.id.textViewEndDate);

        buttonEndDatePicker = findViewById(R.id.buttonEndDatePicker);
        buttonEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
//                DialogFragment datePicker = new DatePickerFragment();
//                datePicker.show(getSupportFragmentManager(), "date picker");
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       //textViewEndDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                        //TextView textView = (TextView) findViewById(R.id.textViewDate);
                        textViewEndDate = (TextView) findViewById(R.id.textViewEndDate);
                        textViewEndDate.setText(currentDateString);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //TextView textView = (TextView) findViewById(R.id.textViewDate);
        textViewStartDate = (TextView) findViewById(R.id.textViewStartDate);
        textViewStartDate.setText(currentDateString);
    }

    private void checkForEmptyFields(TextInputLayout input) {
        input.setError("This field is required");
    }

    private View.OnClickListener buttonSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onSubmitClick(view);
        }
    };

    public void onSubmitClick(View view) {
//        boolean isStartDateValid = validateField(textInputStartDate, "^\\d{2}-\\d{2}-\\d{4}$", "Start date is not valid. The format must be MM-DD-YYYY.");
//        boolean isEndDateValid = validateField(textInputEndDate, "^\\d{2}-\\d{2}-\\d{4}$", "End date is not valid. The format must be MM-DD-YYYY.");
        boolean isTotalPayValid = validateField(textInputTotalPay, "^\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$", "Total Pay is not valid. " +
                "Ex: $4,098.09 ; $4098.09 ; $0.35 ; $0 ; $380");

//        final String startDate = textInputStartDate.getEditText().getText().toString().trim();
//        final String endDate = textInputEndDate.getEditText().getText().toString().trim();
        final String startDate = textViewStartDate.getText().toString();
        final String endDate = textViewEndDate.getText().toString();
        final String totalPay = textInputTotalPay.getEditText().getText().toString().trim();
        String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        final String userThatRequested = firebaseAuth.getCurrentUser().getUid();


        //if (isStartDateValid && isEndDateValid && isTotalPayValid) {
        if (startDate != null && endDate != null && isTotalPayValid) {
            requestRef = db.collection("users").document(petSitterUserId).collection("requests")
                    .document(currentDate + " " + System.currentTimeMillis());

            userRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String firstName = documentSnapshot.getString("fName");
                                String lastName = documentSnapshot.getString("lName");
                                String email = documentSnapshot.getString("email");

                                final Map<String, Object> request = new HashMap<>();
                                request.put(KEY_START_DATE, startDate);
                                request.put(KEY_END_DATE, endDate);
                                request.put(KEY_TOTAL_PAY, totalPay);
                                request.put(KEY_USER_THAT_REQUESTED, userThatRequested);
                                request.put(KEY_STATUS_OF_REQUEST, "pending");
                                request.put(KEY_FIRST_NAME_OF_USER_THAT_REQUESTED, firstName);
                                request.put(KEY_LAST_NAME_OF_USER_THAT_REQUESTED, lastName);
                                request.put(KEY_EMAIL_OF_USER_THAT_REQUESTED, email);

                                String petName = spinnerPetName.getSelectedItem().toString();
                                request.put(KEY_NAME_OF_PET, petName);

//                                DocumentReference pet = db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
//                                        .collection("pets").document(petName);//userPetRef.document(petName);

                                petRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                                        .collection("pets").document(petName);

                                //DocumentReference petRef = pet.document(petName);
                                //pet.get()

                                petRef.get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    String type = documentSnapshot.getString("type");
                                                    String age = documentSnapshot.getString("age");
                                                    String weight = documentSnapshot.getString("weight");
                                                    String specialCareNeeds = documentSnapshot.getString("specialCareNeeds");

                                                    request.put(KEY_TYPE_OF_PET, type);
                                                    request.put(KEY_AGE_OF_PET, age);
                                                    request.put(KEY_WEIGHT_OF_PET, weight);
                                                    request.put(KEY_SPECIAL_CARE_NEEDS_OF_PET, specialCareNeeds);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RequestActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                requestRef.set(request)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RequestActivity.this, "Submitted request", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RequestActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RequestActivity.this, "Error submitting request", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, e.toString());
                                            }
                                        });

                            } else {
                                Toast.makeText(RequestActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RequestActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(this, "Invalid", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private boolean validateField(TextInputLayout input, String regex, String errorNotValid) {
        String text = input.getEditText().getText().toString().trim();
        input.setError(null);

        if (text.matches("")) {
            checkForEmptyFields(input);
            return false;
        } else if (!text.matches(regex)) {
            input.setError(errorNotValid);
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }
}