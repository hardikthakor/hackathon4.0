package com.example.dell.authorityapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import okhttp3.OkHttpClient;

import static com.example.dell.authorityapp.ListView.MEDIA_TYPE;

public class RejectActivity extends AppCompatActivity {
    Button btrej;
    EditText edrej,eddate;
    OkHttpClient client = new OkHttpClient();
    String rese,resed, pincodeA ,authUserName,message;
    String userid,type,timestamp,lat,lon,complaintTitle,imageurl,tokenId,acceptance,proposedDate,location,description,deviceId,dt, pincode,issueDate,jabberId,JabberId,timelineStatus,priority,photoUrl,emailId;
    String DataPayload = "";
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;
    Response responseapi;
    String date;
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject);
        btrej=findViewById(R.id.btrej);
        edrej=findViewById(R.id.edrej);
        //eddate=findViewById(R.id.eddate);
        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        pincodeA = sharedpreferences.getString("pincode", null);
        authUserName=sharedpreferences.getString("authUserName",null);
        date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        Bundle b=getIntent().getExtras();
        //loc_img=findViewById(R.id.img);
        imageurl=b.getString("imageUrl");
        userid=b.getString("userId");
        lat=b.getString("lat");
        lon=b.getString("lon");
        timestamp=b.getString("timestamp");
        type=b.getString("type");
        complaintTitle=b.getString("complaintTitle");
        deviceId=b.getString("deviceId");
        issueDate=b.getString("issueDate");
        pincode=b.getString("pincode");
        priority=b.getString("priority");
        photoUrl=b.getString("photoUrl");
        emailId=b.getString("emailId");
        JabberId=b.getString("JabberId");
        jabberId=b.getString("jabberId");
        tokenId=b.getString("tokenId");
        proposedDate=b.getString("proposedDate");
        timelineStatus=b.getString("timelineStatus");
        description=b.getString("description");
        location=b.getString("location");
        btrej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   // Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
                    call();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });


    }

    public void call() {
        final String urli= "http://52.230.17.234:8011/rejects";
        rese= edrej.getText().toString();
        //resed=eddate.getText().toString();
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                postdata = new JSONObject();
                try {
                    postdata.put("Status", "Rejected");
                    postdata.put("location", location);
                    postdata.put("pincode", pincode);
                    postdata.put("description",description);
                    postdata.put("userId",userid);
                    postdata.put("timestamp",timestamp);
                    postdata.put("type",type);
                    postdata.put("imageurl",imageurl);
                    postdata.put("latitude",lat);
                    postdata.put("longitude",lon);
                    postdata.put("complaintTitle",complaintTitle);
                    postdata.put("issueDate",issueDate);
                    postdata.put("jabberId",jabberId);
                    postdata.put("acceptance",acceptance);
                    postdata.put("priority",priority);
                    postdata.put("deviceId",deviceId);
                    postdata.put("acceptDate",date);
                    postdata.put("authUserName","Prashobh");



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                request = new Request.Builder()
                        .url(urli)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                responseapi = null;
                try {
                    responseapi = client.newCall(request).execute();
                    DataPayload = responseapi.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return DataPayload;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(),result,Toast.LENGTH_LONG).show();
                Log.i("----res",result);
                // tv.setText(result);
                Intent i=new Intent(getApplicationContext(),AcceptMessageActivity.class);
                Bundle b= new Bundle();
                b.putString("message",result);
                i.putExtras(b);
                startActivity(i);
                ApiSucess(result);
            }

        }.execute();

    }

    public void ApiSucess(String result){

        try {
            Log.i("--------", "---------1");
            JSONObject jsonArray = new JSONObject(result);
            message = jsonArray.optString("msg");

            if (message.equals("Job has been rejected")) {
                Log.i("--------", "---------" + message);
                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), AcceptMessageActivity.class);
                Bundle b = new Bundle();
                b.putString("message", message);
                b.putString("alert","Further Details will be proceeded to higher authority.");
                b.putString("authUserName", "Prithvi");
                b.putString("pincode", "400103");
                i.putExtras(b);
                startActivity(i);
            }
            else if(message.equals("Job has been already assigned")) {
                Intent i = new Intent(getApplicationContext(), AcceptMessageActivity.class);
                Bundle b = new Bundle();
                b.putString("message", message);
                b.putString("authUserName", authUserName);
                b.putString("pincode", pincodeA);
                i.putExtras(b);
                startActivity(i);


            }
            else
            {
                Intent i = new Intent(getApplicationContext(), AcceptMessageActivity.class);
                Bundle b = new Bundle();
                b.putString("message","something went wrong");
                b.putString("authUserName", authUserName);
                b.putString("pincode", pincodeA);
                i.putExtras(b);
                startActivity(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
