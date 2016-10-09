package com.revuc.speedacm.ucwhatididthere.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.revuc.speedacm.ucwhatididthere.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    protected TextView mSignUpTextView;
    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLoginButton;
    protected JSONObject mStampData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); Deprecated
        setContentView(R.layout.activity_login);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if(actionbar != null) {
            actionbar.hide();
        }


        mUsername = (EditText)findViewById(R.id.userNameField);
        mPassword = (EditText)findViewById(R.id.passwordField);
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                username = username.trim();
                password = password.trim();

                if(username.isEmpty() || password.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("error");
                    builder.setTitle("error");
                    builder.setPositiveButton(android.R.string.ok,null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    String url = "http://52.32.85.146:8080/api/authenticate";
                    if(isNetworkAvailable()) {
                        OkHttpClient client = new OkHttpClient();
                        FormBody.Builder formBuilder = new FormBody.Builder()
                                .add("email", username);

// dynamically add more parameter like this:
                        formBuilder.add("password", password);

                        RequestBody formBody = formBuilder.build();
                        Request request = new Request.Builder()
                                .url(url)
                                .post(formBody)
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
                        Toast.makeText(getApplication(), "NETWORK Unavlaiabel", Toast.LENGTH_LONG).show();
                    }

                    Toast.makeText(getApplication(),"Logged in",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void handleBlogResponse() {
        if(mStampData == null){
            Log.d("pop", "Error");
        }else{
            try {
                Log.d(TAG,mStampData.toString());
                if(mStampData.isNull("success")) {
                    SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("UserId", mStampData.getString("uuid"));
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplication(),"Logged in failed",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                logException(e);
            }


        }
    }

    private void logException(Exception e) {
        Log.e(TAG, "Exception Caught!", e);
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
