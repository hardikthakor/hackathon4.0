package com.example.dell.authorityapp;

/**
 * Created by Dell on 11/02/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ValuesAdapter2 extends RecyclerView.Adapter {

    ArrayList<Values> valuelist =new ArrayList<>();
    Context context;

    public ValuesAdapter2(Context context, ArrayList<Values> valuelist) {
        this.valuelist = valuelist;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        View view = inflater.inflate(R.layout.cardview2, parent, false);
        ValueViewHolder valueViewHolder= new ValueViewHolder(view,context,valuelist);
        //view.setOnClickListener(mOnClickListener);
        return valueViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ValueViewHolder) holder).loc_img1.setImageBitmap(valuelist.get(position).getBitmap());
        ((ValueViewHolder) holder).locTitle2.setText(valuelist.get(position).getTitle());
        ((ValueViewHolder) holder).sever.setText(valuelist.get(position).getPriority());

    }

    @Override
    public int getItemCount() {
        return valuelist.size();
    }


    public static class ValueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView loc_img1;
        TextView locTitle2,sever;
        RatingBar loc_severity;
        Button chat;
        RelativeLayout relativeLayout;

        Context context;
        ArrayList<Values> valuelist;


        public ValueViewHolder(View view,Context context,ArrayList<Values> valuelist) {
            super(view);

            this.valuelist = valuelist;
            this.context=context;
            view.setOnClickListener(this);
            relativeLayout = view.findViewById(R.id.rl);
            loc_img1 = (ImageView)view.findViewById(R.id.loc_img1);
            locTitle2 = (TextView)view.findViewById(R.id.locTitle2);
            sever=(TextView)view.findViewById(R.id.sever);
            chat=(Button) view.findViewById(R.id.btnchat);
           // loc_severity = view.findViewById(R.id.rtbar);
            chat.setOnClickListener(this);
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int postion= getAdapterPosition();
            Values valuelist=this.valuelist.get(postion);

            switch (v.getId())
            {
                case R.id.btnchat:
                    //Toast.makeText(context,valuelist.getTitle(),Toast.LENGTH_LONG).show();
                     Intent i=new Intent(this.context,xmpp_chat.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("AJabberId",valuelist.getAJabberId());
                    bundle.putString("jabberId",valuelist.getJabberId());
                    i.putExtras(bundle);
                    this.context.startActivity(i);

                    break;
                case R.id.rl:
                    Intent intent= new Intent(this.context,ViewLocationAssigned.class);
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
                    // b.putString("from","values");

                    intent.putExtras(b);
                    this.context.startActivity(intent);
                    break;

            }

        }
    }
}
