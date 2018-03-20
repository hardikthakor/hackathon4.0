package com.example.ht.citizenservice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewNews extends AppCompatActivity implements NewsLoadImageTask.Listener {

    double latitude,longitude;
    Geocoder geocoder;
    String userid,type,timestamp,lat,lon,complaintTitle,imageurl,tokenId,acceptance,proposedDate,location,description,deviceId,dt, pincode,issueDate,jabberId,JabberId,timelineStatus,priority,photoUrl,emailId;
    ImageView loc_img;
    List<Address> addresses;
    TextView tvadd,tv3;
    ImageButton btnacc,btrej;
    Bitmap bitmap=null;
    String address;
    String postalCode;
    int RatingValue = 0;

    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //share.setOnClickListener(this);
        Button btn = findViewById(R.id.share);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bitmap bmp =null;
                String s = "Date : "+issueDate+"\nLocation : "+location+"\nUser Id : "+userid+"\nSeverity : "+priority+"\nProposed Date:"+
                        proposedDate+"\nDescription : "+description;
                try {
                    bmp = BitmapFactory.decodeStream((InputStream)new URL(imageurl).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_TEXT,s);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "", null);
                Uri screenshotUri = Uri.parse(path);

                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "Share image via..."));
            }
        });

        loc_img=findViewById(R.id.loc_img);
        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .build();

        Bundle b=getIntent().getExtras();

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
        JabberId=b.getString("JabberId");
        jabberId=b.getString("jabberId");
        tokenId=b.getString("tokenId");
        proposedDate=b.getString("proposedDate");
        timelineStatus=b.getString("timelineStatus");
        description=b.getString("description");
        location=b.getString("location");
        //Toast.makeText(getApplicationContext(),imageurl,Toast.LENGTH_LONG).show();
        Log.i("-----------------------", b.toString());

        imageFunction();

        TextView textView = (TextView) findViewById(R.id.desc);
        textView.setText(description); //leave this line to assign a specific text

        TextView textView1 = (TextView) findViewById(R.id.date);
        textView1.setText(issueDate);

        TextView textView2 = (TextView) findViewById(R.id.location);
        textView2.setText(location);

        TextView textView3 = (TextView) findViewById(R.id.user_id);
        textView3.setText(userid);

        TextView textView4 = (TextView) findViewById(R.id.prop_time);
        textView4.setText(proposedDate);
        if(priority.equals("High")){
            RatingValue = 5;
        }
        if(priority.equals("Medium")){
            RatingValue=3;
        }
        if(priority.equals("Low")){
            RatingValue=1;
        }

        RatingBar ratingBar=(RatingBar) findViewById(R.id.rbr);
        ratingBar.setRating(RatingValue);

    }

    public void imageFunction() {
        new NewsLoadImageTask(this).execute(imageurl);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) throws JSONException, InterruptedException {
        loc_img.setImageBitmap(bitmap);
        this.bitmap = bitmap;
        Log.i("-----------------", bitmap.toString());
    }

    @Override
    public void onError() {

    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.sharemenu,m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menushare:
                Bitmap bmp = null;
                String s = "Date : "+issueDate+"\nLocation : "+location+"\nUser Id : "+userid+"\nSeverity : "+priority+"\nProposed Date:"+
                        proposedDate+"\nDescription : "+description;
                try {
                    bmp = BitmapFactory.decodeStream((InputStream)new URL(imageurl).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_TEXT,s);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "", null);
                Uri screenshotUri = Uri.parse(path);

                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "Share image via..."));
                return true;

            default:
                return false;
        }
    }
}
