package com.revuc.speedacm.ucwhatididthere.util;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.revuc.speedacm.ucwhatididthere.R;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.gpu.SketchFilterTransformation;

/**
 * Created by samnwosu on 10/9/16.
 */

public class ScreenSlidePagerFragment extends Fragment{
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private Stamp testStamp;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePagerFragment create(int pageNumber) {

        ScreenSlidePagerFragment fragment = new ScreenSlidePagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        testStamp = new Stamp("test","http://i.imgur.com/OgZD9Ax.png","","","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        Context c = getActivity().getApplicationContext();
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        // Set the title view to show the page number.
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(testStamp.getName());
        Picasso.with(c)
                .load("http://i.imgur.com/vjUDNwz.png")
                .placeholder(R.drawable.progress_animation)
                .transform(new SketchFilterTransformation(c))
                .into((ImageView) rootView.findViewById(R.id.imageView));

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

}
