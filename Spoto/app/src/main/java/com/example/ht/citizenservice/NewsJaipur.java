package com.example.ht.citizenservice;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewsJaipur extends AppCompatActivity implements JaipurImageLoad.Listener{

    // api
    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    RequestBody body;
    Request request;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    ListView listView;
    TextView tv;
    ProgressDialog dialog;
    RecyclerView recyclerView;

    List<JaipurData> list = new ArrayList<>();

    SparseArray<String> sparseArraynameTitle = new SparseArray<>();
    SparseArray<String> sparseArraysrcDate = new SparseArray<>();
    SparseArray<String> sparseArrayUrl = new SparseArray<>();
    SparseArray<String> sparseArraydesDate = new SparseArray<>();
    SparseArray<String> sparseArraydescription = new SparseArray<>();

    JSONObject jsonObject;

    int count = 0;
    int i = 0;

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_jaipur);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        recyclerView = findViewById(R.id.recyclerview11);
        tv = findViewById(R.id.tv);

        dialog = ProgressDialog.show(this, getResources().getString(R.string.progressTitle), getResources().getString(R.string.progressMsg), true);

        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        callDB();

    }

    public void callDB () {

        final String url = getString(R.string.newsJaipur);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                request = new Request.Builder()
                        .url(url)
                        .get()
                        //.addHeader("Content-Type", "application/json")
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
            protected void onPostExecute(String subdata) {
                super.onPostExecute(subdata);
                //Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();

                if (subdata == null) {
                    tv.setText("No News Found ...");
                    dialog.cancel();
                }
                else if (subdata.equals("")) {
                    tv.setText("No News Found ...");
                    dialog.cancel();
                }
                else if (subdata.equals("{\"flag\": \"null\"}")) {
                    tv.setText("No News Found ...");
                    dialog.cancel();
                }
                else {
                    //Log.d("---------test", String.valueOf(subdata));

                    JSONArray jsonArraydesc;
                    JSONArray jsonArrayname;
                    JSONArray jsonArraydesDate;
                    JSONArray jsonArraysrcDate;
                    JSONArray jsonArrayurl;

                    try {
                        jsonObject = new JSONObject(subdata);

                        jsonArrayname = new JSONArray(jsonObject.getString("name"));
                        jsonArraydesc = new JSONArray(jsonObject.getString("desc"));
                        jsonArraydesDate = new JSONArray(jsonObject.getString("desDate"));
                        jsonArraysrcDate = new JSONArray(jsonObject.getString("srcDate"));
                        jsonArrayurl = new JSONArray(jsonObject.getString("url"));

                        //Log.d("----", jsonArrayname.toString());

                        // for (int i = 0; i <= (jsonArrayname.length() - 1); i++) {
                        for (int i = 0; i <= 5; i++) {
                            //jsonObject = new JSONObject(jsonArrayname.get(i).toString());
                            sparseArraynameTitle.put(count, jsonArrayname.get(i).toString());
                            sparseArraydescription.put(count, jsonArraydesc.get(i).toString());
                            sparseArrayUrl.put(count, jsonArrayurl.get(i).toString());
                            sparseArraysrcDate.put(count, jsonArraysrcDate.get(i).toString());
                            sparseArraydesDate.put(count, jsonArraydesDate.get(i).toString());
                            count++;
                        }

                        Log.d("----", sparseArraynameTitle.toString());
                        //Log.d("----", sparseArraydescription.toString());
                        //Log.d("----", sparseArrayUrl.toString());
                        //Log.d("----", sparseArraysrcDate.toString());
                        //Log.d("----", sparseArraydesDate.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    for (i = 0; i < sparseArraysrcDate.size(); i++) {
                        //Log.i("-------------", sparseArraynameTitle.get(i) + sparseArraysrcDate.get(i));
                        imageFunction(String.valueOf(i + 1), sparseArraysrcDate.size(), sparseArraynameTitle.get(i, "Error in complaintTitle"), sparseArraysrcDate.get(i, "Error in status"), sparseArrayUrl.get(i, "Error in profileurl"), sparseArraydescription.get(i, "Error in authorityId"), sparseArraydesDate.get(i, "Error in authorityId"));
                    }
                }
            }
        }.execute();
    }


    public void imageFunction(String number, int size, String name, String srcDate, String url, String description, String desDate){
        new JaipurImageLoad(this, number, size, name, srcDate, url, description, desDate).execute(url);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap, String s, int size, String name, String srcDate, String url, String description, String desDate) throws JSONException, InterruptedException {
        list.add(new JaipurData(srcDate, desDate, name, description, url, bitmap));
        //Log.d("----", list.toString());
        recyclerView.setLayoutManager(new LinearLayoutManager(NewsJaipur.this));
        recyclerView.setAdapter(new JaipurAdapter(this, list));
        if (dialog != null)
            dialog.cancel();
    }

    @Override
    public void onError() {

    }
}
