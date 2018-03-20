package com.example.ht.citizenservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by CGT on 09-02-2018.
 */

public class FireBaseDataReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = new Bundle();
        b.putString("data", String.valueOf(intent.getBundleExtra("data")));
        intent.putExtras(b);
    }
}
