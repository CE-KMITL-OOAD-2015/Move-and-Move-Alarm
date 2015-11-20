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
    TextView group0;
    TextView score0;
    TextView ranking0;

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
        final ArrayList<TextView> groupsTextView = new ArrayList<>();
        final ArrayList<TextView> scoresTextView = new ArrayList<>();
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView22));
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView32));
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView42));
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView52));
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView62));
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView72));
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView82));
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView92));
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView102));
        groupsTextView.add((TextView) rootView.findViewById(R.id.textView112));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView23));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView33));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView43));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView53));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView63));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView73));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView83));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView93));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView103));
        scoresTextView.add((TextView) rootView.findViewById(R.id.textView113));
        group0 = (TextView)rootView.findViewById(R.id.groupscore);
        score0 = (TextView)rootView.findViewById(R.id.scoregroup);
        ranking0 = (TextView)rootView.findViewById(R.id.groupranking);

        String url = HttpConnector.URL + "group/findByRank";
        String url2 = HttpConnector.URL + "group/getGroupRank";
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
                                int size = groups.size();
                                for(int i = 0; i < size; i++) {
                                    HashMap<String, Object> userData = groups.get(i);
                                    String groupName = converter.toString(userData.get("name"));
                                    int score = converter.toInt(userData.get("score"));
                                    groupsTextView.get(i).setText(groupName);
                                    scoresTextView.get(i).setText("" + score);
                                }
                            }
                        } catch (Exception e) {
                            Log.i("scoreboard error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", "on error"+volleyError.toString());
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

        Log.i("group",Cache.getInstance().getData("groupData")+"");
        if((Cache.getInstance().getData("groupData")+"").equals("null")) {
            group0.setText("No Group");
        }else {
            final HashMap<String, Object> groupData = (HashMap<String, Object>) Cache.getInstance().getData("groupData");
            Log.i("nameg",Converter.getInstance().toString(groupData.get("name")));
            group0.setText(Converter.getInstance().toString(groupData.get("name")));
            score0.setText("" + Converter.getInstance().toInt(groupData.get("score")));

            StringRequest groupRankingRequest = new StringRequest(Request.Method.POST, url2, //create new string request with POST method
                    new Response.Listener<String>() { //create new listener to traces the data
                        @Override
                        public void onResponse(String response) { //when listener is activated
                            Log.i("volley", response);
                            Converter converter = Converter.getInstance();
                            Log.i("res", response);
                            // HashMap<String, Object> data = converter.JSONToHashMap(response);
                            try {
                                //if((boolean) data.get("status"))
                                //{
                                ranking0.setText(response);
                                //}
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
                    User user = UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUser();
                    HashMap<String, Object> groupmap = new HashMap<>();
                    groupmap.put("id", groupData.get("id"));
                    map.put("JSON", Converter.getInstance().HashMapToJSON(groupmap));
                    return map;
                }
            };
            HttpConnector.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).addToRequestQueue(groupRankingRequest);
        }
        return rootView;
    }

}
