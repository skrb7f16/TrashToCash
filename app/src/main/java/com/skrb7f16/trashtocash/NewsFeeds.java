package com.skrb7f16.trashtocash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;

import com.cloudinary.Cloudinary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skrb7f16.trashtocash.adapters.FeedAdapter;
import com.skrb7f16.trashtocash.databinding.ActivityNewsFeedsBinding;
import com.skrb7f16.trashtocash.models.Feeds;

import java.util.ArrayList;
import java.util.Collections;

public class NewsFeeds extends AppCompatActivity {
    ActivityNewsFeedsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    ArrayList<Feeds> feeds;
    FeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feeds);
        binding=ActivityNewsFeedsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Latest posts");
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        progressDialog=new ProgressDialog(NewsFeeds.this);
        progressDialog.setTitle("Please wait ");
        progressDialog.setMessage("Posts are loaded....");
        feeds=new ArrayList<>();
        adapter=new FeedAdapter(feeds,NewsFeeds.this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(NewsFeeds.this);
        binding.newsFeeds.setAdapter(adapter);
        binding.newsFeeds.setLayoutManager(linearLayoutManager);
        progressDialog.show();
        database.getReference().child("feeds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feeds.clear();
                for(DataSnapshot d:snapshot.getChildren()){
                    Feeds f=d.getValue(Feeds.class);
                    if(f.getTaken()==false) {
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