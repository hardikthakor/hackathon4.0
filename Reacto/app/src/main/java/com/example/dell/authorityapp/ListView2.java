package com.example.dell.authorityapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ListView2 extends AppCompatActivity implements LoadImageTask.Listener {
    OkHttpClient client = new OkHttpClient();
    String userid,type,timestamp,lat,lon,title,imageurl,address,postalCode;
    //    ImageView img1;
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request,request1;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    RecyclerView recyclerView;
    int count1=0;
    ArrayList<Values> list = new ArrayList<>();
    SparseArray<String> sparseArrayTitle = new SparseArray<>();
    SparseArray<String> sparseArrayimageUrl = new SparseArray<>();
    SparseArray<String> sparseArrayimageRating = new SparseArray<>();
    SparseArray<String> sparseArraylat = new SparseArray<>();
    SparseArray<String> sparseArraylong = new SparseArray<>();
    SparseArray<String> sparseArrayuserId = new SparseArray<>();
    SparseArray<String> sparseArrayTimestamp = new SparseArray<>();
    SparseArray<String> sparseArraytype = new SparseArray<>();
    SparseArray<String> sparseArraydeviceId = new SparseArray<>();
    SparseArray<String> sparseArrayissueDate = new SparseArray<>();
    SparseArray<String> sparseArraypincode= new SparseArray<>();
    SparseArray<String> sparseArraylocation = new SparseArray<>();
    SparseArray<String> sparseArraypriority = new SparseArray<>();
    SparseArray<String> sparseArrayphotoUrl=new SparseArray<>();
    SparseArray<String> sparseArrayemailId = new SparseArray<>();
    SparseArray<String> sparseArrayAJabberId =new SparseArray<>();
    SparseArray<String> sparseArrayjabberId=new SparseArray<>();
    SparseArray<String> sparseArraytokenId=new SparseArray<>();
    SparseArray<String> sparseArrayproposedDate= new SparseArray<>();
    SparseArray<String> sparseArraytimelineStatus=new SparseArray<>();
    SparseArray<String> sparseArraydescription=new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view2);

        recyclerView = findViewById(R.id.recycler_view);

        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .build();
        callApi();

        Bundle b=getIntent().getExtras();

//         img1=findViewById(R.id.img1);
//        userid=b.getString("userId");
//        type=b.getString("type");
//        timestamp=b.getString("timestamp");
//        lat=b.getString("lat");
//        lon=b.getString("lon");
//        title=b.getString("title");
//        imageurl=b.getString("imageurl");
//        address=b.getString("address");
//        postalCode=b.getString("postalCode");

    }

    private void callApi() {
//        url
        final String url = "http://52.230.17.234:8124/authcompaccept";
        String authUserName="Prashobh";
        int pincode=302001;
        String se= url.concat("/");
        String se1=se.concat(authUserName);
        String se2=se1.concat("/");
        final String furl=se2.concat(String.valueOf(pincode));


        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

//                postdata=new JSONObject();
//                try {
//                    postdata.put("authUserName","Prashobh");
//                    postdata.put("pincode","401404");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


              //  body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                request = new Request.Builder()
                        .url(furl)
                        .get()
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
               Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();
                ApiSucess(result);
                //tv.setText(result);
            }

        }.execute();
    }



    private void ApiSucess(String result) {

//        JSONArray arr = null;
//        try {
//            arr = new JSONArray(result);
//            for(int i=0;i<arr.length();i++){
//                JSONObject jObj = arr.getJSONObject(i);
//                String longitude = jObj.getString("longitude");
//                String userId = jObj.getString("userId");
//                String imageUrl = jObj.getString("imageUrl");
//                String latitude = jObj.getString("latitude");
//               String title = jObj.getString("complaintTitle");
//                String rating = jObj.getString("rating");
//                String type = jObj.getString("type");
//               String deviceId = jObj.getString("deviceId");
//                String timestamp = jObj.getString("timestamp");
//                String phoneNo = jObj.getString("phoneNo");
//                String issueDate=jObj.getString("issuDate");
//                String pincode=jObj.getString("pincode");
//                String location=jObj.getString("location");
//                String priority=jObj.getString("priority");
//                String photoUrl=jObj.getString("photoUrl");
//                String emailId=jObj.getString("emailId");
//                String AJabberId=jObj.getString("AJabberId");
//                String jabberId=jObj.getString("jabberId");
//                String tokenId= jObj.getString("tokenId");
//                String proposedDate=jObj.getString("proposedDate");
//                String timelineStatus=jObj.getString("timlineStatus");
//                String description=jObj.getString("description");
//                //Toast.makeText(getBaseContext(), phoneNo, Toast.LENGTH_LONG).show();
//                Log.i("----------jabber",title.toString());
//
//                sparseArrayTitle.put(count1, title);
//                sparseArrayimageUrl.put(count1, imageUrl);
//
//                sparseArraylat.put(count1, latitude);
//                sparseArraylong.put(count1, longitude);
//                sparseArrayuserId.put(count1, userId);
//                sparseArraytype.put(count1, type);
//                sparseArrayTimestamp.put(count1, timestamp);
//               sparseArrayimageRating.put(count1, rating);
//                sparseArraydeviceId.put(count1,deviceId);
//                sparseArrayissueDate.put(count1,issueDate);
//                sparseArraypincode.put(count1,pincode);
//                sparseArraylocation.put(count1,location);
//                sparseArraypriority.put(count1,priority);
//                sparseArrayphotoUrl.put(count1,photoUrl);
//                sparseArrayemailId.put(count1,emailId);
//                sparseArrayAJabberId.put(count1,AJabberId);
//                sparseArrayjabberId.put(count1,jabberId);
//                sparseArraytokenId.put(count1,tokenId);
//                sparseArrayproposedDate.put(count1,proposedDate);
//                sparseArraytimelineStatus.put(count1,timelineStatus);
//                sparseArraydescription.put(count1,description);
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//
        try {
            JSONArray jsonArray = new JSONArray(result);

            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String longitude = jsonObject1.optString("longitude");
                String userId = jsonObject1.optString("userId");
                String imageUrl = jsonObject1.optString("imageUrl");
                String type = jsonObject1.optString("type");
                String latitude = jsonObject1.optString("latitude");
                String deviceId = jsonObject1.optString("deviceId");
                String timestamp = jsonObject1.optString("timestamp");
                String title = jsonObject1.optString("complaintTitle");
                String rating = jsonObject1.optString("rating");
               // String status = jsonObject1.optString("status");
                String issueDate=jsonObject1.optString("issuDate");
                String pincode=jsonObject1.optString("pincode");
                String location=jsonObject1.optString("location");
                String priority=jsonObject1.optString("priority");
                String photoUrl=jsonObject1.optString("photoUrl");
                String emailId=jsonObject1.optString("emailId");
                String AJabberId=jsonObject1.optString("AJabberId");
                String jabberId=jsonObject1.optString("jabberId");
                String tokenId= jsonObject1.optString("tokenId");
                String proposedDate=jsonObject1.optString("proposedDate");
                String timelineStatus=jsonObject1.optString("timlineStatus");
                String description=jsonObject1.optString("description");
               //Toast.makeText(getApplicationContext(),title,Toast.LENGTH_LONG).show();

                sparseArrayTitle.put(count1, title);
                sparseArrayimageUrl.put(count1, imageUrl);
                sparseArrayimageRating.put(count1, rating);
                sparseArraylat.put(count1, latitude);
                sparseArraylong.put(count1, longitude);
                sparseArrayuserId.put(count1, userId);
                sparseArrayTimestamp.put(count1, timestamp);
                sparseArraytype.put(count1, type);
                sparseArraydeviceId.put(count1,deviceId);
                sparseArrayissueDate.put(count1,issueDate);
                sparseArraypincode.put(count1,pincode);
                sparseArraylocation.put(count1,location);
                sparseArraypriority.put(count1,priority);
                sparseArrayphotoUrl.put(count1,photoUrl);
                sparseArrayemailId.put(count1,emailId);
                sparseArrayAJabberId.put(count1,AJabberId);
                sparseArrayjabberId.put(count1,jabberId);
                sparseArraytokenId.put(count1,tokenId);
                sparseArrayproposedDate.put(count1,proposedDate);
                sparseArraytimelineStatus.put(count1,timelineStatus);
                sparseArraydescription.put(count1,description);


                count1++;
                //Log.d("---------------", title);
            }

            int i;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0;i < sparseArrayimageRating.size();i++) {
            imageFunction(String.valueOf(i + 1), sparseArrayTitle.size(), sparseArrayTitle.get(i, "Error in name"), sparseArrayimageUrl.get(i, "Error in points"), sparseArrayimageRating.get(i, "Error in points"), sparseArraylat.get(i, "Error in points"), sparseArraylong.get(i, "Error in points"), sparseArrayuserId.get(i, "Error in points"), sparseArrayTimestamp.get(i, "Error in points"), sparseArraytype.get(i, "Error in points"),sparseArraydeviceId.get(i,"error"),sparseArrayissueDate.get(i,"error"),sparseArraypincode.get(i,"error"),sparseArraylocation.get(i,"error"),sparseArraypriority.get(i,"error"),sparseArrayphotoUrl.get(i,"error"),sparseArrayemailId.get(i,"error"),sparseArrayAJabberId.get(i,"error"),sparseArrayjabberId.get(i,"error"),sparseArraytokenId.get(i,"error"),sparseArrayproposedDate.get(i,"error"),sparseArraytimelineStatus.get(i,"error"),sparseArraydescription.get(i,"error"));
        }
    }


    public void imageFunction(String s, int size, String title, String imgUrl, String rating, String lat, String lon, String userId, String timestamp, String type,String deviceId,String issueDate,String pincode,String location,String prioroty,String photoUrl,String emailId,String AJabberId,String jabberId,String tokenId,String proposedDate,String timelineStatus,String description) {
        new LoadImageTask(this, s, size, title, imgUrl, rating, lat, lon, userId, timestamp, type,deviceId,issueDate,pincode,location,prioroty,photoUrl,emailId,AJabberId,jabberId,tokenId,proposedDate,timelineStatus,description).execute(imgUrl);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap, String s, int size, String title, String imgUrl, String rating, String lat, String lon, String userId, String timestamp, String type,String deviceId,String issueDate,String pincode,String location,String prioroty,String photoUrl,String emailId,String AJabberId,String jabberId,String tokenId,String proposedDate,String timelineStatus,String description) throws JSONException, InterruptedException
    {
        if (s == String.valueOf(size)) {

            //Toast.makeText(getApplicationContext(),"end",Toast.LENGTH_SHORT).show();
            // image_id[count],Name,email[count],mobile[count]
            list.add(new Values (lat, lon, userId, title, type, rating, timestamp, imgUrl, deviceId,issueDate,pincode,location,prioroty,photoUrl,emailId,AJabberId,jabberId,tokenId,proposedDate,timelineStatus,description, bitmap));
            //Toast.makeText(getBaseContext(),s + name + email,Toast.LENGTH_SHORT).show();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ValuesAdapter2(this, list));
            //dialog.cancel();
        }
        else {
            list.add(new Values (lat, lon, userId, title, type, rating, timestamp, imgUrl,deviceId,issueDate,pincode,location,prioroty,photoUrl,emailId,AJabberId,jabberId,tokenId,proposedDate,timelineStatus,description, bitmap));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ValuesAdapter2(this, list));
        }
//        img1.setImageBitmap(bitmap);

    }

   @Override
    public void onError() {

    }
}
