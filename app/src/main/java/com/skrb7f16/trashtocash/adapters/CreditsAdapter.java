package com.skrb7f16.trashtocash.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.skrb7f16.trashtocash.R;
import com.skrb7f16.trashtocash.models.CreditModel;
import com.skrb7f16.trashtocash.models.User;

import java.util.ArrayList;


public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder> {

    ArrayList<CreditModel> creditModels=new ArrayList<>();
    Context context;
    User user;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    Dialog dialog;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CreditsAdapter(ArrayList<CreditModel> creditModels, Context context) {
        this.creditModels = creditModels;
        this.context = context;
        database= FirebaseDatabase.getInstance("https://trash-to-cash-20037-default-rtdb.firebaseio.com/");
        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Your coupon is being redeemed");
        dialog=new Dialog(context);
    }


    @NonNull
    @Override
    public CreditsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_credit_tab,null);
        return new CreditsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CreditsViewHolder holder, int position) {
        CreditModel c=creditModels.get(position);
        holder.productName.setText(c.getProductName());
        holder.redeemValue.setText("You have won "+c.getCredValue()+" credit points");
        holder.madeAt.setText(c.getAt());
        if(c.isRedeemed()){
            holder.redeem.setEnabled(false);
        }
        else{
            holder.redeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    user.setTotalCredits(user.getTotalCredits()+c.getCredValue());
                    c.setRedeemed(true);
                    database.getReference().child("Users").child(auth.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            database.getReference().child("credits").child(auth.getUid()).child(c.getCredId()).setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.hide();
                                }
                            });
                        }
                    });
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return creditModels.size();
    }

    public class CreditsViewHolder extends RecyclerView.ViewHolder  {
        public TextView productName,redeemValue,madeAt;
        public Button redeem;

        public CreditsViewHolder(@NonNull View itemView) {
            super(itemView);

            productName=itemView.findViewById(R.id.productname);
            redeem=itemView.findViewById(R.id.redeem);
            madeAt=itemView.findViewById(R.id.madeAt);
            redeemValue=itemView.findViewById(R.id.redeemedValue);
        }
    }
}
