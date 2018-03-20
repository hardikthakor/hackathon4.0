package com.example.ht.citizenservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ResponseNoti extends AppCompatActivity {

    TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_noti);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        Bundle bundle = getIntent().getExtras();
        String s = getIntent().getStringExtra("data");

        startActivity(new Intent(getApplicationContext(), Complaints.class));
        finish();

        /*
        if (bundle != null) {
            //bundle must contain all info sent in "data" field of the notification
            if (bundle.getString("data") != null) {
                // online
                //Toast.makeText(getApplicationContext(), bundle.getString("data"), Toast.LENGTH_SHORT).show();
                tv1.setText(bundle.getString("data"));
                startActivity(new Intent(getApplicationContext(), Complaints.class));
                finish();
            }
            else {
                // offline
                //Toast.makeText(getApplicationContext(), bundle.getString("key_1"), Toast.LENGTH_SHORT).show();
                tv1.setText(bundle.toString());
            }
        }
        else {
            if (s != null) {
                //bundle must contain all info sent in "data" field of the notification
                //Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                tv2.setText(s);
            }
            else {
                Toast.makeText(getApplicationContext(),"empty", Toast.LENGTH_SHORT).show();
            }
        }
        */
    }
}
