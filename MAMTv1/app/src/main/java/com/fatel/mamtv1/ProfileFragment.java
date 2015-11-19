package com.fatel.mamtv1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {
    CircleImageView propic;
    TextView user;
    TextView firstname;
    TextView lastname;
    Button editname;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        propic = (CircleImageView)view.findViewById(R.id.profile_image_p);
        user = (TextView)view.findViewById(R.id.edituser);
        firstname = (TextView)view.findViewById(R.id.editproname);
        lastname = (TextView)view.findViewById(R.id.editsurproname);
        editname = (Button)view.findViewById(R.id.edit);
        String tempid = UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentFacebookId();
        if(!tempid.equals("0.0")) {
            if(!tempid.equals("0")) {
                if(!(tempid.equals("fb0.0"))) {
                    tempid = tempid.substring(0, 1) + tempid.substring(2, 17);
                    Glide.with(this).load("https://graph.facebook.com/" + tempid + "/picture?type=large").into(propic);
                }
            }
        }
        Log.i("username",UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUser().getUserName());
        Log.i("firstname",UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUser().getFirstName()+"");
        Log.i("FBname",UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUser().getFacebookFirstName()+"");
        Log.i("Lastname",UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUser().getLastName()+"");

        if(!(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUsername()+"").equals("null"))
            user.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUsername());
        if(!(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentFirstName()+"").equals("null"))
            firstname.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentFirstName());
        else if(!(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentFacebookFirstName()+"").equals("null"))
            firstname.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentFacebookFirstName());
        if(!(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentLastName()+"").equals("null"))
            lastname.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentLastName());

        return view;
    }
}
