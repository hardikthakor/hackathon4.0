package com.example.ht.citizenservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class Monument extends AppCompatActivity implements View.OnClickListener{

    TextView name1, name2, name3, name4, name5;
    LinearLayout place1, place2, place3, place4, place5;

    String monumentSelected = "";

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    //Camera
    String filename;
    long localTimestamp;
    String localTimestampstr;
    String type = "";
    File file;
    static final int CAM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument);

        // Cam for Nougat above API 24
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        monumentSelected = sharedpreferences.getString("monumentSelected", null);

        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        name4 = findViewById(R.id.name4);
        name5 = findViewById(R.id.name5);
        place1 = findViewById(R.id.place1);
        place2 = findViewById(R.id.place2);
        place3 = findViewById(R.id.place3);
        place4 = findViewById(R.id.place4);
        place5 = findViewById(R.id.place5);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/amitabold.ttf");
        name1.setTypeface(typeface);
        name2.setTypeface(typeface);
        name3.setTypeface(typeface);
        name4.setTypeface(typeface);
        name5.setTypeface(typeface);

        place1.setOnClickListener(this);
        place2.setOnClickListener(this);
        place3.setOnClickListener(this);
        place4.setOnClickListener(this);
        place5.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        SharedPreferences.Editor editor = sharedpreferences.edit();

        switch (v.getId()) {
            case R.id.place1:
                editor.putString("monumentSelected", "1");
                editor.apply();
                startActivity(new Intent(Monument.this, MonumentDescription.class));
                break;
            case R.id.place2:
                editor.putString("monumentSelected", "2");
                editor.apply();
                startActivity(new Intent(Monument.this, MonumentDescription.class));
                break;
            case R.id.place3:
                editor.putString("monumentSelected", "3");
                editor.apply();
                startActivity(new Intent(Monument.this, MonumentDescription.class));
                break;
            case R.id.place4:
                editor.putString("monumentSelected", "4");
                editor.apply();
                startActivity(new Intent(Monument.this, MonumentDescription.class));
                break;
            case R.id.place5:
                editor.putString("monumentSelected", "5");
                editor.apply();
                startActivity(new Intent(Monument.this, MonumentDescription.class));
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menumounment,m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clickmonument:
                camera();
                return true;
            default:
                return false;
        }
    }

    public void camera(){
        type = "monument";
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
            editor.putString("filenameMonument", filename);
            editor.putString("typeMonument", type);
            editor.putString("localTimestampstrMonument", localTimestampstr);
            editor.commit();

            Intent i = new Intent(getBaseContext(), MonumentCameraActivity.class);
            startActivity(i);
        }
    }

    public long getLocalToUtc() {
        final Date toTimestamp = new Date();
        final long timestamp = toTimestamp.getTime();
        return timestamp;
    }

}
