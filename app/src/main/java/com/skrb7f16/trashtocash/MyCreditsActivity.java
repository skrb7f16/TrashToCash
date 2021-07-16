package com.skrb7f16.trashtocash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skrb7f16.trashtocash.adapters.CreditsAdapter;
import com.skrb7f16.trashtocash.databinding.ActivityMyCreditsBinding;
import com.skrb7f16.trashtocash.models.CreditModel;
import com.skrb7f16.trashtocash.models.User;

import java.util.ArrayList;
import java.util.Collections;

public class MyCreditsActivity extends AppCompatActivity {

    ActivityMyCreditsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    User user;
    CreditsAdapter adapter;
    ArrayList<CreditModel> creditModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_credits);
        binding=ActivityMyCreditsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        creditModels=new ArrayList<>();
        adapter=new CreditsAdapter(creditModels,MyCreditsActivity.this);
        binding.creditRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MyCreditsActivity.this);
        binding.creditRecyclerView.setLayoutManager(linearLayoutManager);
        database= FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        progressDialog=new ProgressDialog(MyCreditsActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Your credits are loading");
        progressDialog.show();

        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 user=snapshot.getValue(User.class);
                 binding.heading.setText("Total credits : "+user.getTotalCredits());
                 bringCredits();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void bringCredits(){
        adapter.setUser(user);
        database.getReference().child("credits").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                creditModels.clear();
                for (DataSnapshot d:snapshot.getChildren()){
                    CreditModel c=d.getValue(CreditModel.class);
                    creditModels.add(c);
                }
                Collections.reverse(creditModels);
                adapter.notifyDataSetChanged();

                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}