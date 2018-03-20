package com.example.dell.authorityapp;

/**
 * Created by Dell on 14/02/2018.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
public class LeaderBoardAdapter extends RecyclerView.Adapter {

    Context context;
    List<LeaderBoardData> list = new ArrayList<>();

    public LeaderBoardAdapter(Context context, List list){
        this.context = context;
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.leaderboardview, parent, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((Item) holder).number.setText(list.get(position).getId());
        ((Item) holder).profImg.setImageBitmap(list.get(position).getBitmap());
        ((Item) holder).uname.setText(list.get(position).getName());
        ((Item) holder).pointsview.setText(list.get(position).getPoints());
        ((Item) holder).medal.setImageResource(list.get(position).getMedalimage());

        ((Item) holder).ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, list.get(position).getPoints(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView number;
        CircleImageView profImg;
        TextView uname;
        TextView pointsview;
        ImageView medal;
        LinearLayout ll;

        public Item(View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.ll);
            number = itemView.findViewById(R.id.number);
            profImg = itemView.findViewById(R.id.profImg);
            uname = itemView.findViewById(R.id.uname);
            pointsview = itemView.findViewById(R.id.pointsview);
            medal = itemView.findViewById(R.id.medal);
        }
    }
}
