package com.example.dell.authorityapp;

/**
 * Created by Dell on 15/02/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CGT on 15-02-2018.
 */

public class NewsAdapter extends RecyclerView.Adapter{

    ArrayList<NewsValues> valuelist =new ArrayList<>();
    Context context;

    public NewsAdapter(Context context, ArrayList<NewsValues> valuelist) {
        this.valuelist = valuelist;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        View view = inflater.inflate(R.layout.newscard, parent, false);
        ValueViewHolder valueViewHolder= new ValueViewHolder(view,context,valuelist);
        //view.setOnClickListener(mOnClickListener);
        return valueViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ValueViewHolder) holder).loc_img.setImageBitmap(valuelist.get(position).getBitmap());
        //((ValueViewHolder) holder).locTitle.setText(valuelist.get(position).getTitle());
        //((ValueViewHolder) holder).loc_severity.setRating(4);
        ((ValueViewHolder)holder).locTitle.setText(valuelist.get(position).getTitle());
        ((ValueViewHolder)holder).loc_desc.setText(valuelist.get(position).getDescription());
        ((ValueViewHolder)holder).loc_date.setText(valuelist.get(position).getIssueDate());
    }

    @Override
    public int getItemCount() {
        return valuelist.size();
    }


    public static class ValueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView loc_img;
        TextView locTitle;
        TextView loc_desc;
        TextView loc_date;


        Context context;
        ArrayList<NewsValues> valuelist;


        public ValueViewHolder(View view,Context context,ArrayList<NewsValues> valuelist) {
            super(view);

            this.valuelist = valuelist;
            this.context=context;
            view.setOnClickListener(this);
            loc_img = (ImageView)view.findViewById(R.id.loc_img);
            locTitle = (TextView)view.findViewById(R.id.locTitle);
            loc_desc = view.findViewById(R.id.loc_desc);
            loc_date = view.findViewById(R.id.date);
        }

        @Override
        public void onClick(View v) {
            int postion= getAdapterPosition();
            NewsValues valuelist=this.valuelist.get(postion);
            Intent intent= new Intent(this.context,ViewNews.class);
            Bundle b=new Bundle();
            //intent.putExtra("image_id",contact.getUrl());
            //intent.putExtra("name",contact.getName());
            // intent.putExtra("email",contact.getEmail());
            // intent.putExtra("mobile",contact.getMobile());
            b.putString("imageUrl",valuelist.getImageUrl());
            b.putString("userId",valuelist.getUserId());
            b.putString("lat",valuelist.getLatitude());
            b.putString("lon",valuelist.getLongitude());
            b.putString("timestamp",valuelist.getTimestamp());
            b.putString("type",valuelist.getType());
            b.putString("complaintTitle",valuelist.getTitle());
            b.putString("deviceId",valuelist.getDeviceId());
            b.putString("issueDate",valuelist.getIssueDate());
            b.putString("pincode",valuelist.getPincode());
            b.putString("priority",valuelist.getPriority());
            b.putString("photoUrl",valuelist.getPhotoUrl());
            b.putString("emailId",valuelist.getEmailId());
            b.putString("JabberId",valuelist.getJabberId());
            b.putString("jabberId",valuelist.getAJabberId());
            b.putString("tokenId",valuelist.getTokenId());
            b.putString("proposedDate",valuelist.getProposedDate());
            b.putString("timelineStatus",valuelist.getTimelineStatus());
            b.putString("description",valuelist.getDescription());
            b.putString("location",valuelist.getLocation());
            b.putString("from","values");

            intent.putExtras(b);
            context.startActivity(intent);


        }
    }
}
