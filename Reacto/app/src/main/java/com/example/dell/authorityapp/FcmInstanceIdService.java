package com.example.dell.authorityapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Dell on 08/02/2018.
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService {
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;
    public FcmInstanceIdService() {
        super();
    }

    @Override
    public void onTokenRefresh() {

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        sharedpreferences = getApplicationContext().getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Log.i("------------",recent_token);
        editor.putString("tokenFirebase", recent_token);
        editor.commit();
    }
}
