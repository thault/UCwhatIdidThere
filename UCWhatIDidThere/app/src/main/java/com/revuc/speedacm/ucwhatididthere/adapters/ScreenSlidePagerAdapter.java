package com.revuc.speedacm.ucwhatididthere.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.revuc.speedacm.ucwhatididthere.util.ScreenSlidePagerFragment;
import com.revuc.speedacm.ucwhatididthere.util.Stamp;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by samnwosu on 10/8/16.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    protected List<Stamp> mStamps;

    public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mStamps = new ArrayList<Stamp>();
        Stamp testStamp = new Stamp("test","http://i.imgur.com/OgZD9Ax.png","","","");
        mStamps.add(testStamp);
        mStamps.add(testStamp);
    }


    @Override
    public Fragment getItem(int position) {
        return ScreenSlidePagerFragment.create(position);
    }

    @Override
    public int getCount() {
        return mStamps.size();
    }



}

