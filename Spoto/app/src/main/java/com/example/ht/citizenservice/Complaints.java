package com.example.ht.citizenservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

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

public class Complaints extends AppCompatActivity implements ComplaintsLoadImageTask.Listener{

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

    List<ComplaintsData> list = new ArrayList<>();

    SparseArray<String> sparseArraycomplaintsTitle = new SparseArray<>();
    SparseArray<String> sparseArraytimeStatus = new SparseArray<>();
    SparseArray<String> sparseArrayUrl = new SparseArray<>();
    SparseArray<String> sparseArrayauthorityId = new SparseArray<>();

    /*
    MongoClient mongoClient;
    DB db;
    DBCollection dbCollection;
    DBCursor dbCursor;
    BasicDBObject query;
    */

    JSONObject jsonObject;

    SparseArray<String> clickarray = new SparseArray<>();
    SparseArray<String> arrayjson = new SparseArray<>();

    int count = 0;
    int i = 0;

    String jsonvalue = "";

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    String profileemail, profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        recyclerView = findViewById(R.id.recyclerview);
        tv = findViewById(R.id.tv);

        dialog = ProgressDialog.show(this, getResources().getString(R.string.progressTitle), getResources().getString(R.string.progressMsg), true);

        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .build();

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        profileemail = sharedpreferences.getString("email", null);
        profileName = sharedpreferences.getString("profileName", null);

        callDB();

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(), clickarray.get(Integer.parseInt(Integer.toString(position)), "Error"), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), TimeLineViewActv.class);
                Bundle b = new Bundle();
                b.putString("json", clickarray.get(Integer.parseInt(Integer.toString(position)), "Error"));
                Log.d("-----------json", clickarray.get(Integer.parseInt(Integer.toString(position))));
                i.putExtras(b);
                startActivity(i);
            }
        });
        */

    }

    public void callDB () {

        final String url = getString(R.string.complaint);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                /*

                mongoClient = new MongoClient(getResources().getString(R.string.comIp));
                db = mongoClient.getDB(getResources().getString(R.string.comdb));
                dbCollection = db.getCollection(getResources().getString(R.string.comcol));
                query = new BasicDBObject();
                query.put(getResources().getString(R.string.comEmailKey), profileemail);
                dbCursor = dbCollection.find(query);

                //HashMap<String, String> subdata = new HashMap<>();

                while (dbCursor.hasNext()) {
                    try {
                        jsonObject = new JSONObject(String.valueOf(dbCursor.next()));

                        sparseArraycomplaintsTitle.put(count, jsonObject.getString("complaintTitle"));
                        sparseArraytimeStatus.put(count, jsonObject.getString("timelineStatus"));
                        sparseArrayUrl.put(count, jsonObject.getString("imageUrl"));
                        sparseArrayauthorityId.put(count, jsonObject.getString("AJabberId"));
                        count++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mongoClient.close();
                return null;
                */
                postdata = new JSONObject();
                try {;
                    postdata.put("userId", profileemail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
            protected void onPostExecute(String subdata) {
                super.onPostExecute(subdata);
                //Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();

                if (subdata == null) {
                    tv.setText(R.string.complaintsnull);
                    dialog.cancel();
                }
                else if (subdata.equals("")) {
                    tv.setText(R.string.complaintsinternet);
                    dialog.cancel();
                }
                else if (subdata.equals("{\"flag\": \"null\"}")) {
                    tv.setText(R.string.complaintsnodata);
                    dialog.cancel();
                }
                else {
                    Log.i("---------test", String.valueOf(subdata.length()));
                    JSONArray jsonArray;
                    try {
                        jsonArray = new JSONArray(subdata);
                        //jsonObject = new JSONObject(String.valueOf(dbCursor.next()));

                        for (int i = 0; i <= (jsonArray.length() - 1); i++) {
                            jsonObject = new JSONObject(jsonArray.get(i).toString());

                            sparseArraycomplaintsTitle.put(count, jsonObject.getString("complaintTitle"));
                            sparseArraytimeStatus.put(count, jsonObject.getString("timelineStatus"));
                            sparseArrayUrl.put(count, jsonObject.getString("imageUrl"));
                            sparseArrayauthorityId.put(count, jsonObject.getString("AJabberId"));
                            count++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (i = 0; i < sparseArraytimeStatus.size(); i++) {
                        Log.i("-------------", sparseArraycomplaintsTitle.get(i) + sparseArraytimeStatus.get(i));
                        imageFunction(String.valueOf(i + 1), sparseArraycomplaintsTitle.size(), sparseArraycomplaintsTitle.get(i, "Error in complaintTitle"), sparseArraytimeStatus.get(i, "Error in status"), sparseArrayUrl.get(i, "Error in profileurl"), sparseArrayauthorityId.get(i, "Error in authorityId"));
                    }
                }
            }
        }.execute();
    }

    public void imageFunction(String number, int size, String complaintTitle, String timelineStatus, String profurl, String AJabberId){
        new ComplaintsLoadImageTask(this, number, size, complaintTitle, timelineStatus, AJabberId).execute(profurl);

    }

    @Override
    public void onImageLoaded(Bitmap bitmap, String s, int size, String complaintTitle, String timelineStatus, String AJabberId) throws JSONException, InterruptedException {
        list.add(new ComplaintsData(complaintTitle, timelineStatus, AJabberId, bitmap));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ComplaintsAdapter(this, list));
        dialog.cancel();
    }

    @Override
    public void onError() {

    }
}
