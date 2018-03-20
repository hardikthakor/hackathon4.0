package com.example.ht.citizenservice;

import android.util.Log;

/**
 * Created by CGT on 14-03-2018.
 */

public class srcAppLog {
    private static final String APP_TAG = "AudioRecorder";

    public static int logString(String message){
        return Log.i(APP_TAG,message);
    }
}
