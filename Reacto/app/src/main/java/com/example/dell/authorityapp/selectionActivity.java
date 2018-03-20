package com.example.dell.authorityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class selectionActivity extends AppCompatActivity {
    Button btnST,btnAT,btleader,btchat,btnews;
    String authUserName,pincode;
    int pincode1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        btnAT=findViewById(R.id.btnAT);
        btnST=findViewById(R.id.btnST);
        btnews=findViewById(R.id.news);
        btleader=findViewById(R.id.leader);
        btchat=findViewById(R.id.chat);
        Bundle b=getIntent().getExtras();
        authUserName=b.getString("authUserName");
        pincode=b.getString("pincode");
        pincode1=Integer.parseInt(pincode);
        btnST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),ListView.class);
                Bundle b=new Bundle();
                b.putString("authUserName",authUserName);
                b.putString("pincode",pincode);
                i.putExtras(b);
                startActivity(i);
            }
        });
        btnAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),ListView2.class);
                startActivity(i);
            }
        });

        btchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),watson_chat.class);
                startActivity(i);
            }
        });

        btleader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),leader_board.class);
                startActivity(i);
             }
        });
        btnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),News.class);
                startActivity(i);
            }
        });
    }
}
