package com.example.ht.citizenservice;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
;

public class JaipurDescription extends AppCompatActivity implements LoadImageTask.Listener{

    String name = "", description = "", srcDate = "", desDate = "", url = "";

    TextView jaipurDesName, jaipurDesDate, jaipurDes;
    ImageView jaipurDesurl;

    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaipur_description);

        jaipurDesName = findViewById(R.id.jaipurDesname);
        jaipurDesDate = findViewById(R.id.jaipurDestiming);
        jaipurDes = findViewById(R.id.jaipurDesTex);
        jaipurDesurl = findViewById(R.id.jaipurDesImg);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/amitaregular.ttf");
        jaipurDes.setTypeface(typeface);

        b = getIntent().getExtras();

        if (b != null) {
            name = b.getString("name");
            description = b.getString("description");
            srcDate = b.getString("srcDate");
            desDate = b.getString("desDate");
            url = b.getString("url");
        }

        getSupportActionBar().setTitle(name);

        loadData();

    }

    private void imageFunction() {
        new LoadImageTask(this).execute(url);
        jaipurDesName.setText(name);
        String combineDate = srcDate + " - " + desDate;
        jaipurDesDate.setText(combineDate);
        jaipurDes.setText(description);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) throws JSONException, InterruptedException {
        jaipurDesurl.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {

    }

    public void loadData () {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                imageFunction();

            }
        }.execute();
    }
}
