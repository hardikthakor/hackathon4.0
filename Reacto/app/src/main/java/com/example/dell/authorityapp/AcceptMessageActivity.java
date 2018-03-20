package com.example.dell.authorityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AcceptMessageActivity extends AppCompatActivity {

    TextView message,alert;
    Button btnmessage;
    String mes,mes1,authUserName,pincode,alert1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_message);

        message=findViewById(R.id.tvmessage);
        alert=findViewById(R.id.tvalert);
        //mes=message.getText().toString();
        Bundle b=getIntent().getExtras();
        mes1=b.getString("message");
        alert1=b.getString("alert");
        authUserName=b.getString("authUserName");
        pincode=b.getString("pincode");
        message.setText(mes1);
        alert.setText(alert1);
        btnmessage=findViewById(R.id.btnhome);
        btnmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),selectionActivity.class);
                Bundle b=new Bundle();
                b.putString("authUserName","Prashobh");
                b.putString("pincode","302001");
                i.putExtras(b);
                startActivity(i);
            }
        });



    }
}
