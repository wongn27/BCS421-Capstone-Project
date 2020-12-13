package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPetProfileActivity extends AppCompatActivity {
    private static final String TAG = "AddPetProfileActivity";

    private static final String KEY_NAME = "name";
    private static final String KEY_SEX = "sex";
    private static final String KEY_TYPE = "type";
    private static final String KEY_AGE = "age";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_SPECIAL_CARE_NEEDS = "specialCareNeeds";

    private DocumentReference petRef;
    private String userID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    private TextInputLayout textInputName;
    private TextInputLayout textInputSex;
    private TextInputLayout textInputType;
    private TextInputLayout textInputAge;
    private TextInputLayout textInputWeight;
    private TextInputLayout textInputSpecialCareNeeds;

    private Button buttonAddPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet_profile);

        textInputName = findViewById(R.id.text_input_name);
        textInputSex = findViewById(R.id.text_input_sex);
        textInputType = findViewById(R.id.text_input_type);
        textInputAge = findViewById(R.id.text_input_age);
        textInputWeight = findViewById(R.id.text_input_weight);
        textInputSpecialCareNeeds = findViewById(R.id.text_input_special_care_needs);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        buttonAddPet = findViewById(R.id.buttonAddPet);
        buttonAddPet.setOnClickListener(buttonAddPetClickListener);
    }

    private boolean validateFieldSpecialCareNeeds(TextInputLayout input, String regex, String errorNotValid) {
        String specialCareNeedsInput = input.getEditText().getText().toString().trim();

        if (specialCareNeedsInput.isEmpty()) {
            textInputSpecialCareNeeds.setError("This field is required");
            return false;
        } else if (specialCareNeedsInput.length() > 300) {
            textInputSpecialCareNeeds.setError("Message is too long.");
            return false;
        } else {
            textInputSpecialCareNeeds.setError(null);
            return true;
        }
    }

    private void checkForEmptyFields(TextInputLayout input) {
        input.setError("This field is required");
    }

    private View.OnClickListener buttonAddPetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onAddPetClick(view);
        }
    };

    public void onAddPetClick(View view) {
        boolean isNameValid = validateField(textInputName, "^[A-Z][a-z]{2,29}", "Name is not valid. First letter must be uppercase and must be 3-30 characters");
        boolean isSexValid = validateField(textInputSex, "^[A-Z][a-z]{1,10}", "Sex is not valid. Enter either male or female.");
        boolean isTypeValid = validateField(textInputType, "^[A-Z][a-z]{1,10}", "Type of animal is not valid.");
        boolean isAgeValid = validateField(textInputAge, "^[0-9]{1,2} [a-z]{1,10} [a-z]{1,10}", "Age is not valid. " +
                "Should be in the double digit range and include the words month(s) old or year(s) old.");
        boolean isWeightValid = validateField(textInputWeight, "^[0-9]{1,3} [a-z]{1,10}", "Weight is not valid. Should be in the three digit range and include the word pounds.");
        boolean isSpecialCareNeedsValid = validateFieldSpecialCareNeeds(textInputSpecialCareNeeds, "^[\\s]*(.*?)[\\s]*$", "Special Care Needs is not valid. Include N/A if not applicable.");

        String name = textInputName.getEditText().getText().toString().trim();
        String sex = textInputSex.getEditText().getText().toString().trim();
        String type = textInputType.getEditText().getText().toString().trim();
        String age = textInputAge.getEditText().getText().toString().trim();
        String weight = textInputWeight.getEditText().getText().toString().trim();
        String specialCareNeeds = textInputSpecialCareNeeds.getEditText().getText().toString().trim();

        if (isNameValid && isSexValid && isTypeValid && isAgeValid && isWeightValid && isSpecialCareNeedsValid) {
            Intent intent = new Intent(this, EditPetProfileActivity.class);
            Toast.makeText(this, "Name :" + textInputName.getEditText().getText().toString(), Toast.LENGTH_SHORT).show();

            petRef = db.collection("users").document(userID).collection("pets").document(name);

            Map<String, Object> pet = new HashMap<>();
            pet.put(KEY_NAME, name);
            pet.put(KEY_SEX, sex);
            pet.put(KEY_TYPE, type);
            pet.put(KEY_AGE, age);
            pet.put(KEY_WEIGHT, weight);
            pet.put(KEY_SPECIAL_CARE_NEEDS, specialCareNeeds);

            petRef.set(pet)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddPetProfileActivity.this, "Pet added", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPetProfileActivity.this, "Error adding pet", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });

            startActivity(intent);
            finish();

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
        }
        else if (!text.matches(regex)) {
            input.setError(errorNotValid);
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }
}