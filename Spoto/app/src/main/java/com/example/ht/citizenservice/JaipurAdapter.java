package com.example.ht.citizenservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CGT on 18-03-2018.
 */

public class JaipurAdapter extends RecyclerView.Adapter{

    Context context;
    List<JaipurData> list = new ArrayList<>();

    public JaipurAdapter(Context context, List list){
        this.context = context;
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.jaipurdataview, parent, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((Item) holder).newsImage.setImageBitmap(list.get(position).getBitmap());
        ((Item) holder).srcDate.setText(list.get(position).getSrcDate());
        ((Item) holder).desDate.setText(list.get(position).getDesDate());
        ((Item) holder).main.setText(list.get(position).getName());

        //final String[] namearray = list.get(position).getName().split("\\-");

        //Log.i("---------", namearray[1]);

        //((Item) holder).main.setText("Complaint " + namearray[1]);

        if (position % 2 == 0) {
            //((Item) holder).llcomplaints.setBackgroundColor(Color.parseColor("#9FB3CBf2"));
            ((Item) holder).llcomplaints.setBackground(context.getResources().getDrawable(R.drawable.complaintsview));
            ((Item) holder).srcDate.setTextColor(Color.parseColor("#ffff00"));
            ((Item) holder).desDate.setTextColor(Color.parseColor("#ffff00"));
        }
        else {
            ((Item) holder).llcomplaints.setBackground(context.getResources().getDrawable(R.drawable.complaintsview1));
            ((Item) holder).main.setTextColor(Color.parseColor("#000000"));
            ((Item) holder).srcDate.setTextColor(Color.parseColor("#0000ff"));
            ((Item) holder).desDate.setTextColor(Color.parseColor("#0000ff"));
        }

        ((Item) holder).llcomplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, JaipurDescription.class);
                Bundle b = new Bundle();
                b.putString("name", list.get(position).getName());
                b.putString("url", list.get(position).getUrl());
                b.putString("srcDate", list.get(position).getSrcDate());
                b.putString("desDate", list.get(position).getDesDate());
                b.putString("description", list.get(position).getDes());
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
        TextView srcDate, desDate;
        ImageView newsImage;
        LinearLayout llcomplaints;


        public Item(View itemView) {
            super(itemView);
            llcomplaints = itemView.findViewById(R.id.llcomplaints);
            main = itemView.findViewById(R.id.main);
            srcDate = itemView.findViewById(R.id.srcDate);
            desDate = itemView.findViewById(R.id.desDate);
            newsImage = itemView.findViewById(R.id.newsImage);
        }
    }

}
