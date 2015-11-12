package com.fatel.mamtv1;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends android.support.v4.app.Fragment {


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        String id="";
        if(getArguments()!=null) {
            id = getArguments().getString("id");
        }
//        propic = (CircleImageView)view.findViewById(R.id.profile_image_f);
//        Log.i("xx", propic.toString());
//        propic.setVisibility(View.VISIBLE);
//        Log.i("xx", "do?");
//        Glide.with(this).load("https://graph.facebook.com/" + id + "/picture?type=large").into(propic);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


}
