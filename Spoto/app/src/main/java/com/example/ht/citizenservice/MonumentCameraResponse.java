package com.example.ht.citizenservice;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MonumentCameraResponse extends AppCompatActivity {

    TextView namePlace, desplace;
    Bundle b;

    String name = "", des = "", result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_camera_response);

        namePlace = findViewById(R.id.namePlace);
        desplace = findViewById(R.id.desPlace);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/amitaregular.ttf");
        namePlace.setTypeface(typeface);
        desplace.setTypeface(typeface);

        b = getIntent().getExtras();

        if (b != null) {
            result = b.getString("name");
            try {
                JSONObject jsonObject = new JSONObject(result);
                name = jsonObject.getString("place");
                des = jsonObject.getString("desc");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        namePlace.setText(name);
        desplace.setText(des);

    }
}
