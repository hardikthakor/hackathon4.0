package com.example.dell.authorityapp;

/**
 * Created by Dell on 14/02/2018.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
public class LeaderBoardLoadImageTask extends AsyncTask<String, Void, Bitmap> {

    String number;
    int size;
    String name;
    String points;

    public LeaderBoardLoadImageTask(Listener listener, String number, int size, String name, String points) {

        mListener = listener;
        this.number = number;
        this.size = size;
        this.name = name;
        this.points = points;
    }

    public interface Listener{

        void onImageLoaded(Bitmap bitmap, String s, int size, String name, String points) throws JSONException, InterruptedException;
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
                mListener.onImageLoaded(bitmap, number, size, name, points);
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
