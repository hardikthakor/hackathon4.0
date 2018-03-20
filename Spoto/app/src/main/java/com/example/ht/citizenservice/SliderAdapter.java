package com.example.ht.citizenservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by CGT on 11-02-2018.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {R.drawable.slidingimg1, R.drawable.slidingimg2, R.drawable.slidingimg3};


    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slidelayer, container, false);

        RelativeLayout relativeLayout = view.findViewById(R.id.rv);
        Button bt = view.findViewById(R.id.next);

        if (position == (slide_images.length - 1)){
            bt.setVisibility(View.VISIBLE);
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, MainActivity.class);
                // Shared Pref
                String pref = "UserData";
                SharedPreferences sharedpreferences;
                sharedpreferences = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("slideCheck", true);
                editor.commit();
                context.startActivity(i);

            }
        });

        relativeLayout.setBackgroundResource(slide_images[position]);

        container.addView(view);

        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
