package com.example.ht.citizenservice;

import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.bl.BubbleLayout;
import com.github.vipulasri.timelineview.TimelineView;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeLineViewActv extends AppCompatActivity {

    String json, complaint, status, AJabberId;
    JSONObject jsonObject;

    TimelineView line1, line2, line3, line4;
    BubbleLayout bubble1;
    TextView text1, text2, text3, text4;
    TextView date1, date2, date3, date4;

    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line_view_actv);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line4 = findViewById(R.id.line4);

        bubble1 = findViewById(R.id.bubble1);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);

        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        date3 = findViewById(R.id.date3);
        date4 = findViewById(R.id.date4);

        b = getIntent().getExtras();
        complaint = b.getString("complaintTitle");
        status = b.getString("timelineStatus");
        AJabberId = b.getString("AJabberId");


        Log.i("------json", AJabberId);

        /*
        try {
            jsonObject = new JSONObject(json);
            complaint = jsonObject.getString("complaintTitle");
            status = jsonObject.getString("timelineStatus");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setBackground(getDrawable(R.drawable.dribblinkbot));
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.chatlogo));
        //fab.setBackgroundColor(getResources().getColor(R.color.compBack));
        //fab.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.chatlogo));
        fab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.chatlogo));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent i = new Intent(getBaseContext(), XmppChat.class);
                Bundle bundle = new Bundle();
                bundle.putString("AJabberId", AJabberId);
                i.putExtras(b);
                startActivity(i);
            }
        });

        if (status.equals("0")) {
            line1.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_active));

            //ObjectAnimator objectAnimator = ObjectAnimator.ofInt(bubble1, "backgroundColor", Color.WHITE, Color.RED, Color.WHITE);
            //objectAnimator.setDuration(800);
            //objectAnimator.setEvaluator(new ArgbEvaluator());
            //objectAnimator.setRepeatMode(Animation.REVERSE);
            //objectAnimator.setRepeatCount(Animation.INFINITE);
            //objectAnimator.start();

        }
        else if (status.equals("1")) {
            // strike
            //text1.setPaintFlags(text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            // marker
            line1.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
            line2.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_active));
            }
        else if (status.equals("2")) {
            //text1.setPaintFlags(text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //text2.setPaintFlags(text2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            line1.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
            line2.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
            line3.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_active));
        }
        else if (status.equals("3")) {
            //text1.setPaintFlags(text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //text2.setPaintFlags(text2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //text3.setPaintFlags(text3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            line1.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
            line2.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
            line3.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
            line4.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_active));
        }
        else if (status.equals("4")) {
            //text1.setPaintFlags(text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //text2.setPaintFlags(text2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //text3.setPaintFlags(text3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //text4.setPaintFlags(text4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            line1.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
            line2.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
            line3.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
            line4.setMarker(getResources().getDrawable(R.drawable.timeline_ic_marker_inactive));
        }
    }

    /*
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.chat,m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat:
                //Toast.makeText(getBaseContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(), XmppChat.class));
                //finish();
                return true;
            default:
                return false;
        }
    }
    */

}
