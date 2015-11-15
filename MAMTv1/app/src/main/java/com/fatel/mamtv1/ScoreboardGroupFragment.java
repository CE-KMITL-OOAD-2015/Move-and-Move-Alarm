package com.fatel.mamtv1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScoreboardGroupFragment extends Fragment {


    public static ScoreboardGroupFragment newInstance() {
        // Required empty public constructor
        ScoreboardGroupFragment fragment = new ScoreboardGroupFragment();
        return fragment;
    }

    public ScoreboardGroupFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scoreboard_group, container, false);
        return rootView;
    }

}
