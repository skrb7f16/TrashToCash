package com.skrb7f16.trashtocash;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skrb7f16.trashtocash.adapters.RequestAdapter;
import com.skrb7f16.trashtocash.databinding.ActivityFeedPageBinding;
import com.skrb7f16.trashtocash.databinding.ActivityUserFeedPageBinding;
import com.skrb7f16.trashtocash.models.Feeds;
import com.skrb7f16.trashtocash.models.RequestProduct;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserFeedPage extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ActivityUserFeedPageBinding binding;
    Feeds feed;
    ImageView[]imageView=new ImageView[3];
    ProgressDialog progressDialog;
    ArrayList<RequestProduct >requestProducts;
    RequestAdapter requestAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_page);
        binding=ActivityUserFeedPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestProducts=new ArrayList<>();
         requestAdapter=new RequestAdapter(requestProducts,UserFeedPage.this);
         binding.requestsRecyclerVIew.setAdapter(requestAdapter);
         binding.requestsRecyclerVIew.setLayoutManager(new LinearLayoutManager(UserFeedPage.this));
        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        imageView[0]=binding.feedPagePic1;
        imageView[1]=binding.feedpagePic2;
        imageView[2]=binding.feedPagePic3;
        progressDialog=new ProgressDialog(UserFeedPage.this);
        progressDialog.setTitle("Please wait ");
        progressDialog.setMessage("Posts are loaded....");
        progressDialog.show();
        database.getReference().child("feeds").child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feed=snapshot.getValue(Feeds.class);
                setItems();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void setItems(){
        binding.feedCity.setText(feed.getCity());
        binding.feedAddress.setText(feed.getAddress());
        binding.feedTitle.setText(feed.getTitle());
        binding.feedTime.setText(feed.getAt());
        binding.feedType.setText(feed.getType());
        binding.feedDesc.setText(feed.getDesc());
        int count=0;
        for (String u:feed.getPics()){
            imageView[count].setVisibility(View.VISIBLE);
            Picasso.get().load(u).into(imageView[count]);
            count++;
        }
        if(!feed.getTaken())
        getRequests();
        else{
            binding.requestsRecyclerVIew.setVisibility(View.GONE);
            binding.requestHeading.setVisibility(View.GONE);
            binding.sold.setVisibility(View.VISIBLE);
            progressDialog.hide();
        }
    }

    public void getRequests(){
        database.getReference().child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestProducts.clear();
                for(DataSnapshot d:snapshot.getChildren()){
                    RequestProduct r=d.getValue(RequestProduct.class);

                    if(r.getProductId().equals(feed.getId()) && !r.isAccepted()) {
                        requestProducts.add(r);
                    }
                }
                requestAdapter.notifyDataSetChanged();
                progressDialog.hide();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}