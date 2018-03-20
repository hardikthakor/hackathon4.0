package com.example.dell.authorityapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.widget.Toast;


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
public class leader_board extends AppCompatActivity  implements LeaderBoardLoadImageTask.Listener {

    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jsonObject;
    RequestBody body;
    Request request;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    List<LeaderBoardData> list = new ArrayList<>();

    RecyclerView recyclerView;
    ProgressDialog dialog;

    String name = "0", points = "0", profurl = "0";
    int count = 0;

    SparseArray<String> sparseArrayName = new SparseArray<>();
    SparseArray<String> sparseArrayPoints = new SparseArray<>();
    SparseArray<String> sparseArrayUrl = new SparseArray<>();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        dialog = ProgressDialog.show(this, getResources().getString(R.string.progressTitle), getResources().getString(R.string.progressMsg), true);

        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .build();

        callLB();
    }

    public void imageFunction(String number, int size, String name, String points, String profurl){
        new LeaderBoardLoadImageTask(this, number, size, name, points).execute(profurl);

    }

    public void callLB () {

        final String url = getString(R.string.leaderboard);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                /*
                m = new MongoClient(getResources().getString(R.string.mongoIp));

                db = m.getDB(getResources().getString(R.string.dbName));
                dbCollection = db.getCollection(getResources().getString(R.string.collectionName));
                dbCursor = dbCollection.find().sort(new BasicDBObject(getResources().getString(R.string.pointsKey), -1));

                while (dbCursor.hasNext()) {
                    try {
                        jsonObject = new JSONObject(String.valueOf(dbCursor.next()));

                        name = jsonObject.getString(getResources().getString(R.string.nameKey));
                        points = jsonObject.getString(getResources().getString(R.string.pointsKey));
                        profurl = jsonObject.getString(getResources().getString(R.string.profurlKey));

                        sparseArrayName.put(count, name);
                        sparseArrayPoints.put(count, points);
                        sparseArrayUrl.put(count, profurl);
                        count++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                m.close();
                return null;
                */

                //body = RequestBody.create(MEDIA_TYPE, postdata.toString());

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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();

                JSONArray jsonArray ;
                try {
                    jsonArray = new JSONArray(result);
                    //jsonObject = new JSONObject(String.valueOf(dbCursor.next()));

                    for (int i = 0;i<=(jsonArray.length() - 1);i++) {
                        jsonObject = new JSONObject(jsonArray.get(i).toString());

                        sparseArrayName.put(count, jsonObject.getString(getResources().getString(R.string.nameKey)));
                        sparseArrayPoints.put(count, jsonObject.getString(getResources().getString(R.string.pointsKey)));
                        sparseArrayUrl.put(count, jsonObject.getString(getResources().getString(R.string.profurlKey)));
                        count++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (i = 0;i < sparseArrayName.size();i++){
                    imageFunction(String.valueOf(i + 1), sparseArrayName.size(), sparseArrayName.get(i, "Error in name"), sparseArrayPoints.get(i, "Error in points"), sparseArrayUrl.get(i, "Error in profileurl"));
                }
            }
        }.execute();
    }

    @Override
    public void onImageLoaded(Bitmap bitmap, String s, int size, String name, String points) throws JSONException, InterruptedException {

        if (s == String.valueOf(size)) {
            //Toast.makeText(getApplicationContext(),"end",Toast.LENGTH_SHORT).show();
            list.add(new LeaderBoardData(" " + s + " ", name, bitmap, points, R.drawable.leaderboardmedaltransparent));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new LeaderBoardAdapter(this, list));
            dialog.dismiss();
        }
        else if (s.equals("1")) {
            //Toast.makeText(getApplicationContext(),"one",Toast.LENGTH_SHORT).show();
            list.add(new LeaderBoardData(" " + s + " ", name, bitmap, points, R.drawable.leaderboardgold));
            //Toast.makeText(getBaseContext(),bitmap.toString(),Toast.LENGTH_SHORT).show();
        }
        else if (s.equals("2")){
            list.add(new LeaderBoardData(" " + s + " ", name, bitmap, points, R.drawable.leaderboardsilver));
        }
        else if (s.equals("3")){
            list.add(new LeaderBoardData(" " + s + " ", name, bitmap, points, R.drawable.leaderboardcopper));
        }
        else {
            list.add(new LeaderBoardData(" " + s + " ", name, bitmap, points, R.drawable.leaderboardmedaltransparent));
        }

    }



    @Override
    public void onError() {
        Toast.makeText(getBaseContext(),"Fail",Toast.LENGTH_SHORT).show();
    }
}
