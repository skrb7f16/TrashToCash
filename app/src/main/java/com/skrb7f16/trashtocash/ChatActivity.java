package com.skrb7f16.trashtocash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skrb7f16.trashtocash.adapters.ChatAdapter;
import com.skrb7f16.trashtocash.databinding.ActivityChatBinding;
import com.skrb7f16.trashtocash.models.MessageModels;
import com.skrb7f16.trashtocash.models.User;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    User sender,reciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        auth=FirebaseAuth.getInstance();
        final String recieverId= getIntent().getStringExtra("requesterId");
        final String senderId=getIntent().getStringExtra("senderId");
        final String productId=getIntent().getStringExtra("productId");
        final String productName=getIntent().getStringExtra("productName");
        final String recieverName=getIntent().getStringExtra("recieverName");
        binding.chattingToWhom.setText(recieverName);
        binding.productNameOfChat.setText(productName);
        final String senderRoom=senderId+recieverId+productId;
        final String recieverRoom=recieverId+senderId+productId;
//        database.getReference().child("Users").child(recieverId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                reciever=snapshot.getValue(User.class);
//                if(reciever!=null){
//                    if(reciever.getRooms()==null){
//                        ArrayList<String>r=new ArrayList<>();
//                        r.add(recieverRoom);
//                        reciever.setRooms(r);
//                        addRoom(recieverId,reciever);
//                    }else{
//                        int a=0;
//                        for(String s:reciever.rooms){
//                            if(s.equals(recieverRoom)){
//
//                                a=1;
//                            }
//
//                        }
//                        if(a==1){
//                            reciever.rooms.add(recieverRoom);
//                            addRoom(recieverId,reciever);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        database.getReference().child("Users").child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                sender=snapshot.getValue(User.class);
//                if(reciever!=null){
//                    if(sender.getRooms()==null){
//                        ArrayList<String>r=new ArrayList<>();
//                        r.add(senderRoom);
//                        sender.setRooms(r);
//                        addRoom(senderId,sender);
//                    }else{
//                        int a=0;
//                        for(String s:sender.rooms){
//                            if(s.equals(senderId)){
//
//                                a=1;
//                            }
//
//                        }
//                        if(a==1){
//                            sender.rooms.add(senderRoom);
//                            addRoom(recieverId,sender);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        final ArrayList<MessageModels> messageModels=new ArrayList<>();

        final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setStackFromEnd(true);

        binding.chatRecyclerView.setLayoutManager(layoutManager);


        database.getReference().child("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren() ){
                    MessageModels model=dataSnapshot.getValue(MessageModels.class);
                    messageModels.add(model);

                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=binding.messageToSend.getText().toString();
                MessageModels m=new MessageModels(senderId,message);
                m.setTimestamp(new Date().getTime());
                binding.messageToSend.setText("");
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats").child(recieverRoom).child("messages").push().setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        }

    public void addRoom(String rid,User ob){
        database.getReference().child("Users").child(rid).setValue(ob);
    }
}