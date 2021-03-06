package com.example.ht.citizenservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by CGT on 12-02-2018.
 */

public class ComplaintsLoadImageTask extends AsyncTask<String, Void, Bitmap> {

        String number;
        int size;
        String complaintTitle;
        String timelineStatus;
        String AJabberId;

public ComplaintsLoadImageTask(Listener listener, String number, int size, String complaintTitle, String timelineStatus, String AJabberId) {

        mListener = listener;
        this.number = number;
        this.size = size;
        this.complaintTitle = complaintTitle;
        this.timelineStatus = timelineStatus;
        this.AJabberId = AJabberId;
        }

public interface Listener{

    void onImageLoaded(Bitmap bitmap, String s, int size, String complaintTitle, String timelineStatus, String AJabberId) throws JSONException, InterruptedException;
    void onError();
}

    private ComplaintsLoadImageTask.Listener mListener;
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
                mListener.onImageLoaded(bitmap, number, size, complaintTitle, timelineStatus, AJabberId);
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
