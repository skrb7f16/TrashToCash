package com.skrb7f16.trashtocash;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.skrb7f16.trashtocash.models.ChatRoom;
import com.skrb7f16.trashtocash.models.Feeds;
import com.skrb7f16.trashtocash.models.MessageModels;
import com.skrb7f16.trashtocash.models.RequestProduct;
import com.skrb7f16.trashtocash.models.User;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

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

                requestProduct.setProductName(feed.getTitle());
                String t=database.getReference().child("requests").push().getKey();
                requestProduct.setRequestId(t);
                database.getReference().child("requests").child(t).setValue(requestProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        String senderId=FirebaseAuth.getInstance().getUid();
                        String recieverId=feed.getById();
                        String productId=feed.getId();
                        MessageModels m=new MessageModels(senderId,binding.requestedMsg.getText().toString(),new Date().getTime());
                        final String senderRoom=senderId+recieverId+productId;
                        final String recieverRoom=recieverId+senderId+productId;

                        database.getReference().child("Users").child(recieverId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User reciever=snapshot.getValue(User.class);
                                if(reciever!=null){
                                    if(reciever.getRooms()==null){
                                        ArrayList<String> r=new ArrayList<>();
                                        r.add(recieverRoom);
                                        reciever.setRooms(r);
                                        addRoom(recieverId,reciever,recieverRoom,senderId,m,FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),3);
                                    }else{
                                        int a=0;
                                        for(String s:reciever.rooms){
                                            if(s.equals(recieverRoom)){

                                                a=1;
                                            }

                                        }
                                        if(a==1){
                                            reciever.rooms.add(recieverRoom);
                                            addRoom(recieverId,reciever,recieverRoom,senderId,m,FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),3);
                                        }
                                        else{
                                            progressDialog.hide();
                                            binding.requestedMsg.setText("");

                                            Intent intent=new Intent(FeedPage.this, ChatActivity.class);
                                            intent.putExtra("requesterId",feed.getById());
                                            intent.putExtra("senderId",FirebaseAuth.getInstance().getUid());
                                            intent.putExtra("productId",feed.getId());
                                            intent.putExtra("productName",feed.getTitle());
                                            intent.putExtra("recieverName",feed.getBy());
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        database.getReference().child("Users").child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User sender=snapshot.getValue(User.class);
                                if(sender!=null){
                                    if(sender.getRooms()==null){
                                        ArrayList<String>r=new ArrayList<>();
                                        r.add(senderRoom);
                                        sender.setRooms(r);
                                        addRoom(senderId,sender,senderRoom,recieverId,m,feed.getBy(),4);

                                    }else{
                                        int a=0;
                                        for(String s:sender.rooms){
                                            if(s.equals(senderId)){

                                                a=1;
                                            }

                                        }
                                        if(a==1){
                                            sender.rooms.add(senderRoom);
                                            addRoom(recieverId,sender,senderRoom,recieverId,m,feed.getBy(),4);

                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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
    public void addRoom(String rid,User ob,String roomName,String recieverId,MessageModels m,String recieverName,int isLast){
        database.getReference().child("Users").child(rid).setValue(ob).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ArrayList<MessageModels>mes=new ArrayList<>();
                if(m.getMessage().length()==0)m.setMessage("Hello i want to buy this product");
                mes.add(m);
                ChatRoom cr=new ChatRoom(roomName,mes,feed.getId(),feed.getTitle(),recieverName, recieverId);
                if(isLast!=4) {
                    database.getReference().child("chats").child(roomName).setValue(cr);
                }
                else{
                    database.getReference().child("chats").child(roomName).setValue(cr).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.hide();
                            binding.requestedMsg.setText("");

                            Intent intent=new Intent(FeedPage.this, ChatActivity.class);
                            intent.putExtra("requesterId",feed.getById());
                            intent.putExtra("senderId",FirebaseAuth.getInstance().getUid());
                            intent.putExtra("productId",feed.getId());
                            intent.putExtra("productName",feed.getTitle());
                            intent.putExtra("recieverName",feed.getBy());
                            startActivity(intent);
                        }
                    });

                }
            }
        });
    }
}