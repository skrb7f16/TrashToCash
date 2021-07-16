package com.skrb7f16.trashtocash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.skrb7f16.trashtocash.databinding.ActivitySigninBinding;
import com.skrb7f16.trashtocash.databinding.ActivitySignupBinding;

public class SigninActivity extends AppCompatActivity {
    ActivitySigninBinding binding;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        binding= ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth= FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        progressDialog=new ProgressDialog(SigninActivity.this);
        progressDialog.setTitle("Please wait ");
        progressDialog.setMessage("Logging....");
        binding.btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email=binding.inputEmail.getText().toString();
                String pass=binding.inputPassword.getText().toString();


                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email,pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        progressDialog.hide();
                                        Toast.makeText(SigninActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SigninActivity.this,MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SigninActivity.this, "Sign Error", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        });


                    } else{
                        binding.inputPassword.setError("Empty Fields are not allowed");
                        progressDialog.hide();
                    }


                } else if (email.isEmpty()) {
                    binding.inputEmail.setError("Empty Fields are not allowed");
                    progressDialog.hide();

                } else{
                    binding.inputEmail.setError("Please enter correct email");
                    progressDialog.hide();
                }

            }
        });

        binding.createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this,SignupActivity.class));
                finish();
            }
        });
    }
}