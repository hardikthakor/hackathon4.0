package com.example.ht.citizenservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Category extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    public static final String storageConnectionString = "DefaultEndpointsProtocol=https;"
            + "AccountName=hackathonpdf;"
            + "AccountKey=n01ALbUky6U4LSotdpOvs/GSimmLhO9wzXLFuTnqp5IVeFhMcFFJRQSsRMWPGHLznoiQK0GZ+VbaBhx8pslJ5g==";

    CloudStorageAccount storageAccount;
    CloudBlobClient blobClient;
    CloudBlobContainer container;
    CloudBlockBlob blob;

    File file;

    // api
    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    JSONObject jobj;
    RequestBody body;
    Request request;
    Response responseapi;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    TextView textView;

    private TextToSpeech tts;
    private Button btnSpeak;
    private EditText txtText;
    private ImageView captureImag;
    private TextView translatedText;
    private Button uploadSignBoard;

    Spinner inputSpinner, targetSpinner;
    String sourceSelected, targetSelected;

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    //Camera
    String filename;
    long localTimestamp;
    String localTimestampstr;
    String type = "";
    File filecam;
    static final int CAM_REQUEST = 1;

    String imgString;
    Bitmap bmp;

    ShimmerFrameLayout shi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        uploadSignBoard = findViewById(R.id.uploadSignBoard);
        captureImag = findViewById(R.id.captureImag);
        translatedText = findViewById(R.id.translatedText);
        shi = findViewById(R.id.shiTrans);

        textView = findViewById(R.id.textView);
        inputSpinner = findViewById(R.id.spinner1);
        targetSpinner = findViewById(R.id.spinner2);
        //create a list of items for the spinner.
        final String[] input = new String[]{"English", "Hindi", "Gujrati", "France", "Spanish"};
        final String[] target = new String[]{"English", "Hindi", "Marathi", "France", "Spanish"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        // There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, input);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, target);
        //set the spinners adapter to the previously created one.
        inputSpinner.setAdapter(adapter1);
        targetSpinner.setAdapter(adapter2);


        inputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        sourceSelected = "en-IN";
                        break;
                    case 1:
                        sourceSelected = "hi-IN";
                        break;
                    case 2:
                        sourceSelected = "gu-IN";
                        break;
                    case 3:
                        sourceSelected = "fr-FR";
                        break;
                    case 4:
                        sourceSelected = "es-ES";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        targetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        targetSelected = "en";
                        break;
                    case 1:
                        targetSelected = "hi";
                        break;
                    case 2:
                        targetSelected = "mr";
                        break;
                    case 3:
                        targetSelected = "fr";
                        break;
                    case 4:
                        targetSelected = "es";
                        break;
                }

                //Log.d("----", targetSelected);

                //targetSelected = target[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tts = new TextToSpeech(this, this);

        btnSpeak = (Button) findViewById(R.id.btnSpeak);

        txtText = (EditText) findViewById(R.id.txtText);

        // button on click event
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                speakOut();
            }

        });

        uploadSignBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera();
            }
        });

        setButtonHandlers();
        enableButtons(false);

        bufferSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    private void setButtonHandlers() {
        ((Button)findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button)findViewById(R.id.btnStop)).setOnClickListener(btnClick);
    }

    private void enableButton(int id,boolean isEnable){
        ((Button)findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart,!isRecording);
        enableButton(R.id.btnStop,isRecording);
    }

    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/audiorecordtest" + AUDIO_RECORDER_FILE_EXT_WAV);
    }

    private String getTempFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        File tempFile = new File(filepath,AUDIO_RECORDER_TEMP_FILE);

        if(tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private void startRecording(){
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);

        int i = recorder.getState();
        if(i==1)
            recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                writeAudioDataToFile();
            }
        },"AudioRecorder Thread");

        recordingThread.start();
    }

    private void writeAudioDataToFile(){
        byte data[] = new byte[bufferSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        int read = 0;

        if(null != os){
            while(isRecording){
                read = recorder.read(data, 0, bufferSize);

                if(AudioRecord.ERROR_INVALID_OPERATION != read){
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecording(){
        if(null != recorder){
            isRecording = false;

            int i = recorder.getState();
            if(i==1)
                recorder.stop();
            recorder.release();

            uploadAudioBlob();

            recorder = null;
            recordingThread = null;
        }

        copyWaveFile(getTempFilename(),getFilename());
        deleteTempFile();
    }
    private void deleteTempFile() {
        File file = new File(getTempFilename());

        file.delete();
    }

    private void copyWaveFile(String inFilename,String outFilename){
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 2;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            srcAppLog.logString("File size: " + totalDataLen);

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while(in.read(data) != -1){
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BPP; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnStart:{
                    srcAppLog.logString("Start Recording");

                    enableButtons(true);
                    startRecording();

                    break;
                }
                case R.id.btnStop:{
                    srcAppLog.logString("Start Recording");

                    enableButtons(false);
                    stopRecording();

                    break;
                }
            }
        }
    };

    private void uploadAudioBlob() {

        Thread th = new Thread(new Runnable() {
            public void run() {

                try {
                    storageAccount = CloudStorageAccount.parse(storageConnectionString);
                    blobClient = storageAccount.createCloudBlobClient();
                    container = blobClient.getContainerReference("audio");
                    container.createIfNotExists();
                    blob = container.getBlockBlobReference("audiorecordtest.wav");
                    blob.uploadFromFile(file.getAbsolutePath() + "/audiorecordtest.wav");
                    callApi();

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (StorageException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});

        th.start();
    }

    //callApi
    public void callApi() {
        //Enter the URL here
        //final String url = "http://" + getString(R.string.mongoIp) + ":8004/insertion";
        String url = "http://52.187.6.152:8012/translateApp";

        final String finalUrl = url;

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                postdata = new JSONObject();
                try {
                    postdata.put("uri", "https://hackathonpdf.blob.core.windows.net/audio/audiorecordtest.wav");
                    postdata.put("language", targetSelected);
                    postdata.put("srclanguage", sourceSelected);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.d("------------", postdata.toString());

                body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                request = new Request.Builder()
                        .url(finalUrl)
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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String text = jsonObject.getString("text");
                    textView.setText(text);
                    speakOut();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("----", result);
                //ApiSucess(result);
            }

        }.execute();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Locale loc=new Locale("hin");

            int result = tts.setLanguage(loc);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                btnSpeak.setEnabled(true);
                Log.e("TTS", "speaking");
                //speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {

        CharSequence text = textView.getText();

        // "en", "hi", "mr", "fr", "es"
        String pLang = "mr";

        switch (targetSelected) {
            case "en":
                text = textView.getText();
                pLang = "en";
                break;
            case "hi":
                text = textView.getText();
                pLang = "hi-In";
                break;
            case "mr":
                text = textView.getText();
                pLang = "mr";
                break;
            case "fr":
                text = textView.getText();
                pLang = "fr";
                break;
            case "es":
                text = textView.getText();
                pLang = "spa";
                break;
        }
        //pLang = "mr";
        //text = txtText.getText();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //selectedLanguage = sharedpreferences.getString("selectedLanguage", "eng");
            //Log.e("TTS",selectedLanguage);
            Locale loc=new Locale(pLang);
            int result = tts.setLanguage(loc);

            tts.setSpeechRate(1);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"id1");
        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void camera(){
        type = "signBoard";
        localTimestamp = getLocalToUtc();
        localTimestampstr = Long.toString(localTimestamp);
        filename = type + "-" + localTimestampstr + ".jpg";

        filecam = new File(Environment.getExternalStorageDirectory() + "/CitizenService/", filename);

        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cam.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filecam));
        cam.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(cam, CAM_REQUEST);
    }

    //Cam Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            //Toast.makeText(getBaseContext(),"fail",Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == CAM_REQUEST) {
            dataSetter();
            translatedText.setVisibility(View.VISIBLE);
            translatedText.setBackground(getDrawable(R.drawable.blueopacity));
            shi.setDuration(1000);
            shi.setDropoff(1);
            shi.setAutoStart(true);
        }
    }

    public long getLocalToUtc() {
        final Date toTimestamp = new Date();
        final long timestamp = toTimestamp.getTime();
        return timestamp;
    }

    public void byteConvert () {
        imgString = Base64.encodeToString(getBytesFromBitmap(bmp),Base64.NO_WRAP);
        //Toast.makeText(getBaseContext(),imgString, Toast.LENGTH_SHORT).show();
        //Log.d("bitmap------------", imgString);
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public void dataSetter() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        captureImag.setImageBitmap(bmp);
                    }
                });

                filecam = new File(Environment.getExternalStorageDirectory() + "/CitizenService/", filename);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                bmp = BitmapFactory.decodeFile(filecam.toString(), options);
                //bmp = getResizedBitmap(bmp,10000);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                try {
                    OutputStream fOut = null;
                    fOut = new FileOutputStream(filecam);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
                    fOut.flush(); // Not really required
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        captureImag.setImageBitmap(bmp);
                    }
                });

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                byteConvert();
                callApiSignBoard();
            }
        }.execute();
    }

    //callApi
    public void callApiSignBoard() {

        //Enter the URL here
        //final String url = "http://" + getString(R.string.mongoIp) + ":8004/insertion";
        String url = "http://52.187.6.152:8017/s3upload";

        final String finalUrl = url;

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {


                postdata = new JSONObject();
                try {
                    postdata.put("filename", filename);
                    postdata.put("imageByte", imgString);
                    postdata.put("type", "signBoard");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.d("------------", postdata.toString());

                body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                request = new Request.Builder()
                        .url(finalUrl)
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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                ApiSucess(result);
            }

        }.execute();
    }

    private void ApiSucess(String result) {

        String text = null;

        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                text = jsonObject.getString("text");
                Log.d("----", result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (text != null)
            translatedText.setText(text);
        else
            translatedText.setText("No ResultFound");
        shi.setAutoStart(false);
    }
}
