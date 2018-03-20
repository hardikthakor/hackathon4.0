package com.example.ht.citizenservice;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CameraActv extends AppCompatActivity implements View.OnClickListener {

    // Views
    Button bt;
    ImageView im;
    TextView tv, tv1;
    EditText des;

    //ProgressDialog dialog;

    // Location and Camera
    LocationManager locationManager;
    Double lat = 0.0, lon = 0.0;
    static final int CAM_REQUEST = 1;
    String urlImg_json = "";
    String payLoad_json = "error";
    File file;
    String filename = "";
    //long localTimestamp;
    String localTimestampstr = "";
    Bitmap bitmap;

    // postalCode
    Geocoder geocoder;
    List<Address> addresses;
    String address = "";
    String postalCode = "";
    String city = "";

    // FireBase
    String token = "";

    // Type of Request
    String type = "";

    // api
    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    // s3
    BasicAWSCredentials basicAWSCredentials;
    AmazonS3Client amazonS3Client;
    PutObjectRequest putObjectRequest;
    PutObjectResult response;
    String imgUrltojson = "";
    String bucketName = "mastekproject";
    String filenames3 = "";

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;
    String profileemail = "";
    String jabberId = "";
    String description = "";

    StyleableToast styleableToast, styleableToast1;

    // byte array
    Bitmap bmp;
    String imgString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_actv);

        // s3 Create Bucket Problem
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Cam for Nougat above API 24
        /*
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        */

        /*
        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .build();
        */

        //dialog = ProgressDialog.show(this, getResources().getString(R.string.progressTitle), getResources().getString(R.string.progressMsg), true);


        styleableToast = new StyleableToast
                .Builder(CameraActv.this)
                .duration(Toast.LENGTH_LONG)
                .icon(R.drawable.camera_ic_autorenew_black_24dp)
                .spinIcon()
                .text("Uploading")
                .textColor(Color.parseColor("#FFDA44"))
                .textBold()
                .cornerRadius(5)
                .build();

        styleableToast1 = new StyleableToast
                .Builder(CameraActv.this)
                .duration(Toast.LENGTH_LONG)
                .text("Uploading")
                .textColor(Color.parseColor("#FFDA44"))
                .textBold()
                .cornerRadius(5)
                .build();


        bt = (Button) findViewById(R.id.bt);
        tv = (TextView) findViewById(R.id.tv);
        im = (ImageView) findViewById(R.id.im);
        tv1 = findViewById(R.id.tv1);
        des = findViewById(R.id.des);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/REFSAN.TTF");
        des.setTypeface(typeface);
        tv1.setTypeface(typeface);

        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .build();

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("tokenFirebase", null);
        profileemail = sharedpreferences.getString("email", null);
        filename = sharedpreferences.getString("filename", null);
        type = sharedpreferences.getString("type", null);
        localTimestampstr = sharedpreferences.getString("localTimestampstr", null);
        jabberId = sharedpreferences.getString("jabberId", null);

        filenames3 = profileemail + "/" + filename;

        /*`
        // set clicked image
        file = new File(Environment.getExternalStorageDirectory() + "/CitizenService/", filename);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        bmp = BitmapFactory.decodeFile(file.toString(), options);
        //bmp = getResizedBitmap(bmp,10000);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
        try {
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
            fOut.flush(); // Not really required
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        im.setImageBitmap(bmp);
        */

        bt.setOnClickListener(this);

        //getLocation();
        //getPincode();
        callLocation();

    }

    //getLocation
    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Toast.makeText(getBaseContext(), "lo", Toast.LENGTH_LONG).show();
        //tv.setText("in");
        //tv.setText("before");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                tv.setText("Latitude :"+latitude+" Longitude :"+longitude);
            }
        });

        //Log.i("TAG----------------", "(" + latitude + "," + longitude + ")");
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
    //getLocation End

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                bt.setClickable(false);
                //Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    bt.setTextColor(getColor(R.color.black));
                }
                styleableToast.show();
                //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                String lati = Double.toString(lat);
                String longo = Double.toString(lon);
                lati = lati.substring(0,7);
                longo = longo.substring(0,7);
                byteConvert();
                //uploadingS3();
                description = des.getText().toString();
                callApi(lati, longo, profileemail, type, localTimestampstr, imgUrltojson, token, postalCode, imgString, jabberId, address, description);
                break;
        }
    }

    public void byteConvert () {
        imgString = Base64.encodeToString(getBytesFromBitmap(bmp),Base64.NO_WRAP);
        //Toast.makeText(getBaseContext(),imgString, Toast.LENGTH_SHORT).show();
        //Log.d("bitmap------------", imgString);
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    private void getPincode() {
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lon, 10);
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            postalCode = addresses.get(0).getPostalCode();
            //city = addresses.get(0).getLocality();
            //String state = addresses.get(0).getAdminArea();
            //String country = addresses.get(0).getCountryName();
            //String knownName = addresses.get(0).getFeatureName();
            //Log.i("------add", addresses.toString());

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tv1.setText(address);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void uploadingS3() {
        // B acc
        // basicAWSCredentials = new BasicAWSCredentials("AKIAJEX2JOEMTR6E2UPQ", "qF4c61hpMyaPx+veQFWQQIMnLwMabIK+a2CUym2S");
        // M acc
        basicAWSCredentials = new BasicAWSCredentials("AKIAIZ2FYUKNDQAH6WUQ", "dS3gWvGlxjxrFJpbH3R9JdYKfIZksc3ZMgzmqIcE");
        amazonS3Client = new AmazonS3Client(basicAWSCredentials);
        //amazonS3Client.createBucket(bucketName);
        putObjectRequest = new PutObjectRequest(bucketName, filenames3, file).withCannedAcl(CannedAccessControlList.PublicReadWrite);
        response = amazonS3Client.putObject(putObjectRequest);
        //Toast.makeText(getBaseContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
        imgUrltojson = amazonS3Client.getResourceUrl(bucketName, filenames3);
    }

    //callApi
    public void callApi(final String imgLatitude, final String imgLongitude, final String userIdNo, final String typeOfReport, final String imgTimestamp, String imgUrltojson, final String token, String postalCode, final String imgString, final String jabberId, final String city, final String description) {
        //Enter the URL here
        //final String url = "http://" + getString(R.string.mongoIp) + ":8004/insertion";
        String url = getString(R.string.allUrl);

        // only
        // http://52.230.17.234:8778/detect

        /*
        if (type.equals("pothole")) {
            url = getString(R.string.camactvpot);
            //Log.i("----------url", url);
        }
        else {
            url = getString(R.string.camactvgaric);
            //Log.i("----------url", url);
        }
        */

        if (postalCode == null) {
            postalCode = "302001";
            //Log.i("----------url", postalCode);

        }
        else if (postalCode.equals("")) {
            postalCode = "302001";
            //Log.i("----------url", postalCode);
        }

        //Log.d("----", typeOfReport);

        final String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

        final String finalUrl = url;
        final String finalPostalCode = postalCode;

        //Log.d("----", finalUrl);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                postdata = new JSONObject();
                try {
                    postdata.put("longitude", imgLongitude);
                    postdata.put("latitude", imgLatitude);
                    postdata.put("userId", userIdNo);
                    postdata.put("timestamp", imgTimestamp);
                    postdata.put("imageUrl", imgString);
                    postdata.put("type", typeOfReport);
                    postdata.put("deviceId", token);
                    postdata.put("pincode", "302001");
                    postdata.put("jabberId", jabberId + "@localhost");
                    postdata.put("timelineStatus", "0");
                    postdata.put("complaintTitle", type + "-" + imgTimestamp);
                    postdata.put("issueDate", date);
                    postdata.put("location", city);
                    postdata.put("description", description);
                    postdata.put("acceptance", "notAccepted");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.d("------------", postdata.toString());

                body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                request = new Request.Builder()
                        .url(finalUrl)
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
                /*
                styleableToast1 = new StyleableToast
                        .Builder(CameraActv.this)
                        .duration(Toast.LENGTH_SHORT)
                        .text("Res : " + result)
                        .textColor(Color.parseColor("#FFDA44"))
                        .textBold()
                        .cornerRadius(5)
                        .build();
                styleableToast1.show();
                */

                ApiSucess(result);
            }

        }.execute();

        /*
        AsyncHttpClient client = new AsyncHttpClient();

        JSONObject pothole = new JSONObject();
        try {

            pothole.put("lattitude", imgLatitude);
            pothole.put("longitude", imgLongitude);
            pothole.put("userId", userIdNo);
            pothole.put("type", typeOfReport);
            pothole.put("timestamp", imgTimestamp);
            pothole.put("imageUrl", imgURL);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(pothole.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post( getApplicationContext(),url, entity, "application/json", new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(new String(responseBody));

                    urlImg_json = obj.get("imgUrl").toString();
                    payLoad_json = obj.get("payLoad").toString();
                    ApiSucess();
                    //Toast.makeText(getBaseContext(),payLoad_json,Toast.LENGTH_SHORT).show();
                    //To display
                    //resJson.setText(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(CameraActv.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(CameraActv.this, "Api Failed to Load", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    //ApiSucess
    private void ApiSucess(String result) {
        jobj = null;
        //Log.d("----", result);

        if (result == null){
            //Log.i("----------","failed");
            //Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_SHORT).show();
        }
        else {
            //Log.i("-------", result);
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
        }
        try {
            jobj = new JSONObject(result);
            payLoad_json = jobj.get("payLoad").toString();
            urlImg_json = jobj.get("imageUrl").toString();

            //Log.i("----------", payLoad_json);
            //Log.i("----------", urlImg_json);
            //Toast.makeText(getBaseContext(),"Res : " + payLoad_json,Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(),"Res : " + urlImg_json,Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Delete if Not Valid s3
        if(payLoad_json.equals("erro")) {
            amazonS3Client.deleteObject(bucketName, filenames3);
            //Toast.makeText(getBaseContext(),"Deleted",Toast.LENGTH_SHORT).show();
        }

        //dialog.cancel();

        Intent i = new Intent(getBaseContext(), ImageUploadResponse.class);
        Bundle b = new Bundle();
        b.putString("imgUrl", urlImg_json);
        b.putString("payLoad", payLoad_json);
        //b.putString("latitude", lati);
        i.putExtras(b);
        startActivity(i);
        bt.setClickable(true);
        finish();
    }

    public void callLocation() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        im.setImageBitmap(bmp);
                        getLocation();
                        getPincode();
                    }
                });

                file = new File(Environment.getExternalStorageDirectory() + "/CitizenService/", filename);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                bmp = BitmapFactory.decodeFile(file.toString(), options);
                //bmp = getResizedBitmap(bmp,10000);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                try {
                    OutputStream fOut = null;
                    fOut = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
                    fOut.flush(); // Not really required
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        im.setImageBitmap(bmp);
                    }
                });

                return null;
            }
        }.execute();
    }
}
