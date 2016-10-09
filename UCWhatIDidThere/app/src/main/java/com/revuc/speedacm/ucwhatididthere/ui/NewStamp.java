package com.revuc.speedacm.ucwhatididthere.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.revuc.speedacm.ucwhatididthere.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import jp.wasabeef.picasso.transformations.gpu.SketchFilterTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class NewStamp extends Activity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private TextView mTitle;
    private ImageView mStamp;
    private TextView mDescription;
    protected JSONObject mStampData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stamp);

        mTitle = (TextView) findViewById(R.id.textView4);
        //mDescription = (TextView) findViewById(R.id.textView5);
        mStamp = (ImageView)findViewById(R.id.imageView2);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String url = "http://192.168.43.219:8080/api/stamps/57f9badce7401e0680ea44ab";
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    logException(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Log.v(TAG, "We got a response!");
                        if (response.isSuccessful()) {
                            String jsonData = response.body().string();
                            mStampData = new JSONObject(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    handleBlogResponse();
                                }
                            });
                        } else {
                            Log.i(TAG, "Error Code:"+response.code() );
                        }
                    } catch (Exception e) {
                        logException(e);

                    }

                }
            });
        }else{
            Toast.makeText(this, "NETWORK Unavlaiabel", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }

    private void logException(Exception e) {
        Log.e(TAG, "Exception Caught!", e);
    }


    private void handleBlogResponse() {
        if(mStampData == null){
            Log.d("pop", "Error");
        }else{
            try {
                String title = mStampData.getString("name");
                String description = mStampData.getString("description");
                mTitle.setText(title);
                mDescription.setText(description);
                Picasso.with(this)
                        .load("http://i.imgur.com/DvpvklR.png")
                        .fit()
                        .transform(new SketchFilterTransformation(this))
                        .placeholder(R.drawable.progress_animation)
                        .into(mStamp);
            } catch (JSONException e) {
                logException(e);
            }
        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }



}
