package com.skrb7f16.trashtocash.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skrb7f16.trashtocash.R;
import com.skrb7f16.trashtocash.UserFeedPage;
import com.skrb7f16.trashtocash.models.CreditModel;
import com.skrb7f16.trashtocash.models.Feeds;
import com.skrb7f16.trashtocash.models.RequestProduct;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    Context context;
    ArrayList<RequestProduct> requestProducts;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    FirebaseAuth auth;
    public RequestAdapter( ArrayList<RequestProduct> requestProducts,Context context) {
        this.context = context;
        this.requestProducts = requestProducts;
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Please wait ");
        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");

    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.requested_object,null);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestProduct r=requestProducts.get(position);
        holder.by.setText(r.getRequesterUsername());
        holder.msg.setText(r.getMessage());
        holder.at.setText(r.getRequestedAt());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Accepting");
                progressDialog.show();
                database.getReference().child("requests").child(r.getRequestId()).child("accepted").setValue(true).addOnSuccessListener(aVoid ->
                        database.getReference().child("feeds").child(r.getProductId()).child("taken").setValue(true).addOnSuccessListener(aVoid1 -> {
                        database.getReference("feeds").child(r.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Feeds feeds=snapshot.getValue(Feeds.class);
                                if(feeds.getDonate()==true){
                                    CreditModel creditModel=new CreditModel();
                                    creditModel.setCredValue((int)(Math.random()*(20-1+1)+1));
                                    creditModel.setProduct(r.getProductId());
                                    creditModel.setRedeemed(false);
                                    creditModel.setProductName(feeds.getTitle());
                                    creditModel.setAt(LocalDateTime.now().toString());
                                    String t=database.getReference().child("credits").child(auth.getUid()).push().getKey();
                                    creditModel.setCredId(t);
                                    database.getReference().child("credits").child(auth.getUid()).child(t).setValue(creditModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.hide();
                                            Intent sendIntent = new Intent("android.intent.action.MAIN");
                                            sendIntent.setAction(Intent.ACTION_VIEW);
                                            sendIntent.setPackage("com.whatsapp");
                                            String url = "https://api.whatsapp.com/send?phone=" + "+91"+r.getWhatsappNo() + "&text=" + "Hi I am interested in your offer\n"+
                                                    r.getMessage()+"\nlets discuus";
                                            sendIntent.setData(Uri.parse(url));
                                            if(sendIntent.resolveActivity(context.getPackageManager()) != null){
                                                context.startActivity(sendIntent);
                                            }
                                        }
                                    });
                                }
                                else{
                                    progressDialog.hide();
                                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                                    sendIntent.setAction(Intent.ACTION_VIEW);
                                    sendIntent.setPackage("com.whatsapp");
                                    String url = "https://api.whatsapp.com/send?phone=" + "+91"+r.getWhatsappNo() + "&text=" + "Hi I am interested in your offer\n"+
                                            r.getMessage()+"\nlets discuus";
                                    sendIntent.setData(Uri.parse(url));
                                    if(sendIntent.resolveActivity(context.getPackageManager()) != null){
                                        context.startActivity(sendIntent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        }));
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("rejecting");
                progressDialog.show();
                database.getReference().child("requests").child(r.getRequestId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.hide();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestProducts.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView by,msg,at;
        Button accept,del;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            by=itemView.findViewById(R.id.requestedBy);
            msg=itemView.findViewById(R.id.requestedMsg);
            at=itemView.findViewById(R.id.requestedAt);
            accept=itemView.findViewById(R.id.accept);
            del=itemView.findViewById(R.id.delete);
        }
    }
    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
