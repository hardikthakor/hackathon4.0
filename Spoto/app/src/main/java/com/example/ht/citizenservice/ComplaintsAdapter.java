package com.example.ht.citizenservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by CGT on 12-02-2018.
 */

public class ComplaintsAdapter extends RecyclerView.Adapter {

    Context context;
    List<ComplaintsData> list = new ArrayList<>();

    public ComplaintsAdapter(Context context, List list){
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_timeline_listview, parent, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //((Item) holder).main.setText(list.get(position).getComplaintsTitle());
        ((Item) holder).complaintImage.setImageBitmap(list.get(position).getBitmap());
        ((Item) holder).des.setText(list.get(position).getTimelineStatus());

        final String[] namearray = list.get(position).getComplaintsTitle().split("\\-");

        //Log.i("---------", namearray[1]);

        ((Item) holder).main.setText("Complaint " + namearray[1]);

        if (position % 2 == 0) {
            //((Item) holder).llcomplaints.setBackgroundColor(Color.parseColor("#9FB3CBf2"));
            ((Item) holder).llcomplaints.setBackground(context.getResources().getDrawable(R.drawable.complaintsview));
            ((Item) holder).des.setTextColor(Color.parseColor("#ffff00"));
        }
        else {
            ((Item) holder).llcomplaints.setBackground(context.getResources().getDrawable(R.drawable.complaintsview1));
            ((Item) holder).main.setTextColor(Color.parseColor("#000000"));
            ((Item) holder).des.setTextColor(Color.parseColor("#0000ff"));
        }

        ((Item) holder).llcomplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TimeLineViewActv.class);
                Bundle b = new Bundle();
                b.putString("complaintTitle", list.get(position).getComplaintsTitle());
                b.putString("timelineStatus", list.get(position).getTimelineStatus());
                b.putString("AJabberId", list.get(position).getAJabberId());
                i.putExtras(b);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView main;
        TextView des;
        ImageView complaintImage;
        LinearLayout llcomplaints;


        public Item(View itemView) {
            super(itemView);
            llcomplaints = itemView.findViewById(R.id.llcomplaints);
            main = itemView.findViewById(R.id.main);
            des = itemView.findViewById(R.id.des);
            complaintImage = itemView.findViewById(R.id.complaintImage);
        }
    }
}
