package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.ETC1;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.journey.R;
import com.example.journey.databinding.ActivityLoginBinding;
import com.example.journey.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    private Button btLogin;
    private Button btSignup;
    private EditText etEmail;
    private EditText etPassword;

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bindElements();
        setupElements();
    }

    private void bindElements() {
        btLogin = binding.btSignIn;
        btSignup = binding.btSignUp;
        etEmail = binding.etEmail;
        etPassword = binding.etPassword;
    }

    private void setupElements() {
        setupButtons();
    }

    private void setupButtons() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignin();
            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignup();
            }
        });
    }

    private void handleSignin() {
        if (!inputsNonNull()) {
            Toast.makeText(this, "Email and password fields must be filled out.", Toast.LENGTH_SHORT).show();
            return;
        }
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Timber.d("signInWithEmail:success");
                            goToMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Timber.w(task.getException(), "signInWithEmail:failure");
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleSignup() {
        if (!inputsNonNull()) {
            Toast.makeText(this, "Email and password fields must be filled out.", Toast.LENGTH_SHORT).show();
            return;
        }
        final String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Timber.d("createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            User newUser = new User(currentUser.getEmail(), currentUser.getUid());
                            mDatabase.child("users").child(newUser.getUserId()).setValue(newUser);
                            goToMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Timber.w(task.getException(), "createUserWithEmail:failure");
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean inputsNonNull() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        return !email.isEmpty() && !password.isEmpty();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToMainActivity();
        }
    }


    private void goToMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }



}