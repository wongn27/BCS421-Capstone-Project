package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private final int REQUEST_EMAIL = 0;

    EditText email, password;
    Button btnLogin;
    TextView register;
    FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        register = findViewById(R.id.register);

        if (mFirebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailX = email.getText().toString();
                String pwdX = password.getText().toString();
                int flags = 0;

                if(!isValidEmail(emailX)){
                    email.setError("Not a valid email address");
                    flags++;
                }

                if(pwdX.length() < 6){
                    password.setError("Must be at least 6 characters");
                    flags++;
                }

                if (emailX.isEmpty()){
                    email.setError("Please enter an email");
                    flags++;
                }

                if (pwdX.isEmpty()){
                    password.setError("Please enter a password");
                    flags++;
                }

                //Try to Login
                if (flags == 0){
                    mFirebaseAuth.signInWithEmailAndPassword(emailX, pwdX).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login error, please try again", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Will return to this activity after Registration is completed
                //startActivityForResult(new Intent(LoginActivity.this, RegistrationActivity.class), REQUEST_EMAIL);
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public boolean isValidEmail(String e) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return e.matches(regex);
    }

}