package com.example.dell.authorityapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewLocationAssigned extends FragmentActivity implements OnMapReadyCallback,LoadImageTaskView.Listener {

    private GoogleMap mMap;
    String longitude,userId,imageUrl,type,latitude,deviceId,timestamp,complaintTitle,issueDate,pincode,location,priority,photoUrl,emailId,AJabberId,jabberId,tokenId,proposedDate,timelineStatus,description;
    double longitude1,latitude1;
    Button btnupdate,btncamera;
    ImageView loc_imgA;
    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location_assigned);
        btncamera=findViewById(R.id.btncamera);
        btnupdate=findViewById(R.id.btnupdate);
        loc_imgA=findViewById(R.id.loc_imgA);
        Bundle b= getIntent().getExtras();
        longitude=b.getString("lon");
        userId=b.getString("userId");
        imageUrl=b.getString("imageUrl");
        type=b.getString("type");
        latitude=b.getString("lat");
        deviceId=b.getString("deviceId");
        timestamp=b.getString("timestamp");
        complaintTitle=b.getString("complaintTitle");
        issueDate=b.getString("issueDate");
        pincode=b.getString("pincode");
        location=b.getString("location");
        priority=b.getString("priority");
        photoUrl=b.getString("photoUrl");
        emailId=b.getString("emailId");
        AJabberId=b.getString("AJabberId");
        jabberId=b.getString("jabberId");
        tokenId=b.getString("tokenId");
        proposedDate=b.getString("proposedDate");
        timelineStatus=b.getString("timelineStatus");
        description=b.getString("description");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        latitude1 = Double.parseDouble(latitude);
        longitude1 = Double.parseDouble(longitude);
        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),cameralayout.class);
                Bundle b=new Bundle();
                b.putString("longitude",longitude);
                b.putString("latitude",latitude);
                b.putString("userId",userId);
                b.putString("imageUrl",imageUrl);
                b.putString("type",type);
                b.putString("deviceId",deviceId);
                b.putString("timestamp",timestamp);
                b.putString("complaintTitle",complaintTitle);
                b.putString("issueDate",issueDate);
                b.putString("pincode",pincode);
                b.putString("location",location);
                b.putString("priority",priority);
                b.putString("photoUrl",photoUrl);
                b.putString("emailId",emailId);
                b.putString("AJabberId",AJabberId);
                b.putString("jabberId",jabberId);
                b.putString("tokenId",tokenId);
                b.putString("proposedDate",proposedDate);
                b.putString("timlineStatus",timelineStatus);
                b.putString("description",description);
                i.putExtras(b);
                startActivity(i);
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        imageFunction();
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
                Intent i=new Intent(getApplicationContext(),AcceptMessageActivity.class);
                Bundle b= new Bundle();
                b.putString("message","updated");
                b.putString("authUserName","Prashobh");
                b.putString("pincode","302001");
                i.putExtras(b);
                startActivity(i);
            }
        });

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng area = new LatLng(latitude1, longitude1);
        mMap.addMarker(new MarkerOptions().position(area).title("reported location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((area),16f));
    }

    public void imageFunction() {
        new LoadImageTaskView(this).execute(imageUrl);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) throws JSONException, InterruptedException {
        loc_imgA.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {

    }

    public void callApi() {
        final String url = "http://52.230.17.234:8130/timeline";

        Log.i("---------pin", pincode);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                //body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                postdata = new JSONObject();

                try {
                    postdata.put("location", location);
                    postdata.put("pincode", pincode);
                    postdata.put("description",description);
                    postdata.put("userId",userId);
                    postdata.put("timestamp",timestamp);
                    postdata.put("type",type);
                    postdata.put("imageUrl",imageUrl);
                    postdata.put("latitude",latitude);
                    postdata.put("longitude",longitude);
                   postdata.put("complaintTitle",complaintTitle);
                    postdata.put("issueDate",issueDate);
                    postdata.put("jabberId",jabberId);
                    postdata.put("AJabberId",AJabberId);
                   // postdata.put("acceptance",acceptance);
                    postdata.put("priority",priority);
                    postdata.put("deviceId",deviceId);
                    postdata.put("timelineStatus",timelineStatus);
                    Log.i("----------j", postdata.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                body = RequestBody.create(MEDIA_TYPE, postdata.toString());
                request = new Request.Builder()
                        .url(url)
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
                Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();
                //ApiSucess(result);
                //tv.setText(result);
            }

        }.execute();
    }



//    public void ApiSucess(String result) {
//
//        try {
//            JSONObject jsonObject = new JSONObject(result);
//            if (jsonObject.getString("msg").equals("ok")) {
//                Intent i=new Intent(getApplicationContext(),AcceptActivity.class);
//                Bundle b= new Bundle();
//                b.putString("imageurl",imageUrl);
//                b.putString("complaintTitle",complaintTitle);
//                b.putString("type",type);
//                b.putString("timestamp",timestamp);
//                b.putString("lat",latitude);
//                b.putString("lon",longitude);
//                b.putString("location",location);
//                b.putString("pincode",pincode);
//                b.putString("userId",userId);
//                b.putString("issueDate",issueDate);
//               // b.putString("acceptance",acceptance);
//                b.putString("jabberId",jabberId);
//                b.putString("AJabberId",AJabberId);
//                b.putString("priority",priority);
//                b.putString("description",description);
//                b.putString("timelineStatus",timelineStatus);
//                b.putString("deviceId",deviceId);
//                i.putExtras(b);
//                startActivity(i);
//            }
//            else if (jsonObject.getString("msg").equals("notOk")) {
//
//            }
//            else
//                Toast.makeText(getApplicationContext(),"msg not found", Toast.LENGTH_LONG).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }

}
