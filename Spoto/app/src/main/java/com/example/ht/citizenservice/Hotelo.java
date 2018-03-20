package com.example.ht.citizenservice;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Hotelo extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    String hrating,hname,hlat,hlong,desc,hurl,location,postalCode,city;
    Request request;
    Response responseapi;
    String DataPayload = "";
    TextView hotelTitle,sever;
    Geocoder geocoder;
    Double lat=0.0,lon=0.0,latitude,longitude;
    LocationManager locationManager;
    List<Address> addresses;
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    RecyclerView recyclerView;
    int count1=0;
    ArrayList<HoteloValues> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotelo);

        recyclerView=findViewById(R.id.recycler_view);

        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .build();

        //hotelTitle=findViewById(R.id.hotelTitle);
        //sever=findViewById(R.id.sever);

        //list.add(new values("abc","habx","smcslm","cnkdxznck"));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(new ValuesAdapter(this, list));

        // geocoder = new Geocoder(this, Locale.getDefault());



        getLocation();
        getcity();

        callapi();

    }

    private void callapi() {
        final String url = "http://52.187.6.152:8014/hotelInfo";

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                postdata=new JSONObject();

                try {
                    postdata.put("city",city);
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
                ApiSucess(result);
                //tv.setText(result);
            }

        }.execute();
    }

    public void  ApiSucess(String result)
    {
        try {
            JSONArray jsonArray = new JSONArray(result);

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hrating = jsonObject1.optString("hrating");
                hlat = jsonObject1.optString("hlat");
                hlong = jsonObject1.optString("hlong");
                hname = jsonObject1.optString("hname");
                desc=jsonObject1.optString("desc");
                hurl=jsonObject1.optString("hurl");

                Double rating = Double.valueOf(hrating);
                rating = rating/2;

                hrating = String.valueOf(rating);

                list.add(new HoteloValues(hlat, hlong, hname, hrating,desc,hurl));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new HoteloAdapter(this, list));
            }
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
    private void handleLatLng(final double latitude, final double longitude)  {
        lat = latitude;
        lon = longitude;
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

    private void getcity()
    {

        geocoder = new Geocoder(this, Locale.getDefault());

        try {

            addresses = geocoder.getFromLocation(lat, lon, 1);
            location = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            //String state = addresses.get(0).getAdminArea();
            //String country = addresses.get(0).getCountryName();
            //postalCode = addresses.get(0).getPostalCode();
            //String knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
