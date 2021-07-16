package com.skrb7f16.trashtocash.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.skrb7f16.trashtocash.FeedPage;
import com.skrb7f16.trashtocash.R;
import com.skrb7f16.trashtocash.UserFeedPage;
import com.skrb7f16.trashtocash.models.Feeds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    ArrayList<Feeds> feeds=new ArrayList<>();
    int count=0;
    Context context;

    public FeedAdapter(ArrayList<Feeds> feeds, Context c) {
        this.feeds = feeds;
        context=c;
    }

    @NonNull
    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed_page_feed,null);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.FeedViewHolder holder, int position) {

            Feeds f=feeds.get(position);
            holder.title.setText(f.getTitle());
            holder.by.setText(f.getBy());
            holder.city.setText(f.getCity());
            holder.at.setText(f.getTitle());
            holder.type.setText(f.getType());
            holder.at.setText(f.getAt());
            Picasso.get().load(f.getPics().get(0)).into(holder.pic);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if(FirebaseAuth.getInstance().getUid()==null){
                        Toast.makeText(context,"Please login to see more",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(f.getById().equals(FirebaseAuth.getInstance().getUid())){
                         intent=new Intent(context,UserFeedPage.class);
                    }
                    else {
                         intent = new Intent(context, FeedPage.class);
                    }
                    intent.putExtra("id",f.getId());
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder{
        public TextView title,city,at,by,type;
        public ImageView pic;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.feed_title);
            city=itemView.findViewById(R.id.feed_city);
            type=itemView.findViewById(R.id.requestedBy);
            at=itemView.findViewById(R.id.feed_time);
            by=itemView.findViewById(R.id.feed_by);
            pic=itemView.findViewById(R.id.feedPagePic1);
        }
    }
}
