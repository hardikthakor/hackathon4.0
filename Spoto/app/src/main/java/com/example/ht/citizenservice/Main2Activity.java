package com.example.ht.citizenservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class Main2Activity extends AppCompatActivity {

    ViewPager viewPager;

    SliderAdapter sliderAdapter;

    String pref = "UserData" ;
    SharedPreferences sharedpreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        viewPager = findViewById(R.id.pgView);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        sliderAdapter = new SliderAdapter(this);

        if (!sharedpreferences.getBoolean("slideCheck", false)) {
            viewPager.setAdapter(sliderAdapter);
        }
        else {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }
    }
}
