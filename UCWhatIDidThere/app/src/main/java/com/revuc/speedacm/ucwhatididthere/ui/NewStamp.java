package com.revuc.speedacm.ucwhatididthere.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.count);
        mStamp = (ImageView)findViewById(R.id.thumbnail);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String url = "http://52.32.85.146:8080/api/stamps/"+message;
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
                String url = mStampData.getString("url");
                String uuid = mStampData.getString("_id");
                mTitle.setText(title);
                mDescription.setText(description);
                Picasso.with(this)
                        .load(url)
                        .fit()
                        .transform(new SketchFilterTransformation(this))
                        .placeholder(R.drawable.progress_animation)
                        .into(mStamp);

                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();

                Set<String> set = sharedpreferences.getStringSet("stamps", new HashSet<String>());

                //set.add(uuid);
                //editor.putStringSet("stamps", set);
                //editor.apply();
                ArrayList test = new ArrayList<String>(set);
                test.add(uuid);
                set.addAll(test);
                editor.putStringSet("stamps", set);
                editor.apply();
                for(int i = 0; i < set.size(); i++){
                    Log.d(TAG,test.get(i).toString());
                }

                Map<String,?> keys = sharedpreferences.getAll();

                for(Map.Entry<String,?> entry : keys.entrySet()){
                    Log.d("map values",entry.getKey() + ": " +
                            entry.getValue().toString());
                }


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
