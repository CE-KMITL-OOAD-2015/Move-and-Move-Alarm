package com.fatel.mamtv1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ScoreboardUserFragment extends Fragment {
    TextView user1;
    TextView user2;
    TextView user3;
    TextView user4;
    TextView user5;
    TextView user6;
    TextView user7;
    TextView user8;
    TextView user9;
    TextView user10;

    public static ScoreboardUserFragment newInstance() {
        // Required empty public constructor
        ScoreboardUserFragment fragment = new ScoreboardUserFragment();
        return fragment;
    }

    public ScoreboardUserFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scoreboard_user, container, false);
        user1 = (TextView)rootView.findViewById(R.id.textView22);
        user2 = (TextView)rootView.findViewById(R.id.textView32);
        user3 = (TextView)rootView.findViewById(R.id.textView42);
        user4 = (TextView)rootView.findViewById(R.id.textView52);
        user5 = (TextView)rootView.findViewById(R.id.textView62);
        user6 = (TextView)rootView.findViewById(R.id.textView72);
        user7 = (TextView)rootView.findViewById(R.id.textView82);
        user8 = (TextView)rootView.findViewById(R.id.textView92);
        user9 = (TextView)rootView.findViewById(R.id.textView102);
        user10 = (TextView)rootView.findViewById(R.id.textView112);
        return rootView;
    }


}
