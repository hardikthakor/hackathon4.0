package com.example.dell.authorityapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewLocation extends FragmentActivity implements OnMapReadyCallback, LoadImageTaskView.Listener {

    private GoogleMap mMap;
    double latitude,longitude;
    Geocoder geocoder;
    String userid,type,timestamp,lat,lon,complaintTitle,imageurl,tokenId,acceptance,proposedDate,location,description,deviceId,dt, pincode,issueDate,jabberId,AJabberId,timelineStatus,priority,photoUrl,emailId;
    ImageView loc_img;
    List<Address> addresses;
    TextView tvadd,tv3,proDate;
    ImageButton btnacc,btrej;
    String address;
    String postalCode;

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
        setContentView(R.layout.activity_view_location);

        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .build();

        //Bundle bundle=getIntent().getExtras();

        Bundle b=getIntent().getExtras();

        if (b != null) {

            if (b.getString("from").equals("values"))
            {
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
                AJabberId=b.getString("AJabberId");
                jabberId=b.getString("jabberId");
                tokenId=b.getString("tokenId");
                proposedDate=b.getString("proposedDate");
                   timelineStatus=b.getString("timelineStatus");
                description=b.getString("description");
                location=b.getString("location");
               // Toast.makeText(getApplicationContext(),jabberId,Toast.LENGTH_LONG).show();
                Log.i("------jabber",jabberId);

            }
            else {
                try {
                    // Toast.makeText(getApplicationContext(),b.toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),b.getString("data"),Toast.LENGTH_LONG).show();
                    dt = b.getString("data");
                    String[] sp = dt.split("=");
                    //Toast.makeText(getApplicationContext(),sp[1],Toast.LENGTH_LONG).show();
                    String s = sp[1];
                    String[] sp1 = s.split("\\}");
                    String se = sp1[0];
                    String sr = se.concat("}");
                    //Toast.makeText(getApplicationContext(),sr,Toast.LENGTH_LONG).show();
                    // JSONObject jsonObject = new JSONObject("sr");
                    //jsonObject.put("k", b.getString("locatioin"));
                    //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                    // String j = jsonObject.getString("k");
                    //Toast.makeText(getApplicationContext(),j,Toast.LENGTH_LONG).show();
                    //JSONObject sub1 = new JSONObject(jsonObject.getString("key_1"));
                    JSONObject sub = new JSONObject(sr);
                    //Toast.makeText(getApplicationContext(),sub.getString("imageUrl"),Toast.LENGTH_LONG).show();

//                title = sub.getString("complaintTitle");
//                String issueDate = sub.getString("issueDate");
//                userid = sub.getString("userId");
//                String timelineStatus = sub.getString("timelineStatus");
//                location = sub.getString("location");
//                type= sub.getString("type");
//                timestamp = sub.getString("timestamp");
//                String pincode = sub.getString("pincode");
//                String jabberId = sub.getString("jabberId");
//                lon = sub.getString("longitude");
//                imageurl= sub.getString("imageUrl");

//                lat = sub.getString("latitude");
//                pincode = sub.getString("pincode");
//                type= sub.getString("type");
//                timestamp = sub.getString("timestamp");
//                description = sub.getString("description");
//                userid = sub.getString("userId");
//                title = sub.getString("complaintTitle");
                    Log.i("----------json", sub.toString());
                    complaintTitle = sub.getString("complaintTitle");
                    acceptance = sub.getString("acceptance");
                    issueDate = sub.getString("issueDate");
                    location = sub.getString("location");
                    imageurl = sub.getString("imageUrl");
                    lat = sub.getString("latitude");
                    lon = sub.getString("longitude");
                    description = sub.getString("description");
                    deviceId = sub.getString("deviceId");
                    pincode = sub.getString("pincode");
                    jabberId = sub.getString("jabberId");
                    priority = sub.getString("priority");
                    timelineStatus = sub.getString("timelineStatus");
                    userid = sub.getString("userId");
                    type = sub.getString("type");
                    timestamp = sub.getString("timestamp");
                    proposedDate=sub.getString("proposedDate");
                   // Toast.makeText(getApplicationContext(),description,Toast.LENGTH_LONG).show();

                    //Toast.makeText(getApplicationContext(),imageurl,Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),deviceId,Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),jabberId,Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),pincode,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            /*title="garbage-1518157814379";
            userid="hardikthakor.ht@gmail.com";
            type="garbage";
            timestamp="1518157814379";
            imageurl="https://citizenserviceapp2.s3.amazonaws.com:443/garbage-7ISVj9huSV.jpg";
            acceptance="notAccepted";
            description="hfhf";
            location="Old Palghar - Manor Rd, Dungipada, Vevoor, Palghar, Maharashtra 401404, India";
            latitude = 19.70650;
            longitude = 72.78419;*/

            /*
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(b.getString("data"));
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    title = jsonObject1.getString("complaintTitle");
                    String issueDate = jsonObject1.getString("issueDate");
                    userid = jsonObject1.getString("userId");
                    String timelineStatus = jsonObject1.getString("timelineStatus");
                    location = jsonObject1.getString("location");
                    type= jsonObject1.getString("type");
                    timestamp = jsonObject1.getString("timestamp");
                    String pincode = jsonObject1.getString("pincode");
                    String jabberId = jsonObject1.getString("jabberId");
                    lon = jsonObject1.getString("longitude");
                    imageurl= jsonObject1.getString("imageUrl");
                    String description = jsonObject1.getString("description");
                    lat = jsonObject1.getString("latitude");
                    acceptance = jsonObject1.getString("acceptance");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
        }
        else {
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
        }

        loc_img=findViewById(R.id.img);
        tvadd=findViewById(R.id.tvadd);
        //proDate=findViewById(R.id.prodate);
        tv3=findViewById(R.id.tv3);
        btnacc=findViewById(R.id.imv);
        btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();


            }
        });

        btrej=findViewById(R.id.imv1);
        btrej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),RejectActivity.class);
                Bundle b= new Bundle();
                b.putString("imageurl",imageurl);
                b.putString("complaintTitle",complaintTitle);
                b.putString("type",type);
                b.putString("timestamp",timestamp);
                b.putString("lat",lat);
                b.putString("lon",lon);
                b.putString("location",location);
                b.putString("pincode",pincode);
                b.putString("userId",userid);
                b.putString("issueDate",issueDate);
                b.putString("acceptance",acceptance);
                b.putString("jabberId",jabberId);
                b.putString("priority",priority);
                b.putString("description",description);
                b.putString("timelineStatus",timelineStatus);
                b.putString("deviceId",deviceId);


                i.putExtras(b);
                startActivity(i);
            }
        });


        //Log.d("-------------", imageurl);

       latitude = Double.parseDouble(lat);
       longitude = Double.parseDouble(lon);
        //latitude = 19.70650;
        //longitude = 72.78419;
        tvadd.setText(location);
        tv3.setText(description);
       // proDate.setText(proposedDate);

        imageFunction();
        //Toast.makeText(getApplicationContext(), b.getString("title"),Toast.LENGTH_LONG).show();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tvadd.setText(location);
       // Toast.makeText(getApplicationContext(),location,Toast.LENGTH_LONG).show();
/*
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            location = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            //Toast.makeText(getApplicationContext(), postalCode, Toast.LENGTH_LONG).show();
           // tvadd.setText(location);

        } catch (IOException e) {
            e.printStackTrace();
        }*/


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
        LatLng area = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(area).title("reported location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(area));
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

    public void callApi() {
        final String url = "http://52.230.17.234:8123/check";

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
                    postdata.put("userId",userid);
                    postdata.put("timestamp",timestamp);
                    postdata.put("type",type);
                    postdata.put("imageurl",imageurl);
                    postdata.put("latitude",lat);
                    postdata.put("longitude",lon);
                    postdata.put("complaintTitle",complaintTitle);
                    postdata.put("issueDate",issueDate);
                    postdata.put("jabberId",jabberId);
                    postdata.put("AJabberId",AJabberId);
                    postdata.put("acceptance",acceptance);
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
                //Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();
                ApiSucess(result);
                //tv.setText(result);
            }

        }.execute();
    }

    public void ApiSucess(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("msg").equals("ok")) {
                Intent i=new Intent(getApplicationContext(),AcceptActivity.class);
                Bundle b= new Bundle();
                b.putString("imageurl",imageurl);
                b.putString("complaintTitle",complaintTitle);
                b.putString("type",type);
                b.putString("timestamp",timestamp);
                b.putString("lat",lat);
                b.putString("lon",lon);
                b.putString("location",location);
                b.putString("pincode",pincode);
                b.putString("userId",userid);
                b.putString("issueDate",issueDate);
                b.putString("acceptance",acceptance);
                b.putString("jabberId",jabberId);
                b.putString("AJabberId",AJabberId);
                b.putString("priority",priority);
                b.putString("description",description);
                b.putString("timelineStatus",timelineStatus);
                b.putString("deviceId",deviceId);
                i.putExtras(b);
                startActivity(i);
            }
            else if (jsonObject.getString("msg").equals("notOk")) {

            }
            else
                Toast.makeText(getApplicationContext(),"msg not found", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
