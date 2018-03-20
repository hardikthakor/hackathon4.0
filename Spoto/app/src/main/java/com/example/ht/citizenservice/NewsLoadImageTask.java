package com.example.ht.citizenservice;

/**
 * Created by CGT on 15-02-2018.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class NewsLoadImageTask extends AsyncTask<String, Void, Bitmap>  {

    public NewsLoadImageTask(Listener listener) {

        mListener = listener;
    }

    public interface Listener {

        void onImageLoaded(Bitmap bitmap) throws JSONException, InterruptedException;
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
                mListener.onImageLoaded(bitmap);
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
