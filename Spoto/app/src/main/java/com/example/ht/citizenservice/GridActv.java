package com.example.ht.citizenservice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class GridActv extends AppCompatActivity implements View.OnClickListener{

    CardView potholecard, garbagecard, complaintscard;
    File folder;
    String token;

    //Camera
    String filename;
    long localTimestamp;
    String localTimestampstr;
    String type;
    File file;
    static final int CAM_REQUEST = 1;

    // Permission
    private static final int REQ_PER_CODE = 901;

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_actv);

        // Cam for Nougat above API 24
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //Bundle b = getIntent().getExtras();
        //token = b.getString("token");

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("tokenFirebase", null);

        potholecard = (CardView) findViewById(R.id.potholecard);
        garbagecard = (CardView) findViewById(R.id.garbagecard);
        complaintscard = (CardView) findViewById(R.id.complaintscard);

        potholecard.setOnClickListener(this);
        garbagecard.setOnClickListener(this);
        complaintscard.setOnClickListener(this);

        folder = new File(Environment.getExternalStorageDirectory() + "/CitizenService/");

        if(! folder.exists()){
            folder.mkdir();
        }

        //width height
        /*
        Display dis = getWindowManager().getDefaultDisplay();
        Point pt = new Point();
        dis.getRealSize(pt);
        Integer width = pt.x;
        Integer height = pt.y;
        //Toast.makeText(getBaseContext(),width.toString()+height.toString(),Toast.LENGTH_SHORT).show();
        */
    }

    // Timestamp
    public long getLocalToUtc() {
        final Date toTimestamp = new Date();
        final long timestamp = toTimestamp.getTime();
        return timestamp;
    }

    /*
    // Timestamp to Date
    public void getUtcToLocal(long loctimestamp) {
        //final Date fromTimestamp = new Date(timestamp);
        //System.out.println("Date :"+fromTimestamp.toString());
        //long timeParameter = 1509687242634;//Replace with your value 1186358400
        Timestamp timestamp = new Timestamp(loctimestamp);
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String textDate = df.format(date);
        //System.out.println("connvert:"+textDate);
        tv.setText(textDate);
    }
    */



    LocationManager locationManager;
    Boolean locationServiceBoolean = false;
    int providerType = 0;
    static AlertDialog alert;
    boolean gpsIsEnabled;

    public boolean isConnected(Context context) {

        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        gpsIsEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkIsEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //String s = Boolean.toString(gpsIsEnabled);
        //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (networkIsEnabled == true && gpsIsEnabled == true) {
            locationServiceBoolean = true;
            providerType = 1;

        } else if (networkIsEnabled != true && gpsIsEnabled == true) {
            locationServiceBoolean = true;
            providerType = 2;

        } else if (networkIsEnabled == true && gpsIsEnabled != true) {
            locationServiceBoolean = true;
            providerType = 3;
        }


        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //finish();
            }
        });

        return builder;
    }

    public void createLocationServiceError(final Activity activityObj) {

        // show alert dialog if Internet is not connected
        AlertDialog.Builder builder = new AlertDialog.Builder(activityObj);

        builder.setMessage(
                "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings")
                .setTitle("Turn On Location")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                activityObj.startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        alert = builder.create();
        alert.show();
    }

    //menu
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.userprofile_menu,m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(getBaseContext(), UserProfile.class));
                //finish();
                return true;
            case R.id.settings:
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
                return true;
            case  R.id.leaderboard:
                startActivity(new Intent(getBaseContext(), LeaderBoard.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
                return true;
            default:
                return false;
        }
    }

    //click events
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.potholecard:
                //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                if (!isConnected(GridActv.this)) {
                    buildDialog(GridActv.this).show();
                    //Toast.makeText(GridActv.this,"Welcome", Toast.LENGTH_SHORT).show();
                    //setContentView(R.layout.activity_grid_actv);
                }
                else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(),"per",Toast.LENGTH_SHORT).show();
                    String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    if(Build.VERSION.SDK_INT >= 23){
                        requestPermissions(permission, REQ_PER_CODE);
                    }
                    //return;
                }
                else if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(),"gps",Toast.LENGTH_SHORT).show();
                    String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                    if(Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(permission, REQ_PER_CODE);
                    }
                }
                else if(gpsIsEnabled == false) {
                    Toast.makeText(getBaseContext(),"gps ------ ",Toast.LENGTH_SHORT).show();
                    createLocationServiceError(GridActv.this);
                }
                else {
                    type = "pothole";
                    camera();
                }
                break;
            case R.id.garbagecard:
                if (!isConnected(GridActv.this)) {
                    buildDialog(GridActv.this).show();
                    //Toast.makeText(GridActv.this,"Welcome", Toast.LENGTH_SHORT).show();
                    //setContentView(R.layout.activity_grid_actv);
                }
                else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(),"per",Toast.LENGTH_SHORT).show();
                    String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    if(Build.VERSION.SDK_INT >= 23){
                        requestPermissions(permission, REQ_PER_CODE);
                    }
                    //return;
                }
                else if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //buildDialog(GridActv.this).show();
                    //Toast.makeText(GridActv.this,"Welcome", Toast.LENGTH_SHORT).show();
                    //setContentView(R.layout.activity_grid_actv);
                    String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                    if(Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(permission, REQ_PER_CODE);
                    }
                }
                else if(gpsIsEnabled == false) {
                    createLocationServiceError(GridActv.this);
                }
                else {
                    type = "garbage";
                    camera();
                }
                break;
            case R.id.complaintscard:
                startActivity(new Intent(getBaseContext(), Complaints.class));
                break;
        }
    }

    public void camera(){
        localTimestamp = getLocalToUtc();
        localTimestampstr = Long.toString(localTimestamp);
        filename = type + "-" + localTimestampstr + ".jpg";

        file = new File(Environment.getExternalStorageDirectory() + "/CitizenService/", filename);

        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cam.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        cam.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(cam, CAM_REQUEST);
    }

    //Cam Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getBaseContext(),"fail",Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == CAM_REQUEST) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("filename", filename);
            editor.putString("type", type);
            editor.putString("localTimestampstr", localTimestampstr);
            editor.commit();
            Intent i = new Intent(getBaseContext(), CameraActv.class);
            startActivity(i);
            Toast.makeText(getBaseContext(), type + " Card", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQ_PER_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(GridActv.this,"Permission accepted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(GridActv.this,"Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
