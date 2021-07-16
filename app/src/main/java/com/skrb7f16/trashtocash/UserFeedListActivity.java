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
import com.skrb7f16.trashtocash.adapters.FeedAdapter;
import com.skrb7f16.trashtocash.databinding.ActivityNewsFeedsBinding;
import com.skrb7f16.trashtocash.databinding.ActivityUserFeedListsBinding;
import com.skrb7f16.trashtocash.models.Feeds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class UserFeedListActivity extends AppCompatActivity {
    ActivityUserFeedListsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    ArrayList<Feeds> feeds;
    FeedAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_lists);
        binding= ActivityUserFeedListsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth= FirebaseAuth.getInstance();
        getSupportActionBar().setTitle(auth.getCurrentUser().getDisplayName()+" Latest posts");
        database= FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        progressDialog=new ProgressDialog(UserFeedListActivity.this);
        progressDialog.setTitle("Please wait ");
        progressDialog.setMessage("Posts are loaded....");
        feeds=new ArrayList<>();
        adapter=new FeedAdapter(feeds,UserFeedListActivity.this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(UserFeedListActivity.this);
        binding.userFeedsList.setAdapter(adapter);
        binding.userFeedsList.setLayoutManager(linearLayoutManager);
        progressDialog.show();
        database.getReference().child("feeds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d:snapshot.getChildren()){
                    Feeds f=d.getValue(Feeds.class);
                    if(f.getById().equals(auth.getUid())){
                        feeds.add(f);
                    }
                }

                Collections.reverse(feeds);
                adapter.notifyDataSetChanged();
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}