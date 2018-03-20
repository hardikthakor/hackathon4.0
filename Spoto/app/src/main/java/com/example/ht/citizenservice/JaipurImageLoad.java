package com.example.ht.citizenservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by CGT on 18-03-2018.
 */

public class JaipurImageLoad extends AsyncTask<String, Void, Bitmap> {

    String number;
    int size;
    String name;
    String srcDate;
    String description;
    String desDate;
    String url;

    public JaipurImageLoad(Listener listener, String number, int size, String name, String srcDate, String url, String description, String desDate) {
        mListener = listener;
        this.number = number;
        this.size = size;
        this.name = name;
        this.srcDate = srcDate;
        this.url = url;
        this.description = description;
        this.desDate = desDate;
    }

    public interface Listener{

        void onImageLoaded(Bitmap bitmap, String s, int size, String name, String srcDate, String url, String description, String desDate) throws JSONException, InterruptedException;
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
                mListener.onImageLoaded(bitmap, number, size, name, srcDate, url, description, desDate);
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
