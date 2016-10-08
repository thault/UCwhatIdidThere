package com.revuc.speedacm.ucwhatididthere.ui;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
}
