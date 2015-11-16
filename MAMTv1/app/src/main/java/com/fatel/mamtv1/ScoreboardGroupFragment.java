package com.fatel.mamtv1;

import android.content.Context;
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

public class ScoreboardGroupFragment extends Fragment {
    TextView group1;
    TextView group2;
    TextView group3;
    TextView group4;
    TextView group5;
    TextView group6;
    TextView group7;
    TextView group8;
    TextView group9;
    TextView group10;
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
        group1 = (TextView)rootView.findViewById(R.id.textView22);
        group2 = (TextView)rootView.findViewById(R.id.textView32);
        group3 = (TextView)rootView.findViewById(R.id.textView42);
        group4 = (TextView)rootView.findViewById(R.id.textView52);
        group5 = (TextView)rootView.findViewById(R.id.textView62);
        group6 = (TextView)rootView.findViewById(R.id.textView72);
        group7 = (TextView)rootView.findViewById(R.id.textView82);
        group8 = (TextView)rootView.findViewById(R.id.textView92);
        group9 = (TextView)rootView.findViewById(R.id.textView102);
        group10 = (TextView)rootView.findViewById(R.id.textView112);
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
        String url = "http://203.151.92.196:8080/group/findByRank";

        StringRequest groupScoreboardRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley", response);
                        Converter converter = Converter.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        Log.i("go","-1");
                        try {
                            if((boolean) data.get("status"))
                            {
                                Log.i("go","0");
                                ArrayList<HashMap<String, Object>> groups = converter.toHashMapArrayList(data.get("groups"));
                                Log.i("go","1");
                                group1.setText(converter.toString(groups.get(0).get("name")));
                                score1.setText(converter.toString(groups.get(0).get("score")));
                                group2.setText(converter.toString(groups.get(1).get("name")));
                                score2.setText(converter.toString(groups.get(1).get("score")));
                                group3.setText(converter.toString(groups.get(2).get("name")));
                                score3.setText(converter.toString(groups.get(2).get("score")));
                                Log.i("go", "2");
                                group4.setText(converter.toString(groups.get(3).get("name")));
                                score4.setText(converter.toString(groups.get(3).get("score")));
                                group5.setText(converter.toString(groups.get(4).get("name")));
                                score5.setText(converter.toString(groups.get(4).get("score")));
                                group6.setText(converter.toString(groups.get(5).get("name")));
                                score6.setText(converter.toString(groups.get(5).get("score")));
                                group7.setText(converter.toString(groups.get(6).get("name")));
                                score7.setText(converter.toString(groups.get(6).get("score")));
                                group8.setText(converter.toString(groups.get(7).get("name")));
                                score8.setText(converter.toString(groups.get(7).get("score")));
                                group9.setText(converter.toString(groups.get(8).get("name")));
                                score9.setText(converter.toString(groups.get(8).get("score")));
                                group10.setText(converter.toString(groups.get(9).get("name")));
                                score10.setText(converter.toString(groups.get(9).get("score")));
                                Log.i("go", "3");
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
        HttpConnector.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).addToRequestQueue(groupScoreboardRequest);
        return rootView;
    }

}
