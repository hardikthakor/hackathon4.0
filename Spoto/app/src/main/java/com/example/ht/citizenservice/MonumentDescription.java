package com.example.ht.citizenservice;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MonumentDescription extends AppCompatActivity implements OnMapReadyCallback{

    TextView descriptionTv, timingTv;

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    String monumentSelected = "", markerName = "";
    String descrpition = "", timing = "", latitudeStr = "", longitudeStr = "";
    double latitude = 0.0, longitude = 0.0, currentLat = 0.0, currentLong = 0.0;

    LocationManager locationManager;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_description);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        monumentSelected = sharedpreferences.getString("monumentSelected", null);

        descriptionTv = findViewById(R.id.description);
        timingTv = findViewById(R.id.timeloc);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/amitaregular.ttf");
        descriptionTv.setTypeface(typeface);

        if (monumentSelected.equals("1")) {
            descrpition = getString(R.string.des1);
            timing = getString(R.string.time1);
            latitudeStr = getString(R.string.latitude1);
            longitudeStr = getString(R.string.longitude1);
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);
            descriptionTv.setText(descrpition);
            timingTv.setText(timing);
            markerName = getString(R.string.title1);
            getSupportActionBar().setTitle(getString(R.string.title1));
        }
        else if (monumentSelected.equals("2")) {
            descrpition = getString(R.string.des2);
            timing = getString(R.string.time2);
            latitudeStr = getString(R.string.latitude2);
            longitudeStr = getString(R.string.longitude2);
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);
            descriptionTv.setText(descrpition);
            timingTv.setText(timing);
            markerName = getString(R.string.title2);
            getSupportActionBar().setTitle(getString(R.string.title2));
        }
        else if (monumentSelected.equals("3")) {
            descrpition = getString(R.string.des3);
            timing = getString(R.string.time3);
            latitudeStr = getString(R.string.latitude3);
            longitudeStr = getString(R.string.longitude3);
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);
            descriptionTv.setText(descrpition);
            timingTv.setText(timing);
            markerName = getString(R.string.title3);
            getSupportActionBar().setTitle(getString(R.string.title3));
        }
        else if (monumentSelected.equals("4")) {
            descrpition = getString(R.string.des4);
            timing = getString(R.string.time4);
            latitudeStr = getString(R.string.latitude4);
            longitudeStr = getString(R.string.longitude4);
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);
            descriptionTv.setText(descrpition);
            timingTv.setText(timing);
            markerName = getString(R.string.title4);
            getSupportActionBar().setTitle(getString(R.string.title4));
        }
        else if (monumentSelected.equals("5")) {
            descrpition = getString(R.string.des5);
            timing = getString(R.string.time5);
            latitudeStr = getString(R.string.latitude5);
            longitudeStr = getString(R.string.longitude5);
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);
            descriptionTv.setText(descrpition);
            timingTv.setText(timing);
            markerName = getString(R.string.title5);
            getSupportActionBar().setTitle(getString(R.string.title5));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.ridemenu,m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String url;
        Intent i;
        switch (item.getItemId()) {
            case R.id.ola:
                //currentLat = Double.parseDouble(getString(R.string.latitude1));
                //currentLong = Double.parseDouble(getString(R.string.longitude1));
                url = "http://book.olacabs.com/?lat=" + currentLat + "&lng=" + currentLong + "&landing_page=bk&bk_act=rn&drop_lat=" + latitudeStr + "&drop_lng=" + longitudeStr + "&dsw=yes";
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                //i.setType("text/plain");
                // i.putExtra(Intent.EXTRA_TEXT, "hi");
                // i.setPackage("com.olacabs.customer");
                startActivity(i);
                return true;
            case R.id.uber:
                //currentLat = Double.parseDouble(getString(R.string.latitude1));
                //currentLong = Double.parseDouble(getString(R.string.longitude1));
                url = "https://m.uber.com/ul/?&action=setPickup&pickup[latitude]=" + currentLat + "&pickup[longitude]=" + currentLong + "&dropoff[latitude]=" + latitudeStr + "&dropoff[longitude]=" + longitudeStr;
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                //i.setType("text/plain");
                // i.putExtra(Intent.EXTRA_TEXT, "hi");
                // i.setPackage("com.olacabs.customer");
                startActivity(i);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng area = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(area).title(markerName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((area),16f));

        getLocation();
    }

    //getLocation
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new MonumentDescription.Listener());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new MonumentDescription.Listener());
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
        currentLat = latitude;
        currentLong = longitude;
        //Toast.makeText(getBaseContext(),"ok",Toast.LENGTH_LONG).show();
        /*
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                tv.setText("Latitude :"+latitude+" Longitude :"+longitude);
            }
        });
        */

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
    //getLocation End
}
