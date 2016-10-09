package com.revuc.speedacm.ucwhatididthere.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.revuc.speedacm.ucwhatididthere.ui.MainActivity;
import com.revuc.speedacm.ucwhatididthere.util.ScreenSlidePagerFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by samnwosu on 10/8/16.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    protected List<String> mStamps;

    public ScreenSlidePagerAdapter(FragmentManager fragmentManager,Context context) {
        super(fragmentManager);
        mStamps = new ArrayList<String>();
        SharedPreferences sharedpreferences = context.getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        Set<String> set = sharedpreferences.getStringSet("stamps", new HashSet<String>());
        mStamps.addAll(set);

    }


    @Override
    public Fragment getItem(int position) {
        return ScreenSlidePagerFragment.create(position,mStamps.get(position));
    }

    @Override
    public int getCount() {
        return mStamps.size();
    }



}

