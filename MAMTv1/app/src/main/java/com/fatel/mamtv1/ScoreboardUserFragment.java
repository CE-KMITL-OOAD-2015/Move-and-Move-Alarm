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


public class ScoreboardUserFragment extends Fragment {

    TextView user0;
    TextView score0;
    TextView ranking0;

    public ScoreboardUserFragment() {
    }

    public static ScoreboardUserFragment newInstance() {
        // Required empty public constructor
        ScoreboardUserFragment fragment = new ScoreboardUserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scoreboard_user, container, false);
        final ArrayList<TextView> usersTextView = new ArrayList<>();
        final ArrayList<TextView> scoresTextView = new ArrayList<>();
        usersTextView.add((TextView) rootView.findViewById(R.id.textView22));
        usersTextView.add((TextView) rootView.findViewById(R.id.textView32));
        usersTextView.add((TextView) rootView.findViewById(R.id.textView42));
        usersTextView.add((TextView) rootView.findViewById(R.id.textView52));
        usersTextView.add((TextView) rootView.findViewById(R.id.textView62));
        usersTextView.add((TextView) rootView.findViewById(R.id.textView72));
        usersTextView.add((TextView) rootView.findViewById(R.id.textView82));
        usersTextView.add((TextView) rootView.findViewById(R.id.textView92));
        usersTextView.add((TextView) rootView.findViewById(R.id.textView102));
        usersTextView.add((TextView) rootView.findViewById(R.id.textView112));
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
        user0 = (TextView) rootView.findViewById(R.id.userscore);
        score0 = (TextView) rootView.findViewById(R.id.scoreuser);
        ranking0 = (TextView) rootView.findViewById(R.id.userranking);
        String tempid = UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentFacebookId();
        Log.i("iduserscore",tempid);
        //Log.i("username",UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUsername());
        if (!tempid.equals("fb0.0")) {
            if (!tempid.equals("fb0")) {
                if(!tempid.equals("0")){
                    if(!tempid.equals("0.0")){
                        user0.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentFacebookFirstName());
                    }
                    else {
                        user0.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUsername());
                    }
                }
                else {
                    user0.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUsername());
                }
            }
            else {
                user0.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUsername());
            }
        } else {
            user0.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentUsername());
        }
        Log.i("score", UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentScore() + "");
        score0.setText(UserManage.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).getCurrentScore() + "");
        String url = HttpConnector.URL + "user/findByRank";
        String url2 = HttpConnector.URL + "user/getUserRank";

        StringRequest userScoreboardRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley", response);
                        Converter converter = Converter.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        try {
                            if ((boolean) data.get("status")) {
                                ArrayList<HashMap<String, Object>> users = converter.toHashMapArrayList(data.get("users"));
                                int size = users.size();
                                Log.i("amount", "" + size);
                                for (int i = 0; i < size; i++) {
                                    HashMap<String, Object> userData = users.get(i);
                                    String userName = converter.toString(userData.get("userName"));
                                    String name = (userName == null || userName.equals("")) ? converter.toString(userData.get("facebookFirstName")) : userName;
                                    int score = converter.toInt(userData.get("score"));
                                    usersTextView.get(i).setText(name);
                                    scoresTextView.get(i).setText("" + score);
                                }
                            }
                        } catch (Exception e) {
                            Log.i("error", e.toString());
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

        StringRequest userRankingRequest = new StringRequest(Request.Method.POST, url2, //create new string request with POST method
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
                HashMap<String, Object> usermap = new HashMap<>();
                usermap.put("id", user.getIdUser());
                map.put("JSON", Converter.getInstance().HashMapToJSON(usermap));
                return map;
            }
        };
        HttpConnector.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).addToRequestQueue(userRankingRequest);


        return rootView;
    }


}
