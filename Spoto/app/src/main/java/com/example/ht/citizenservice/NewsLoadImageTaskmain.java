package com.example.ht.citizenservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by CGT on 15-02-2018.
 */

public class NewsLoadImageTaskmain extends AsyncTask<String, Void, Bitmap> {
    String s;
    int size;
    String title;
    String imgUrl;
    String rating;
    String lat;
    String lon;
    String userId;
    String timestamp;
    String type;
    String deviceId;
    String issueDate;
    String pincode;
    String location;
    String priority;
    String phothoUrl;
    String emailId;
    String JabberId;
    String jabberId;
    String tokenId;
    String proposedDate;
    String timlineStatus;
    String description;

    public NewsLoadImageTaskmain(Listener listener, String s, int size, String title, String url, String rating, String lat, String lon, String userId, String timestamp, String type, String deviceId,String issueDate,String pincode,String location, String priority,String phothoUrl,String emailId,String JabberId,String jabberId,String tokenId,String proposedDate,String timlineStatus,String description) {

        mListener = listener;
        this.s = s;
        this.size = size;
        this.title = title;
        this.imgUrl = url;
        this.rating = rating;
        this.lat = lat;
        this.lon = lon;
        this.userId = userId;
        this.timestamp = timestamp;
        this.type = type;
        this.deviceId = deviceId;
        this.issueDate=issueDate;
        this.pincode=pincode;
        this.location=location;
        this.priority=priority;
        this.phothoUrl=phothoUrl;
        this.emailId=emailId;
        this.JabberId=JabberId;
        this.jabberId=jabberId;
        this.tokenId=tokenId;
        this.proposedDate=proposedDate;
        this.timlineStatus=timlineStatus;
        this.description=description;
    }

    public interface Listener {

        void onImageLoaded(Bitmap bitmap, String s, int size, String title, String imgUrl, String rating, String lat, String lon, String userId, String timestamp, String type, String deviceId, String issueDate, String pincode, String location, String priority, String phothoUrl, String emailId, String JabberId, String jabberId, String tokenId, String proposedDate, String timlineStatus, String description) throws JSONException, InterruptedException;
        void onError();
    }

    private Listener mListener;
    @Override
    protected Bitmap doInBackground(String... args) {

        try {

            return BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (bitmap != null) {

            try {
                mListener.onImageLoaded(bitmap, s, size, title, imgUrl, rating, lat, lon, userId, timestamp, type, deviceId,issueDate,pincode,location,priority,phothoUrl,emailId,JabberId,jabberId,tokenId,proposedDate,timlineStatus,description);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {

            mListener.onError();
        }
    }
}
