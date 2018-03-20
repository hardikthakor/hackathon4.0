package com.example.ht.citizenservice;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserProfile extends AppCompatActivity implements LoadImageTask.Listener{

    Button bt;
    ImageView setProfileImage;
    TextView name, setemail, complaintsview, pointsview;

    ProgressDialog dialog;

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    String profileurl;
    String profilename;
    String profileemail;

    // api
    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    /*
    // mongo
    MongoClient mongoClient;
    DB db;
    DBCollection dbCollection;
    DBCursor dbCursor;
    BasicDBObject query;
    */

    JSONObject jsonObject;

    String jsonpoints, complaints, points;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        dialog = ProgressDialog.show(this, getResources().getString(R.string.progressTitle), getResources().getString(R.string.progressMsg), true);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        profilename = sharedpreferences.getString("profileName", null);
        profileurl = sharedpreferences.getString("profileImageUrl", null);
        profileemail = sharedpreferences.getString("email", null);
        //Toast.makeText(getBaseContext(),test,Toast.LENGTH_SHORT).show();

        bt = (Button) findViewById(R.id.bt);
        setProfileImage = (ImageView) findViewById(R.id.profileImage);
        name = (TextView) findViewById(R.id.name);
        setemail = (TextView) findViewById(R.id.setemail);
        complaintsview = findViewById(R.id.complaints);
        pointsview = findViewById(R.id.points);

        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/REFSAN.TTF");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/amitabold.ttf");
        name.setTypeface(typeface1);
        setemail.setTypeface(typeface);

        if (profilename == null){
            name.setText("Username not Available");
        }
        else {
            name.setText(profilename);
        }

        if (profileemail == null){
            setemail.setText("e-mail not Available");
        }
        else {
            setemail.setText(profileemail);
        }

        if (profileurl == null){ }
        else {
            imageFunction();
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), DashBoard.class));
                finish();
            }
        });
    }

    public void imageFunction(){
        callDB();
    }

    public void callDB () {

        final String url = getString(R.string.userprof);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                postdata = new JSONObject();
                try {
                    postdata.put("userId", profileemail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
                mongoClient = new MongoClient(getResources().getString(R.string.prof_Ip));
                db = mongoClient.getDB(getResources().getString(R.string.prof_db));

                dbCollection = db.getCollection(getResources().getString(R.string.prof_col));
                query = new BasicDBObject();
                query.put(getResources().getString(R.string.profEmailKey), profileemail);

                dbCursor = dbCollection.find(query);

                while (dbCursor.hasNext()) {
                    jsonpoints = String.valueOf(dbCursor.next());
                }

                mongoClient.close();
                */
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
                //Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();
                Log.i("---------", result);

                try {
                    if (result == null) {
                        pointsview.setText("0");
                        complaintsview.setText("0");
                    }
                    else {
                        jsonObject = new JSONObject(result);

                        points = String.valueOf(jsonObject.getInt("points"));
                        complaints = jsonObject.getString("complaintsCount");

                        pointsview.setText(points);
                        complaintsview.setText(complaints);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new LoadImageTask(UserProfile.this).execute(profileurl);
                dialog.cancel();
            }
        }.execute();
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) throws JSONException, InterruptedException {
        setProfileImage.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {

    }
}
