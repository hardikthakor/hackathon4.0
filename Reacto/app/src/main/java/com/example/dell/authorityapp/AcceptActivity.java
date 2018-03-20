package com.example.dell.authorityapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.dell.authorityapp.ListView.MEDIA_TYPE;

public class AcceptActivity extends FragmentActivity implements OnMapReadyCallback, LoadImageTaskView.Listener {

    private GoogleMap mMap;
    double latitude,longitude;
    String authUserName,pincodeA, userid,type,timestamp,lat,lon,complaintTitle,imageurl,acceptance,location,description,deviceId,dt, pincode,issueDate,AJabberId,jabberId,timelineStatus,priority;
    ImageView loc_img;
    OkHttpClient client = new OkHttpClient();
    String DataPayload = "";
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;
    Response responseapi;
    Button btwd;
    String rese,resd;
    EditText edacc,eddate;
    String formattedDate;
    String date;
    String result, message;
    Bundle bundle;

    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        pincodeA = sharedpreferences.getString("pincode", null);
        authUserName=sharedpreferences.getString("authUserName",null);


        date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        edacc=findViewById(R.id.edupdes);
        //eddate=findViewById(R.id.eddate);
        btwd=findViewById(R.id.btwd);
        btwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
                    call();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        Bundle b=getIntent().getExtras();
        loc_img=findViewById(R.id.img);
        imageurl=b.getString("imageurl");
        complaintTitle=b.getString("complaintTitle");
        type=b.getString("type");
        timestamp=b.getString("timestamp");
        lat=b.getString("lat");
        lon=b.getString("lon");
        location=b.getString("location");
        pincode=b.getString("pincode");
        userid=b.getString("userId");
        issueDate=b.getString("issueDate");
       acceptance= b.getString("acceptance");
       jabberId= b.getString("jabberId");
       AJabberId=b.getString("AJabberId");
      priority=  b.getString("priority");
      description=  b.getString("description");
      timelineStatus=  b.getString("timelineStatus");
      deviceId=  b.getString("deviceId");
       // Toast.makeText(getApplicationContext(),pincode,Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplicationContext(),jabberId,Toast.LENGTH_LONG).show();



        latitude = Double.parseDouble(lat);
        longitude = Double.parseDouble(lon);
        imageFunction();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void call() {
        final String urli= "http://52.230.17.234:8011/accept";
        rese= edacc.getText().toString();
       // resd=eddate.getText().toString();
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                postdata = new JSONObject();
                try {
                    postdata.put("Status", "Accepted");
                    postdata.put("location", location);
                    postdata.put("pincode", pincode);
                    postdata.put("description",description);
                    postdata.put("userId",userid);
                    postdata.put("timestamp",timestamp);
                    postdata.put("type",type);
                    postdata.put("imageUrl",imageurl);
                    postdata.put("latitude",lat);
                    postdata.put("longitude",lon);
                    postdata.put("complaintTitle",complaintTitle);
                    postdata.put("issueDate",issueDate);
                    postdata.put("jabberId",jabberId);
                    postdata.put("AJabberId",AJabberId);
                    postdata.put("acceptance",acceptance);
                    postdata.put("priority",priority);
                    postdata.put("deviceId",deviceId);
                    postdata.put("acceptDate",date);
                    postdata.put("authUserName",authUserName);
                    postdata.put("timelineStatus","0");


                   // postdata.put("Reason:", rese);



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

                Log.i("---------",result);
                //Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();
               // Toast.makeText(getBaseContext(),result,Toast.LENGTH_LONG).show();
                // tv.setText(result);
                //bundle.putString("data", result);
//                Intent i=new Intent(getApplicationContext(),AcceptMessageActivity.class);
//                Bundle b= new Bundle();
//                b.putString("message",message);
//                b.putString("authUserName",authUserName);
//                b.putString("pincode",pincodeA);
//               i.putExtras(b);
//               startActivity(i);

               ApiSucess(result);

            }

        }.execute();

    }


    public void ApiSucess(String result){

        Log.i("--------res", result);

        try {
            Log.i("--------", "---------1");
            JSONObject jsonArray = new JSONObject(result);
            message = jsonArray.optString("msg");

            if (message.equals("Job has been Assigned")) {
               // Log.i("--------", "---------" + message);
               // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), AcceptMessageActivity.class);
                Bundle b = new Bundle();
                b.putString("message", message);
                b.putString("authUserName", authUserName);
                b.putString("pincode", pincodeA);
                i.putExtras(b);
                startActivity(i);
            }
            else {
                Intent i = new Intent(getApplicationContext(), AcceptMessageActivity.class);
                Bundle b = new Bundle();
                b.putString("message", "something went wrong!!!!");
                b.putString("authUserName", authUserName);
                b.putString("pincode", pincodeA);
                i.putExtras(b);
                startActivity(i);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng area = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(area).title("reported location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((area),16f));
    }

    public void imageFunction() {
        new LoadImageTaskView(this).execute(imageurl);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) throws JSONException, InterruptedException {
        loc_img.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {

    }
}
