package com.example.dell.authorityapp;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.dell.authorityapp.ListView.MEDIA_TYPE;

public class cameralayout extends AppCompatActivity {
    String filename="";
    long localTimestamp;
    String localTimestampstr;
    OkHttpClient client = new OkHttpClient();
    String DataPayload = "";
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;
    Response responseapi;

    LocationManager locationManager;
    Double lat = 0.0, lon = 0.0;
    //static final int CAM_REQUEST = 1;
    String urlImg_json = "";
    String payLoad_json = "error";
   // File file;
    //String filename = "";
    //long localTimestamp;
    //String localTimestampstr = "";
    Bitmap bitmap;
    TextView tv;
    //String type;
    File file,folder ;
    Bitmap bmp;
    ImageView clrloc_img;
    Button upload;
    String date,longitude1,userId,imageUrl,type,latitude1,deviceId,timestamp,complaintTitle,issueDate,pincode,location,priority,photoUrl,emailId,AJabberId,jabberId,tokenId,proposedDate,timelineStatus,description;
    String imgString,message;
    static final int CAM_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameralayout);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        clrloc_img=findViewById(R.id.clrloc_img);
        tv=findViewById(R.id.tvloc);
        upload=findViewById(R.id.upload);
        date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

        Bundle b= getIntent().getExtras();
        longitude1=b.getString("longitude");
        userId=b.getString("userId");
        imageUrl=b.getString("imageUrl");
        type=b.getString("type");
       latitude1=b.getString("latitude");
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
//        Log.i("----lat",latitude);
//        Log.i("----lon",longitude);
        String s= "Authority";
        String se= s.concat("-");
        String se1=se.concat(complaintTitle);
        String se2=se1.concat("-");
        String se3= se2.concat(date);
        filename=se3.concat(".jpg");
        folder = new File(Environment.getExternalStorageDirectory() + "/auth/");
       file = new File(Environment.getExternalStorageDirectory() + "/auth/", filename);
        if(! folder.exists()){
            folder.mkdir();
        }
        getLocation();

        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cam.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        cam.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(cam, CAM_REQUEST);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();

            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            //Toast.makeText(getBaseContext(),"fail",Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == CAM_REQUEST) {
//           // SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putString("filename", filename);
//            editor.putString("type", type);
//            editor.putString("localTimestampstr", localTimestampstr);
//            editor.commit();
//            Intent i = new Intent(getBaseContext(), .class);
//            startActivity(i);
            //Toast.makeText(getBaseContext(), type + " Card", Toast.LENGTH_SHORT).show();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bmp = BitmapFactory.decodeFile(file.toString(), options);
            imgString = Base64.encodeToString(getBytesFromBitmap(bmp),Base64.NO_WRAP);


            clrloc_img.setImageBitmap(bmp);
            //tv.setText("Latitude :"+lat+" Longitude :"+lon);

        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public void call() {
        final String urli= "http://52.230.17.234:8887/imageComp";
        //rese= edacc.getText().toString();
        // resd=eddate.getText().toString();
        String lati = Double.toString(lat);
        String longo = Double.toString(lon);
        lati = lati.substring(0,5);
        longo = longo.substring(0,5);
        Log.i("---lati",lati);
        Log.i("---longo",longo);
        final String finalLongo = longo;
        final String finalLati = lati;
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                postdata = new JSONObject();
                try {
                    postdata.put("Alatitude", finalLati.toString());
                    postdata.put("Alongitude", finalLongo.toString());
                    postdata.put("latitude",latitude1.toString());
                    postdata.put("longitude",longitude1.toString());
                    postdata.put("AimageUrl",imgString);
                    postdata.put("priority",priority);
                    postdata.put("deviceId",deviceId);
                    postdata.put("timelineStatus",timelineStatus);
                    postdata.put("pincode", pincode);
                    postdata.put("description",description);
                    postdata.put("userId",userId);
                    postdata.put("timestamp",timestamp);
                    postdata.put("type",type);
                    postdata.put("imageUrl",imageUrl);
                    postdata.put("complaintTitle",complaintTitle);
                    postdata.put("issueDate",issueDate);
                    postdata.put("jabberId",jabberId);
                    postdata.put("AJabberId",AJabberId);
                    postdata.put("authUserName","Prashobh");


                    // postdata.put("Reason:", rese);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("-----pd",postdata.toString());

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
                //Toast.makeText(getBaseContext(),result,Toast.LENGTH_LONG).show();
                // tv.setText(result);
                //bundle.putString("data", result);
//                Intent i=new Intent(getApplicationContext(),AcceptMessageActivity.class);
//                Bundle b= new Bundle();
//                b.putString("message","sussessfully uploaded");
//                i.putExtras(b);
//                startActivity(i);
                ApiSucess(result);

            }

        }.execute();

    }

    public void ApiSucess(String result) {
        Log.i("---res",result);

        try {
            //JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonArray = new JSONObject(result);
            message = jsonArray.optString("flag");
            if (message.equals("success")) {
                Log.i("--------", "---------" + message);
                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), AcceptMessageActivity.class);
                Bundle b = new Bundle();
                b.putString("message", message);
                b.putString("alert","Photo uploaded ,Further Details will be proceeded to higher authority.");
                b.putString("authUserName", "Prashobh");
                b.putString("pincode", "302001");
                i.putExtras(b);
                startActivity(i);
            }
            else {

                Intent i = new Intent(getApplicationContext(), AcceptMessageActivity.class);
                Bundle b = new Bundle();
                b.putString("message","You are resolving a different issue");
                b.putString("picode", "302001");
                b.putString("authUserName", "Prashobh");
                i.putExtras(b);
                startActivity(i);
            }

              //  Toast.makeText(getApplicationContext(),"msg not found", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Toast.makeText(getBaseContext(), "lo", Toast.LENGTH_LONG).show();
        //tv.setText("in");
        //tv.setText("before");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new Listener());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new Listener());
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null) {
            handleLatLng(location.getLatitude(), location.getLongitude());
        }
    }

    //set lat lon
    private void handleLatLng(final double latitude, final double longitude)  {
        lat = latitude;
        lon = longitude;
        //Toast.makeText(getBaseContext(),"ok",Toast.LENGTH_LONG).show();
        tv.setText("Latitude :"+latitude+" Longitude :"+longitude);


        Log.i("TAG----------------", "(" + latitude + "," + longitude + ")");
    }

    //Listener
    private class Listener implements LocationListener {
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            handleLatLng(latitude, longitude);
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }



}
