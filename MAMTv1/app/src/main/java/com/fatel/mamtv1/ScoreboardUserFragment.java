package com.fatel.mamtv1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


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
    TextView score1;
    TextView score2;
    TextView score3;
    TextView score4;
    TextView score5;
    TextView score6;
    TextView score7;
    TextView score8;
    TextView score9;
    TextView score10;

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
        score1 = (TextView)rootView.findViewById(R.id.textView23);
        score2 = (TextView)rootView.findViewById(R.id.textView33);
        score3 = (TextView)rootView.findViewById(R.id.textView43);
        score4 = (TextView)rootView.findViewById(R.id.textView53);
        score5 = (TextView)rootView.findViewById(R.id.textView63);
        score6 = (TextView)rootView.findViewById(R.id.textView73);
        score7 = (TextView)rootView.findViewById(R.id.textView83);
        score8 = (TextView)rootView.findViewById(R.id.textView93);
        score9 = (TextView)rootView.findViewById(R.id.textView103);
        score10 = (TextView)rootView.findViewById(R.id.textView113);
        String url = "http://203.151.92.196:8080/user/findByRank";

        StringRequest userScoreboardRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley", response);
                        Converter converter = Converter.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        try {
                            if((boolean) data.get("status"))
                            {
                                ArrayList<HashMap<String, Object>> users = converter.toHashMapArrayList(data.get("users"));
                                user1.setText(converter.toString(users.get(0).get("userName")));
                                score1.setText(converter.toString(users.get(0).get("score")));
                                user2.setText(converter.toString(users.get(1).get("userName")));
                                score2.setText(converter.toString(users.get(1).get("score")));
                                user3.setText(converter.toString(users.get(2).get("userName")));
                                score3.setText(converter.toString(users.get(2).get("score")));
                                user4.setText(converter.toString(users.get(3).get("userName")));
                                score4.setText(converter.toString(users.get(3).get("score")));
                                user5.setText(converter.toString(users.get(4).get("userName")));
                                score5.setText(converter.toString(users.get(4).get("score")));
                                user6.setText(converter.toString(users.get(5).get("userName")));
                                score6.setText(converter.toString(users.get(5).get("score")));
                                user7.setText(converter.toString(users.get(6).get("userName")));
                                score7.setText(converter.toString(users.get(6).get("score")));
                                user8.setText(converter.toString(users.get(7).get("userName")));
                                score8.setText(converter.toString(users.get(7).get("score")));
                                user9.setText(converter.toString(users.get(8).get("userName")));
                                score9.setText(converter.toString(users.get(8).get("score")));
                                user10.setText(converter.toString(users.get(9).get("userName")));
                                score10.setText(converter.toString(users.get(9).get("score")));
//                            for(HashMap<String, Objects> item : users) {
//
//                            }
                            }
                        } catch (Exception e) {
                            Log.i("scoreboard error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>(); //create map to keep variables
                map.put("startRank", "1");
                map.put("endRank", "10");
                return map;
            }
        };

        HttpConnector.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).addToRequestQueue(userScoreboardRequest);
        return rootView;
    }


}
