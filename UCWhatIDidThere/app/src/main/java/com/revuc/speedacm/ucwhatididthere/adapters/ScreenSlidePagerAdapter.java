package com.revuc.speedacm.ucwhatididthere.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.revuc.speedacm.ucwhatididthere.util.Stamp;

import java.util.List;


/**
 * Created by samnwosu on 10/8/16.
 */

public class ScreenSlidePagerAdapter extends ArrayAdapter<Stamp> {

    protected Context mContext;
    protected List<Stamp> mStamps;

    public ScreenSlidePagerAdapter(Context context, List<Stamp> stamps){
        super(context, android.R.layout.activity_list_item,stamps);
        mContext = context;
        mStamps = stamps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        return null;

    }

    private static class ViewHolder {
        ImageView userImageView;
        ImageView checkImageView;
        TextView nameLabel;
    }

}

