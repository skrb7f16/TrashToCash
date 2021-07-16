package com.skrb7f16.trashtocash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.skrb7f16.trashtocash.databinding.ActivitySignupBinding;
import com.skrb7f16.trashtocash.models.User;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Please wait ");
        progressDialog.setMessage("Registering....");
        mAuth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        binding.btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email=binding.inputEmailReg.getText().toString();
                String pass=binding.inputPasswordReg.getText().toString();
                String confirmpass=binding.confirmPassword.getText().toString();
                if(binding.username.getText().toString().isEmpty()){
                    binding.username.setError("Required");
                    progressDialog.hide();
                    return;
                }
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty() && pass.equals(confirmpass)) {
                        mAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {


                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(
                                                binding.username.getText().toString()
                                        ).build();

                                        firebaseUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                User user=new User(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getDisplayName(),0);
                                                database.getReference().child("Users").child(user.getUserId()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        progressDialog.hide();
                                                        Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                                        finish();
                                                    }
                                                });
                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignupActivity.this, "Registered error", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        });

                    } else{
                        binding.inputPasswordReg.setError("Empty Fields are not allowed");
                    progressDialog.hide();
                    }

                } else if (email.isEmpty()) {
                    binding.confirmPassword.setError("Empty Fields are not allowed");
                    progressDialog.hide();
                } else{
                    binding.inputEmailReg.setError("Please enter correct email");
                progressDialog.hide();}
            }
        });



        binding.AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,SigninActivity.class));
                finish();
            }
        });
    }
}