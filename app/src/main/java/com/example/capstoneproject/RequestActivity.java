package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "RequestActivity";

    private ImageButton buttonDatePicker;

    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_TOTAL_PAY = "totalPay";
    private static final String KEY_STATUS_OF_REQUEST = "statusOfRequest";
    private static final String KEY_USER_THAT_REQUESTED = "userThatRequested";

    private DocumentReference requestRef;
    private String petSitterUserId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    private TextInputLayout textInputStartDate;
    private TextInputLayout textInputEndDate;
    private TextInputLayout textInputTotalPay;

    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        textInputStartDate = findViewById(R.id.text_input_start_date);
        textInputEndDate = findViewById(R.id.text_input_end_date);
        textInputTotalPay = findViewById(R.id.text_input_total_pay);

        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        petSitterUserId = intent.getStringExtra(ViewPetSitterActivity.EXTRA_PETSITTERID2);

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(buttonSubmitClickListener);


        buttonDatePicker = findViewById(R.id.buttonDatePicker);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
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

        TextView textView = (TextView) findViewById(R.id.textViewDate);
        textView.setText(currentDateString);
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
        boolean isStartDateValid = validateField(textInputStartDate, "^\\d{2}-\\d{2}-\\d{4}$", "Start date is not valid. The format must be MM-DD-YYYY.");
        boolean isEndDateValid = validateField(textInputEndDate, "^\\d{2}-\\d{2}-\\d{4}$", "End date is not valid. The format must be MM-DD-YYYY.");
        boolean isTotalPayValid = validateField(textInputTotalPay, "^\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$", "Total Pay is not valid. " +
                "Ex: $4,098.09 ; $4098.09 ; $0.35 ; $0 ; $380");

        String startDate = textInputStartDate.getEditText().getText().toString().trim();
        String endDate = textInputEndDate.getEditText().getText().toString().trim();
        String totalPay = textInputTotalPay.getEditText().getText().toString().trim();
        String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        String userThatRequested = firebaseAuth.getCurrentUser().getUid();


        if (isStartDateValid && isEndDateValid && isTotalPayValid) {
            requestRef = db.collection("users").document(petSitterUserId).collection("requests")
                    .document(currentDate + " " + System.currentTimeMillis());

            Map<String, Object> request = new HashMap<>();
            request.put(KEY_START_DATE, startDate);
            request.put(KEY_END_DATE, endDate);
            request.put(KEY_TOTAL_PAY, totalPay);
            request.put(KEY_USER_THAT_REQUESTED, userThatRequested);
            request.put(KEY_STATUS_OF_REQUEST, "pending");

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