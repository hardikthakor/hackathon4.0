package com.example.dell.authorityapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    Button bt1,bt2;
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;
    String token,userr,passr,tkn;
    TextView tvt,tx1;
    EditText user,pass;
    int counter = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("tokenFirebase", null);

        user=findViewById(R.id.etUser);
        pass=findViewById(R.id.etPass);
        //userr=user.getText().toString();
       // passr=pass.getText().toString();
        tx1=findViewById(R.id.tvr);
       // bt2=(Button)findViewById(R.id.next);
       bt1=findViewById(R.id.login);
        bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tkn= FirebaseInstanceId.getInstance().getToken();

                Log.i("----", tkn);

                if(user.getText().toString().equals("admin")&&pass.getText().toString().equals("abc123"))
                {
                    Intent i= new Intent(getApplicationContext(),selectionActivity.class);
                    Bundle b= new Bundle();
                    b.putString("authUserName","Prashobh");
                    b.putString("pincode","302001");
                    i.putExtras(b);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("authUserName", "Prashobh");
                    editor.putString("pincode", "302001");
                    editor.commit();
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                    counter--;
                    tx1.setText(Integer.toString(counter));
                    if (counter == 0) {
                        bt1.setEnabled(false);
                    }

                }


//               Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();
//Log.i("------------",token);
//                SendNotification sendNotification = new SendNotification();
//                sendNotification.callApi("titile", "jasndjnsadniosan", "fcZTe3gG7Tc:APA91bGkV6f-3jMKzH7cuSUT2k77cf0ZSPps7upMjmbYQgok2Wxqpsf5PbkR3BqVregZ63HymzDAATYHGqZEGBTujQONtNiZ-CNGTTd9JKTGGuB0Q-V9hQfl8XSvpuxMrrK752KVqlu0");


            }
        });


    }
}
