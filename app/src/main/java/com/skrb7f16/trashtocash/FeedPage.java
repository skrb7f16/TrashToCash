package com.skrb7f16.trashtocash;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.skrb7f16.trashtocash.databinding.ActivityFeedPageBinding;
import com.skrb7f16.trashtocash.models.Feeds;
import com.skrb7f16.trashtocash.models.RequestProduct;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;

public class FeedPage extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ActivityFeedPageBinding binding;
    Feeds feed;
    ImageView[]imageView=new ImageView[3];
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_page);
        binding=ActivityFeedPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        imageView[0]=binding.feedPagePic1;
        imageView[1]=binding.feedpagePic2;
        imageView[2]=binding.feedPagePic3;
        progressDialog=new ProgressDialog(FeedPage.this);
        progressDialog.setTitle("Please wait ");
        progressDialog.setMessage("Posts are loaded....");
        progressDialog.show();
        database.getReference().child("feeds").child(getIntent().getStringExtra("id")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feed=snapshot.getValue(Feeds.class);
                setItems();
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.requestBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(binding.requestedMsg.getText().toString().length()==0){
                    Toast.makeText(FeedPage.this,"Message cannt be empty",Toast.LENGTH_SHORT);
                    binding.requestedMsg.setError("Required");
                    return;
                }
                if(binding.whatsapp.getText().toString().length()==0){
                    Toast.makeText(FeedPage.this,"Number needed",Toast.LENGTH_SHORT);
                    binding.requestedMsg.setError("Required");
                    return;
                }
                progressDialog.setMessage("Making your request");
                progressDialog.show();
                RequestProduct requestProduct=new RequestProduct();
                requestProduct.setMessage(binding.requestedMsg.getText().toString());
                requestProduct.setProductId(feed.getId());
                requestProduct.setRequestedAt(LocalDateTime.now().toString());
                requestProduct.setRequesterUsername(auth.getCurrentUser().getDisplayName());
                requestProduct.setRequesterId(auth.getCurrentUser().getUid());
                requestProduct.setRequestedToId(feed.getById());
                requestProduct.setRequestedToUsername(feed.getBy());
                requestProduct.setWhatsappNo(binding.whatsapp.getText().toString());
                String t=database.getReference().child("requests").push().getKey();
                requestProduct.setRequestId(t);
                database.getReference().child("requests").child(t).setValue(requestProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.hide();
                        binding.requestedMsg.setText("");
                        binding.whatsapp.setText("");
                    }
                });
            }
        });
    }

    public void setItems(){
        binding.feedBy.setText(feed.getBy());
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
        if(feed.getById().equals(auth.getCurrentUser().getUid())){
            binding.requestingView.setVisibility(View.GONE);
        }
    }
}