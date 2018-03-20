package com.example.ht.citizenservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CGT on 19-03-2018.
 */

public class HoteloAdapter extends RecyclerView.Adapter{

    ArrayList<HoteloValues> valuelist =new ArrayList<>();
    Context context;

    public HoteloAdapter(Context context, ArrayList<HoteloValues> valuelist) {
        this.valuelist = valuelist;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        View view = inflater.inflate(R.layout.hotelocard, parent, false);
        ValueViewHolder valueViewHolder= new ValueViewHolder(view, context, valuelist);
        //view.setOnClickListener(mOnClickListener);
        return valueViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ValueViewHolder) holder).hotelTitle.setText(valuelist.get(position).getHname());
        ((ValueViewHolder) holder).sever.setText(valuelist.get(position).getHrating());
        ((ValueViewHolder) holder).rat.setRating(Float.parseFloat(valuelist.get(position).getHrating()));

    }

    @Override
    public int getItemCount() {
        return valuelist.size();
    }


    public static class ValueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView hotelTitle,sever;
        RatingBar rat;


        Context context;
        ArrayList<HoteloValues> valuelist;


        public ValueViewHolder(View view,Context context,ArrayList<HoteloValues> valuelist) {
            super(view);

            this.valuelist = valuelist;
            this.context=context;
            view.setOnClickListener(this);
            //loc_img = (ImageView)view.findViewById(R.id.loc_img);
            hotelTitle = (TextView)view.findViewById(R.id.hotelTitle);
            sever=(TextView)view.findViewById(R.id.sever);
            rat=(RatingBar)view.findViewById(R.id.rat);
            // loc_severity = view.findViewById(R.id.rtbar);

        }

        @Override
        public void onClick(View v) {
            int postion= getAdapterPosition();
            HoteloValues valuelist=this.valuelist.get(postion);
            Intent intent= new Intent(this.context, HoteloView.class);
            Bundle b=new Bundle();
            //intent.putExtra("image_id",contact.getUrl());
            //intent.putExtra("name",contact.getName());
            // intent.putExtra("email",contact.getEmail());
            // intent.putExtra("mobile",contact.getMobile());
            b.putString("hrating",valuelist.getHrating());
            b.putString("hlat",valuelist.getHlat());
            b.putString("hlong",valuelist.getHlong());
            b.putString("hname",valuelist.getHname());
            b.putString("desc",valuelist.getDesc());
            b.putString("hurl",valuelist.getHurl());
            b.putString("from","values");

            intent.putExtras(b);
            this.context.startActivity(intent);


        }
    }

}
