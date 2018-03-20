package com.example.ht.citizenservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MonumentCameraActivity extends AppCompatActivity implements View.OnClickListener{

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;
    String filenameMonument = "";
    String typeMonument = "";
    String localTimestampstrMonument = "";

    // byte array
    Bitmap bmp;
    String imgString = "";

    ImageView monumentUploadImg;
    Button uploadMonument;

    File file;

    StyleableToast styleableToast;

    // api
    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_camera);

        monumentUploadImg = findViewById(R.id.monumentImg);
        uploadMonument = findViewById(R.id.uploadMonument);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        filenameMonument = sharedpreferences.getString("filenameMonument", null);
        typeMonument = sharedpreferences.getString("typeMonument", null);
        localTimestampstrMonument = sharedpreferences.getString("localTimestampstrMonument", null);

        file = new File(Environment.getExternalStorageDirectory() + "/CitizenService/", filenameMonument);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        bmp = BitmapFactory.decodeFile(file.toString(), options);

        styleableToast = new StyleableToast
                .Builder(MonumentCameraActivity.this)
                .duration(Toast.LENGTH_LONG)
                .icon(R.drawable.camera_ic_autorenew_black_24dp)
                .spinIcon()
                .text("Uploading")
                .textColor(Color.parseColor("#FFDA44"))
                .textBold()
                .cornerRadius(5)
                .build();

        dataSetter();

        uploadMonument.setOnClickListener(this);

    }

    public void dataSetter() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        monumentUploadImg.setImageBitmap(bmp);
                    }
                });

                file = new File(Environment.getExternalStorageDirectory() + "/CitizenService/", filenameMonument);

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
                        monumentUploadImg.setImageBitmap(bmp);
                    }
                });

                return null;
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadMonument:
                uploadMonument.setClickable(false);
                //Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    uploadMonument.setTextColor(getColor(R.color.black));
                }
                styleableToast.show();
                byteConvert();
                callApi();
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

    //callApi
    public void callApi() {

        //Enter the URL here
        //final String url = "http://" + getString(R.string.mongoIp) + ":8004/insertion";
        String url = "http://52.187.6.152:8017/s3upload";

        final String finalUrl = url;

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {


                postdata = new JSONObject();
                try {
                    postdata.put("filename", filenameMonument);
                    postdata.put("imageByte", imgString);
                    postdata.put("type", "monument");
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
                ApiSucess(result);
            }

        }.execute();
    }

    private void ApiSucess(String result) {

        Log.d("----", result);

        /*
        try {
            JSONObject res = new JSONObject(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        */

        Intent i = new Intent(this, MonumentCameraResponse.class);
        Bundle b = new Bundle();
        b.putString("name", result);
        i.putExtras(b);
        startActivity(i);
        finish();
    }
}
