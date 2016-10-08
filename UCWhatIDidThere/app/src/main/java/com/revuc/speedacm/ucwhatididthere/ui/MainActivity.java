package com.revuc.speedacm.ucwhatididthere.ui;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.revuc.speedacm.ucwhatididthere.R;

public class MainActivity extends FragmentActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager mPager;
    private PagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = (ViewPager) findViewById(R.id.pager);
        //mAdapter = new ScreenSlidePagerAdapter
        mPager.setAdapter(mAdapter);
        //mPager.setOnPageChangeListener();
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }

    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {

    }


}
