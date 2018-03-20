package com.example.ht.citizenservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageUploadResponse extends AppCompatActivity implements LoadImageTask.Listener{

    String urlImg_json;
    String payLoad_json;

    ImageView im;
    TextView tv, name, email;
    Button bt;

    CircleImageView profImg;

    CardView backcard;

    ShimmerFrameLayout shi;

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;
    String profileemail = "";
    String profileName = "";
    String profileUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload_response);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle b = getIntent().getExtras();
        urlImg_json = b.getString("imgUrl");
        payLoad_json = b.getString("payLoad");

        im = (ImageView) findViewById(R.id.im);
        tv = (TextView) findViewById(R.id.tv);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        profImg = findViewById(R.id.profImg);
        shi = (ShimmerFrameLayout) findViewById(R.id.shi);
        bt = findViewById(R.id.bt);
        backcard = findViewById(R.id.backcard);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        profileemail = sharedpreferences.getString("email", null);
        profileName = sharedpreferences.getString("profileName", null);
        profileUrl = sharedpreferences.getString("profileImageUrl", null);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/REFSAN.TTF");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/amitaregular.ttf");
        //des.setTypeface(typeface);
        tv.setTypeface(typeface);
        name.setTypeface(typeface1);
        email.setTypeface(typeface1);

        name.setText(profileName);
        email.setText(profileemail);

        backcard.setBackgroundResource(R.drawable.complaintsview);

        shi.setDuration(1000);
        shi.setDropoff(1);
        shi.setAutoStart(true);

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream((InputStream)new URL(profileUrl).getContent());
            //Intent intent = new Intent(Intent.ACTION_SEND);
            //intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");
            //String path = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "", null);
            //Uri screenshotUri = Uri.parse(path);
            //intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            //intent.setType("image/*");
            //startActivity(Intent.createChooser(intent, "Share image via..."));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profImg.setImageBitmap(bmp);

        if (payLoad_json.equals("error")) {
            urlImg_json = "https://s3.ap-south-1.amazonaws.com/responseimages/error.png";
            //Toast.makeText(getBaseContext(),"Manual", Toast.LENGTH_SHORT).show();
        }

        if (payLoad_json.equals("")) {
            payLoad_json = "error";
            urlImg_json = "https://s3.ap-south-1.amazonaws.com/responseimages/error.png";
            //Toast.makeText(getBaseContext(),"Null", Toast.LENGTH_SHORT).show();
        }

        imageFunction();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), DashBoard.class));
                finish();
            }
        });
    }

    public void imageFunction(){
        new LoadImageTask(this).execute(urlImg_json);
    }


    @Override
    public void onImageLoaded(Bitmap bitmap) throws JSONException, InterruptedException {

        if (payLoad_json.equals(getString(R.string.garbageError))) {
            display(payLoad_json, bitmap);
            //Thread.sleep(1000);
        }
        else if (payLoad_json.equals(getString(R.string.potholeError))) {
            display(payLoad_json, bitmap);
        }
        else if (payLoad_json.equals(getString(R.string.captionPothole))) {
            display(payLoad_json, bitmap);
        }
        else if (payLoad_json.equals(getString(R.string.captionGarbage))) {
            display(payLoad_json, bitmap);
        }
        else if (payLoad_json.equals(getString(R.string.successResp))) {
            display(getString(R.string.success), bitmap);
        }
        else if (payLoad_json.equals(getString(R.string.duplicateResp))){
            display(getString(R.string.duplicate), bitmap);
        }
        else if (payLoad_json.equals(getString(R.string.fraudcheck))) {
            display(getString(R.string.fraud), bitmap);
        }
        else if (payLoad_json.equals(getString(R.string.Pothole))) {    // jaipur
            display(getString(R.string.success), bitmap);
        }
        else if (payLoad_json.equals(getString(R.string.Garbage))) {    // jaipur
            display(getString(R.string.success), bitmap);
        }
        else if (payLoad_json.equals(getString(R.string.Wrong))) {      // jaipur
            display(getString(R.string.wrongMsg), bitmap);
        }
        else {
            im.setImageResource(R.drawable.ic_error_outline);
            tv.setText(R.string.error);
            shi.setAutoStart(false);
        }
    }

    private void display (String text, Bitmap bitmap) {
        im.setImageBitmap(bitmap);
        tv.setText(text);
        shi.setAutoStart(false);
    }

    @Override
    public void onError() {
        StyleableToast styleableToast;
        styleableToast = new StyleableToast
                .Builder(ImageUploadResponse.this)
                .duration(Toast.LENGTH_SHORT)
                .text(String.valueOf(R.string.imageLoadingFail))
                .textColor(Color.parseColor("#FFDA44"))
                .textBold()
                .cornerRadius(5)
                .build();
        styleableToast.show();
        //Toast.makeText(getApplicationContext(), R.string.imageLoadingFail,Toast.LENGTH_SHORT).show();
    }
}
