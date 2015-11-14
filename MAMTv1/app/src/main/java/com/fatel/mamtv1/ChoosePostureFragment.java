package com.fatel.mamtv1;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChoosePostureFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    public ChoosePostureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_choose_posture, container, false);
        Button posture1 = (Button)v.findViewById(R.id.post1);
        Button posture2 = (Button)v.findViewById(R.id.post2);
        Button posture3 = (Button)v.findViewById(R.id.post3);
        Button posture4 = (Button)v.findViewById(R.id.post4);
        Button posture5 = (Button)v.findViewById(R.id.post5);
        Button posture6 = (Button)v.findViewById(R.id.post6);
        Button posture7 = (Button)v.findViewById(R.id.post7);
        Button posture8 = (Button)v.findViewById(R.id.post8);
        Button posture9 = (Button)v.findViewById(R.id.post9);
        Log.i("xx", "click0");
        return inflater.inflate(R.layout.fragment_choose_posture, container, false);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), PostureActivity.class);
        intent.putExtra("value",9);
        startActivity(intent);
    }

}
