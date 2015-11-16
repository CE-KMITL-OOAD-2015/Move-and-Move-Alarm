package com.fatel.mamtv1;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {
CircleImageView propic;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        propic = (CircleImageView)view.findViewById(R.id.profile_image_p);
        Glide.with(this).load("https://graph.facebook.com/" + UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentFacebookId() + "/picture?type=large").into(propic);
        return view;
    }


}
