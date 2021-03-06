package com.revuc.speedacm.ucwhatididthere.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.revuc.speedacm.ucwhatididthere.R;
import com.revuc.speedacm.ucwhatididthere.adapters.ScreenSlidePagerAdapter;
import com.revuc.speedacm.ucwhatididthere.util.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends FragmentActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String Stamps = "stampsKey";
    SharedPreferences sharedpreferences;

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";


    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "Poop";
    private ViewPager mPager;
    private TextView mTextView;
    private PagerAdapter mAdapter;
    private NfcAdapter mNfcAdapter;
    protected JSONObject mUserData;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(!sharedpreferences.contains("UserId")){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        //grab user info
        String url = "http://52.32.85.146:8080/api/users/" + sharedpreferences.getString("UserId","");
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
                            mUserData = new JSONObject(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    handleUserData();
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



        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new ScreenSlidePagerAdapter( getFragmentManager(),getApplicationContext());
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null){
            //Stop here,we definitely need NFC
            Toast.makeText(this, "This device does't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
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

    private void handleUserData() {
        if(mUserData == null){
            Log.d("pop", "Error");
        }else{
            try {
                Log.d(TAG,mUserData.toString());
                String id = mUserData.getString("_id");
                String email = mUserData.getString("email");
                ArrayList<String> stamps = new ArrayList<String>();
                JSONArray array = mUserData.getJSONArray("stamps");
                if (array != null) {
                    int len = array.length();
                    for (int i=0;i<len;i++){
                        stamps.add(array.get(i).toString());
                    }
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("email", email);
                Set<String> set = sharedpreferences.getStringSet("stamps", new HashSet<String>());
                set.addAll(stamps);
                editor.putStringSet("stamps", set);
                editor.apply();

            } catch (JSONException e) {
                logException(e);
            }


        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        Log.d(TAG,"I got here");
        handleUserData();
        handleIntent(getIntent());

    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        Log.d(TAG,"TA;");
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //TODO: handle Intent
        Log.d(TAG,"test");
        String action = intent.getAction();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){

            String type = intent.getType();
            if(MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            } else {
                Log.d(TAG, "Wrong mine type: " + type);
            }
        }else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d("Poop",result);
                Intent intent = new Intent(getApplicationContext(),NewStamp.class);
                intent.putExtra(EXTRA_MESSAGE, result);
                startActivity(intent);
                finish();
               // mTextView.setText("Read content: " + result);
            }
        }


    }


}
