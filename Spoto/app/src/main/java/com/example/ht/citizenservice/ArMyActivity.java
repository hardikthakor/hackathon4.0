package com.example.ht.citizenservice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.SensorDevice;
import com.maxst.ar.TrackerManager;

public class ArMyActivity extends ARActivity implements View.OnTouchListener, View.OnClickListener {

    private InstantTrackerRenderer instantTargetRenderer;
    private int preferCameraResolution = 0;
    private Button startTrackingButton;
    private GLSurfaceView glSurfaceView;
    ImageView img1, img2;

    // Location and Camera
    LocationManager locationManager;
    Double lat = 0.0, lon = 0.0;

    String dropLat, dropLong = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_my);

        img1 = findViewById(R.id.img);
        img2 = findViewById(R.id.img2);

        Log.d("----", "----------------");
        getLocation();

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackerManager.getInstance().findSurface();
                instantTargetRenderer.resetPosition();

                //dropoff[latitude]=26.92403&dropoff[longitude]=75.82673";
                dropLat = "26.92403";
                dropLong = "75.82673";

            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dropoff[latitude]=26.92482&dropoff[longitude]=75.82457";
                TrackerManager.getInstance().findSurface();
                instantTargetRenderer.resetPosition();

                dropLat = "26.92482";
                dropLong = "75.82457";
            }
        });

        startTrackingButton = (Button) findViewById(R.id.start_tracking);
        startTrackingButton.setOnClickListener(this);

        instantTargetRenderer = new InstantTrackerRenderer(this);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(instantTargetRenderer);
        glSurfaceView.setOnTouchListener(this);

        preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        glSurfaceView.onResume();
        SensorDevice.getInstance().start();
        TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_INSTANT);

        ResultCode resultCode = ResultCode.Success;

        switch (preferCameraResolution) {
            case 0:
                resultCode = CameraDevice.getInstance().start(0, 640, 480);
                break;

            case 1:
                resultCode = CameraDevice.getInstance().start(0, 1280, 720);
                break;
        }

        if (resultCode != ResultCode.Success) {
            Toast.makeText(this, R.string.camera_open_fail, Toast.LENGTH_SHORT).show();
            finish();
        }

        MaxstAR.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();

        TrackerManager.getInstance().quitFindingSurface();
        TrackerManager.getInstance().stopTracker();
        CameraDevice.getInstance().stop();
        SensorDevice.getInstance().stop();

        MaxstAR.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private static final float TOUCH_TOLERANCE = 5;
    private float touchStartX;
    private float touchStartY;
    private float translationX;
    private float translationY;

    @Override
    public boolean onTouch(View v, final MotionEvent event) {
        float x = event.getX();
        float y = event.getY();



        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.i("-----", "down");
                touchStartX = x;
                touchStartY = y;

                final float[] screen = new float[2];
                screen[0] = x;
                screen[1] = y;

                final float[] world = new float[3];

                TrackerManager.getInstance().getWorldPositionFromScreenCoordinate(screen, world);
                translationX = world[0];
                translationY = world[1];

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                Log.i("-----", "move");
                float dx = Math.abs(x - touchStartX);
                float dy = Math.abs(y - touchStartY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    touchStartX = x;
                    touchStartY = y;

                    final float[] screen = new float[2];
                    screen[0] = x;
                    screen[1] = y;

                    final float[] world = new float[3];

                    TrackerManager.getInstance().getWorldPositionFromScreenCoordinate(screen, world);
                    float posX = world[0];
                    float posY = world[1];

                    instantTargetRenderer.setTranslate(posX - translationX, posY - translationY);
                    translationX = posX;
                    translationY = posY;
                }
                break;
            }

            case MotionEvent.ACTION_UP:
                Log.i("-----", "up");
                String url = "https://m.uber.com/ul/?&action=setPickup&pickup[latitude]=" + lat + "&pickup[longitude]=" + lon + "&dropoff[latitude]=" + dropLat + "&dropoff[longitude]=" + dropLong;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                //i.setType("text/plain");
                // i.putExtra(Intent.EXTRA_TEXT, "hi");
                // i.setPackage("com.olacabs.customer");
                startActivity(i);
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_tracking:
                String text = startTrackingButton.getText().toString();
                if (text.equals(getResources().getString(R.string.start_tracking))) {
                    TrackerManager.getInstance().findSurface();
                    instantTargetRenderer.resetPosition();
                    startTrackingButton.setText(getResources().getString(R.string.stop_tracking));
                } else {
                    TrackerManager.getInstance().quitFindingSurface();
                    startTrackingButton.setText(getResources().getString(R.string.start_tracking));
                }
                break;
        }
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
        Log.d("----", String.valueOf(lat));

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
