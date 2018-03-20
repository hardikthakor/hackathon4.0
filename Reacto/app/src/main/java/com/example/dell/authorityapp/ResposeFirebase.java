package com.example.dell.authorityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResposeFirebase extends AppCompatActivity {

    TextView tv, tv1;
    int count1=0;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respose_firebase);

        tv = findViewById(R.id.tv);
        tv1 = findViewById(R.id.tv1);

        //Bundle b = getIntent().getExtras();

        //tv.setText(b.getString("data"));

        /*
        b.putString("imageurl",valuelist.getImageUrl());
            b.putString("userId",valuelist.getUserId());
            b.putString("lat",valuelist.getLatitude());
            b.putString("lon",valuelist.getLongitude());
            b.putString("timestamp",valuelist.getTimestamp());
            b.putString("type",valuelist.getType());
            b.putString("title",valuelist.getTitle());
        */


        Bundle bundle = getIntent().getExtras();
        String s = getIntent().getStringExtra("data");

        //if (bundle != null) {
            //bundle must contain all info sent in "data" field of the notification
         //   if (bundle.getString("data") != null) {
                // online
                //Toast.makeText(getApplicationContext(), bundle.getString("data"), Toast.LENGTH_SHORT).show();
           //     tv.setText(bundle.getString("data"));

                Intent intent=new Intent(getApplicationContext(),ListView.class);
                Bundle b=new Bundle();
                b.putString("data", bundle.getString("data"));
                data=bundle.getString("data");
                intent.putExtras(b);
                startActivity(intent);
                //finish();
            //}
            //else {
                // offline
                //Toast.makeText(getApplicationContext(), bundle.getString("key_1"), Toast.LENGTH_SHORT).show();
             //   tv.setText(bundle.toString());
               // Intent intent=new Intent(getApplicationContext(),ListView.class);
                //intent.putExtras(bundle);
                //startActivity(intent);
            //}
        //}
        //else {
          //  if (s != null) {
                //bundle must contain all info sent in "data" field of the notification
                //Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
            //    tv1.setText(s);
            //}
           // else {
             //   Toast.makeText(getApplicationContext(),"empty", Toast.LENGTH_SHORT).show();
           // }
        //}



    }
}
