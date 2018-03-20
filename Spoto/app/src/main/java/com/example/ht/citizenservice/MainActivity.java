package com.example.ht.citizenservice;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;
import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Button bt;
    String tkn;
    Button SignIn;


    public GoogleApiClient mgoogleApiClient;
    private static final int REQ_CODE = 9001;
    Account[] accounts;
    String tokenaws;
    CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncClient;
    Dataset dataset;

    //private SignInButton SignIn;
    private Button signout;
    private TextView tv;
    ImageView im;

    // Shared Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    // fb
    LoginButton loginButton;
    CallbackManager callbackManager;
    String fbtoken;
    Profile profile;

    String name;
    String email;
    String prof_url;
    String first_name;
    String last_name;

    // api
    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        /*
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        */

        //Paint shadow = new Paint();
        //shadow.setShadowLayer(10.0f, 0.0f, 2.0f, R.color.black);
        //SignIn.setLayerPaint(shadow);

        // fb
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton = findViewById(R.id.fb);
        callbackManager = CallbackManager.Factory.create();

        tv = (TextView) findViewById(R.id.tv);
        bt = (Button) findViewById(R.id.bt);
        SignIn = findViewById(R.id.bt_login);
        signout = (Button) findViewById(R.id.bt_signout);
        im = (ImageView) findViewById(R.id.photo);

        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_SHORT).show();
                fbsignIn(loginResult);
            }

            @Override
            public void onCancel() {
                //Toast.makeText(getApplicationContext(),"Failed to Login",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                //Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });

        bt.setOnClickListener(this);
        SignIn.setOnClickListener(this);
        signout.setOnClickListener(this);

       // tkn = FirebaseInstanceId.getInstance().getToken();

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        //SharedPreferences.Editor editor = sharedpreferences.edit();
        //editor.putString("tokenFirebase", tkn);
        //editor.commit();

        //Toast.makeText(getBaseContext(),tkn,Toast.LENGTH_SHORT).show();
        // 124336757180-mfdj7ub58vqb7vs3v11h9sje263sup6f.apps.googleusercontent.com
        // 362805170548-6c5j7gvujp92sf7jbfa2196bfnvdli30.apps.googleusercontent.com

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestId().requestIdToken("197710807993-m5jgti3if485lnfo7eg2d7bsu77b5qqs.apps.googleusercontent.com")
                .build();

        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        if (!sharedpreferences.getBoolean("logincheck", false)) {
            //Toast.makeText(getBaseContext(),"signed",Toast.LENGTH_SHORT).show();
        } else {
            // not signed in. Show the "sign in" button and explanation.
            // ...
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
            finish();
        }

        /*
        // old
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:3d459157-037c-4a79-8ec8-77bb65ede51c", // Identity pool ID
                Regions.AP_SOUTH_1 // Region
        );
        */

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:28c00a21-1a9e-49a7-9df2-7036dad8c86f", // Identity pool ID
                Regions.AP_SOUTH_1 // Region
        );

        // ap-south-1_cHZJi98H4

        syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.AP_SOUTH_1, // Region
                credentialsProvider);
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt:
                Intent i = new Intent(getBaseContext(), DashBoard.class);
                startActivity(i);
                //startActivity(new Intent(getBaseContext(), GridActv.class));
                finish();
                break;
            case R.id.bt_login:
                /*
                tkn = FirebaseInstanceId.getInstance().getToken();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("tokenFirebase", tkn);
                editor.putString("profileName", "hardik thakor");
                editor.putString("email", "hardikthakor.ht@gmail.com");
                editor.putString("profileImageUrl", "https://lh5.googleusercontent.com/-fFK1dScz0PA/AAAAAAAAAAI/AAAAAAAAAKE/YnxSdX1JU2Q/s96-c/photo.jpg");
                editor.putBoolean("logincheck", true);
                editor.putString("jaberId", "hardikthakor.ht");
                editor.commit();

                Intent i1 = new Intent(getBaseContext(), DashBoard.class);
                startActivity(i1);
                */
                sigIn();
                break;
            case R.id.bt_signout:
                signOut();
                break;
        }

    }

    private void fbsignIn(LoginResult loginResult){

        profile = Profile.getCurrentProfile();

        Map<String, String> logins = new HashMap<String, String>();
        fbtoken = String.valueOf(loginResult.getAccessToken().getToken());
        logins.put("graph.facebook.com", fbtoken);
        credentialsProvider.clear();
        credentialsProvider.setLogins(logins);

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            email = response.getJSONObject().getString("email");
                            name = profile.getName();
                            prof_url = String.valueOf(profile.getProfilePictureUri(200, 200));
                            first_name = profile.getFirstName();
                            last_name = profile.getLastName();

                            //Log.i("-----------email", object.toString());

                            final String[] jaber = email.split("\\@");

                            tkn = FirebaseInstanceId.getInstance().getToken();

                            Log.i("--------token", tkn);

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("tokenFirebase", tkn);
                            editor.putString("profileName", name);
                            editor.putString("email", email);
                            editor.putString("profileImageUrl", prof_url);
                            editor.putBoolean("logincheck", true);
                            editor.putString("jabberId", jaber[0]);
                            editor.commit();

                            if (!sharedpreferences.getBoolean("firsttime", false)) {

                                final String url = getString(R.string.mainactv);

                                new AsyncTask<Void, Void, String>() {

                                    @Override
                                    protected String doInBackground(Void... voids) {
                                        /*
                                        MongoClient m = new MongoClient(getResources().getString(R.string.ip));
                                        DB db = m.getDB(getResources().getString(R.string.db));
                                        DBCollection dbCollection = db.getCollection(getResources().getString(R.string.col));
                                        BasicDBObject doc = new BasicDBObject();
                                        doc.put("profName", name);
                                        doc.put("points", 50);
                                        doc.put("profUrl", prof_url);
                                        doc.put("profEmail", email);
                                        doc.put("complaintsCount", 0);
                                        doc.put("tokenId", tkn);
                                        dbCollection.insert(doc);
                                        m.close();
                                        return null;
                                        */

                                        postdata = new JSONObject();
                                        try {
                                            postdata.put("profName", name);
                                            postdata.put("points", 50);
                                            postdata.put("profUrl", prof_url);
                                            postdata.put("profEmail", email);
                                            postdata.put("complaintsCount", 0);
                                            postdata.put("deviceId", tkn);
                                            postdata.put("jabberId", jaber[0] + "@localhost");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.d("------------", postdata.toString());

                                        body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                                        Request request;

                                        request = new Request.Builder()
                                                .url(url)
                                                .post(body)
                                                .addHeader("Content-Type", "application/json")
                                                .build();

                                        responseapi = null;
                                        try {
                                            responseapi = client.newCall(request).execute();
                                            DataPayload = responseapi.body().string();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        return DataPayload;

                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        SharedPreferences.Editor editor1 = sharedpreferences.edit();
                                        editor1.putBoolean("firsttime", true);
                                        editor1.commit();
                                    }
                                }.execute();
                            }
                            startActivity(new Intent(getBaseContext(), DashBoard.class));
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dataset = syncClient.openOrCreateDataset("Userdata");
                        dataset.put("fb-name", name);
                        dataset.put("fb-email", email);
                        dataset.put("FireBaseToken", tkn);
                        syncDataset(dataset);

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void sigIn(){
        Intent i = Auth.GoogleSignInApi.getSignInIntent(mgoogleApiClient);
        startActivityForResult(i, REQ_CODE);
    }
    public void signOut(){
        Auth.GoogleSignInApi.signOut(mgoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                //Toast.makeText(getBaseContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                revokeAccess();
            }
        });

    }
    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount acc = result.getSignInAccount();
            name = acc.getDisplayName();
            email = acc.getEmail();
            final Uri ur = acc.getPhotoUrl();
            tokenaws = acc.getIdToken();

            tkn = FirebaseInstanceId.getInstance().getToken();

            Log.i("--------token", tkn);

            final String[] jaber = email.split("\\@");

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("tokenFirebase", tkn);
            editor.putString("profileName", name);
            editor.putString("email", email);
            editor.putString("profileImageUrl", ur.toString());
            editor.putBoolean("logincheck", true);
            editor.putString("jabberId", jaber[0]);
            editor.commit();

            if (!sharedpreferences.getBoolean("firsttime", false)) {

                final String url = getString(R.string.mainactv);

                final String jabberId = jaber[0];

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        /*
                        MongoClient m = new MongoClient(getResources().getString(R.string.ip));
                        DB db = m.getDB(getResources().getString(R.string.db));
                        DBCollection dbCollection = db.getCollection(getResources().getString(R.string.col));
                        BasicDBObject doc = new BasicDBObject();
                        doc.put("profName",name);
                        doc.put("points",50);
                        doc.put("profUrl",ur.toString());
                        doc.put("profEmail",email);
                        doc.put("complaintsCount",0);
                        doc.put("tokenId", tkn);
                        dbCollection.insert(doc);
                        m.close();
                        return null;
                        */
                        postdata = new JSONObject();
                        try {
                            postdata.put("profName", name);
                            postdata.put("points", 50);
                            postdata.put("profUrl", ur.toString());
                            postdata.put("profEmail", email);
                            postdata.put("complaintsCount", 0);
                            postdata.put("deviceId", tkn);
                            postdata.put("jabberId", jabberId + "@localhost");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("------------", postdata.toString());

                        body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                        Request request;

                        request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .addHeader("Content-Type", "application/json")
                                .build();

                        responseapi = null;
                        try {
                            responseapi = client.newCall(request).execute();
                            DataPayload = responseapi.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return DataPayload;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        SharedPreferences.Editor editor1 = sharedpreferences.edit();
                        editor1.putBoolean("firsttime",true);
                        editor1.commit();

                    }


                }.execute();
            }
            startActivity(new Intent(getBaseContext(), DashBoard.class));
            finish();
            Map<String, String> logins = new HashMap<String, String>();
            logins.put("accounts.google.com", tokenaws);
            credentialsProvider.setLogins(logins);

            Dataset dataset = syncClient.openOrCreateDataset("Userdata");
            dataset.put("Google-name", name);
            dataset.put("Google-email", email);
            dataset.put("FireBaseToken", tkn);

            syncDataset(dataset);

        }
    }

    private void syncDataset(final Dataset dataset){
        final Activity activity = this;

        dataset.synchronize(new DefaultSyncCallback() {
            public void onSuccess(final Dataset dataset, List newrecords){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String t = dataset.get("test");
                        //tv.setText(t);
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mgoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Toast.makeText(getApplicationContext(),"Failed to connect ... ",Toast.LENGTH_SHORT).show();
    }

}
