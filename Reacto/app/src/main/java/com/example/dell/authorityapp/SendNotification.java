package com.example.dell.authorityapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by CGT on 08-02-2018.
 */

public class SendNotification {

    // api
    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");


    public void callApi(final String title, final String text, final String deviceId) {
        //Enter the URL here
        final String url = "https://fcm.googleapis.com/fcm/send";

        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        return response.request().newBuilder().header("Authorization", "key=AIzaSyBp0LK2dSzS9E6ewJ-oX-w7hu8d-RCvFQY").build();
                    }
                })
                .build();

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                JSONObject subJson1 = new JSONObject();
                JSONObject subJson2 = new JSONObject();

                postdata = new JSONObject();
                try {
                    subJson1.put("body", text);
                    subJson1.put("title", title);

                    subJson2.put("body", "First Notification");
                    subJson2.put("title", "Collapsing A");
                    subJson2.put("key_1", "Data for key one");
                    subJson2.put("key_2", "Hellowww");

                    postdata.put("to", deviceId);
                    postdata.put("collapse_key", "type_a");
                    postdata.put("notification", subJson1);
                    postdata.put("data", subJson2);

                    //extra = ", "data" : {"body" : "First Notification", "title": "Collapsing A", "key_1" : "Data for key one", "key_2" : "Hellowww"}}";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.d("------------", postdata.toString());

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
                //ApiSucess(result);
            }

        }.execute();
    }

}
