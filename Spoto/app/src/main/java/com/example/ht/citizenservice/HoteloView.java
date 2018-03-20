package com.example.ht.citizenservice;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HoteloView extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String hrating,hlat,hlong,hname,hurl,desc;
    Double latitude,longitude;
    TextView desc1,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotelo_view);

        Bundle b=getIntent().getExtras();

        hrating=b.getString("hrating");
        hlat=b.getString("hlat");
        hlong=b.getString("hlong");
        hname=b.getString("hname");
        desc=b.getString("desc");
        hurl=b.getString("hurl");



//        Log.i("des",desc);
//        Log.i("des",hurl);
        latitude = Double.parseDouble(hlat);
        longitude = Double.parseDouble(hlong);

        desc1=findViewById(R.id.des);
        url=findViewById(R.id.url);
        SpannableString content=new SpannableString("click here for more info..");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        url.setText(content);

        desc1.setText(desc);
        //url.setText(hurl);
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = hurl;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                //i.setType("text/plain");
                // i.putExtra(Intent.EXTRA_TEXT, "hi");
                // i.setPackage("com.olacabs.customer");
                startActivity(i);
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(hname));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((sydney),16));
    }
}
