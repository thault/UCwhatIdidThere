package com.revuc.speedacm.ucwhatididthere.util;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by samnwosu on 10/9/16.
 */

public class ScreenSlidePagerFragment extends Fragment{
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    public static final String ARG_UUID = "uuid";
    public static final String TAG = "Poop";


    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private String mStampUUID;
    private Stamp testStamp;
    private JSONObject mStamp;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePagerFragment create(int pageNumber, String uuid) {

        ScreenSlidePagerFragment fragment = new ScreenSlidePagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putString(ARG_UUID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        mStampUUID = getArguments().getString(ARG_UUID);
        //stamp  info
        String url = "http://52.32.85.146:8080/api/stamps/" + mStampUUID;
            OkHttpClient client = new OkHttpClient();



            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    logException(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Log.v(TAG, "We got a response!");
                        if (response.isSuccessful()) {
                            String jsonData = response.body().string();
                            mStamp = new JSONObject(jsonData);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    handleTagData();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        Context c = getActivity().getApplicationContext();
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.activity_new_stamp, container, false);

        // Set the title view to show the page number.
        if(testStamp != null) {
            ((TextView) rootView.findViewById(R.id.title)).setText(testStamp.getName());
            ((TextView) rootView.findViewById(R.id.count)).setText(testStamp.getDiscription());
            Picasso.with(c)
                    .load(testStamp.getUrl())
                    .placeholder(R.drawable.progress_animation)
                    .transform(new SketchFilterTransformation(c))
                    .into((ImageView) rootView.findViewById(R.id.thumbnail));
        }

        return rootView;
    }
    private void logException(Exception e) {
        Log.e(TAG, "Exception Caught!", e);
    }

    private void handleTagData() {
        if(mStamp == null){
            Log.d("pop", "Error");
        }else{
            try {
                Log.d(TAG,mStamp.toString());
                String name = mStamp.getString("name");
                String description = mStamp.getString("description");
                String gps = mStamp.getString("gps");
                String url = mStamp.getString("url");
                String UUID = mStamp.getString("_id");
                testStamp = new Stamp(name,url,gps,description,UUID);
                this.getFragmentManager().beginTransaction().detach(this).commit();
                this.getFragmentManager().beginTransaction().attach(this).commit();


                /*SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE)
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(UUID, email);
                Set<String> set = new HashSet<String>();
                set.addAll(stamps);
                editor.putStringSet("stamps", set);
                editor.commit();*/

            } catch (JSONException e) {
                logException(e);
            }

        }
    }


    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

}
