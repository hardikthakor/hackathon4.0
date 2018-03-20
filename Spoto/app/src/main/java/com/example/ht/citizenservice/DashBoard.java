package com.example.ht.citizenservice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import org.json.JSONException;

import java.io.File;
import java.util.Date;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoadImageTask.Listener, View.OnClickListener {

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;
    String profileemail;
    String profileName;
    String profileImageUrl;

    ArcNavigationView arcNavigationView;

    TextView uname, email;
    ImageView profimg;

    CardView potholecard, garbagecard, monumentcard, categorycard, newscard, watsoncard;
    File folder;
    String token;

    //Camera
    String filename;
    long localTimestamp;
    String localTimestampstr;
    String type = "";
    File file;
    static final int CAM_REQUEST = 1;

    // Permission
    private static final int REQ_PER_CODE = 901;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cam for Nougat above API 24
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //SendNotification sendNotification = new SendNotification();
        //sendNotification.callApi("Citizen App", "Hello", "coZpVCk8nUw:APA91bGX8LAFGqS5fw_trfxYISO6s-l9mF_q52G2nxj-4qkcn1tppstWm3t8IzX68ZXFyHNBN-VCgWMK0Dn-ZqrRJnuQtqaxb5FeUNp_v-r8ITCLvApN0iwvAKKIBDD-HQqQXVIds4vH");

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        profileemail = sharedpreferences.getString("email", null);
        profileName = sharedpreferences.getString("profileName", null);
        profileImageUrl = sharedpreferences.getString("profileImageUrl", null);
        token = sharedpreferences.getString("tokenFirebase", null);

        //Log.i("------token", token);

        potholecard = (CardView) findViewById(R.id.potholecard);
        garbagecard = (CardView) findViewById(R.id.garbagecard);
        monumentcard = (CardView) findViewById(R.id.monumentcard);
        categorycard = findViewById(R.id.categorycard);
        newscard = findViewById(R.id.newscard);
        watsoncard = findViewById(R.id.watsoncard);
        arcNavigationView = findViewById(R.id.nav_view);

        potholecard.setBackgroundResource(R.drawable.dashboardpart1min);
        garbagecard.setBackgroundResource(R.drawable.dashboardpart2min);
        monumentcard.setBackgroundResource(R.drawable.dashboardpart3min);
        newscard.setBackgroundResource(R.drawable.dashboardpart4min);
        categorycard.setBackgroundResource(R.drawable.dashboardpart5min);
        watsoncard.setBackgroundResource(R.drawable.dashboardpart6min);

        potholecard.setOnClickListener(this);
        garbagecard.setOnClickListener(this);
        monumentcard.setOnClickListener(this);
        categorycard.setOnClickListener(this);
        newscard.setOnClickListener(this);
        watsoncard.setOnClickListener(this);

        folder = new File(Environment.getExternalStorageDirectory() + "/CitizenService/");

        if(! folder.exists()){
            folder.mkdir();
        }

        String tkn = FirebaseInstanceId.getInstance().getToken();

        Log.i("--------token", tkn);
        
        View v = arcNavigationView.getHeaderView(0);
        uname = v.findViewById(R.id.uname);
        email = v.findViewById(R.id.email);
        profimg = v.findViewById(R.id.imageView);

        imageFunction();
        
        uname.setText(profileName);
        email.setText(profileemail);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public long getLocalToUtc() {
        final Date toTimestamp = new Date();
        final long timestamp = toTimestamp.getTime();
        return timestamp;
    }

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

    public void imageFunction(){
        new LoadImageTask(this).execute(profileImageUrl);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }
    */
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.profile:
                //Toast.makeText(getBaseContext(),"Prof",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(), UserProfile.class));
                return true;
            case R.id.leaderboard:
                startActivity(new Intent(getBaseContext(), LeaderBoard.class));
                return true;
            case R.id.complaints:
                startActivity(new Intent(getBaseContext(), Complaints.class));
                break;
            case R.id.hotelo:
                startActivity(new Intent(getBaseContext(), Hotelo.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("logincheck", false);
                editor.commit();
                finish();
                return true;
            case R.id.nav_share:
                Uri uri = Uri.parse("android.resource://com.example.ht.citizenservice/"+R.drawable.bgmin);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, This is test Sharing");
                startActivity(Intent.createChooser(shareIntent, "Send your image"));

                return true;
            case R.id.nav_send:
                startActivity(new Intent(getBaseContext(), WatsonChat.class));
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) throws JSONException, InterruptedException {
        profimg.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {
        Toast.makeText(getBaseContext(),"Failed to Load Profile Image ... ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.potholecard:
                startActivity(new Intent(DashBoard.this, Monument.class));
                break;
            case R.id.garbagecard:
                if (!isConnected(DashBoard.this)) {
                    buildDialog(DashBoard.this).show();
                }
                else if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getBaseContext(),"per",Toast.LENGTH_SHORT).show();
                    String[] permission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
                    if(Build.VERSION.SDK_INT >= 23){
                        requestPermissions(permission, REQ_PER_CODE);
                    }
                    //return;
                }
                else if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    String[] permission = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
                    if(Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(permission, REQ_PER_CODE);
                    }
                }
                else if(gpsIsEnabled == false) {
                    createLocationServiceError(DashBoard.this);
                }
                else {
                    type = "garbage";
                    //showRadioButtonDialog();
                    camera();
                }
                break;
            case R.id.monumentcard:
                startActivity(new Intent(getBaseContext(), MapsActivity.class));
                break;
            case R.id.newscard:
                startActivity(new Intent(getBaseContext(), NewsJaipur.class));
                //Toast.makeText(getBaseContext(), "To be Done ... ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.categorycard:
                startActivity(new Intent(getBaseContext(), Category.class));
                break;
            case R.id.watsoncard:
                startActivity(new Intent(getBaseContext(), WatsonChat.class));
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
            //Toast.makeText(getBaseContext(),"fail",Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == CAM_REQUEST) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("filename", filename);
            editor.putString("type", type);
            editor.putString("localTimestampstr", localTimestampstr);
            editor.commit();
            Intent i = new Intent(getBaseContext(), CameraActv.class);
            startActivity(i);
            //Toast.makeText(getBaseContext(), type + " Card", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQ_PER_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getBaseContext(),"Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getBaseContext(),"Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void showRadioButtonDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogselectionradio);

        RadioButton potholerb, garbagerb;

        potholerb = dialog.findViewById(R.id.pot);
        garbagerb = dialog.findViewById(R.id.gar);

        potholerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "pothole";
                dialog.dismiss();
                camera();
            }
        });

        garbagerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "garbage";
                dialog.dismiss();
                camera();
            }
        });

        //Log.d("----", type);

        dialog.show();

    }
}
