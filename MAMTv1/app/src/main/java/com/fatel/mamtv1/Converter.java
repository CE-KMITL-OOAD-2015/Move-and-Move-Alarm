package com.fatel.mamtv1;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oat90 on 25/10/2558.
 */
public class Converter {
    private Gson gson;
    public static Converter jToH = null;

    public Converter() {
        gson = new Gson();
    }

    public static Converter getInstance()
    {
        if(jToH == null) {
            jToH = new Converter();
        }
        return jToH;
    }

    public HashMap<String,Object> JSONToHashMap(String json) {
        HashMap<String,Object> map = new HashMap<>();
        map = gson.fromJson(json,map.getClass()); //convert from JSON to HashMap
        return map;
    }

    public String HashMapToJSON(HashMap<String, Object> map) {
        return gson.toJson(map); //convert HashMap to JSON
    }

    public String HashMapArrayToJSON(HashMap<String, Object>[] arrayOfMap, String key)
    {
        HashMap<String, Object> container = new HashMap<>();
        container.put(key, arrayOfMap);
        return HashMapToJSON(container);
    }


    public ArrayList<HashMap<String, Object>> toHashMapArrayList(Object JSON)
    {
        Type listType = new TypeToken<List<HashMap<String, Object>>>(){}.getType();
        List<HashMap<String, Object>> list = gson.fromJson(JSON.toString(), listType);
        ArrayList<HashMap<String, Object>> temp = new ArrayList<>(list.size());
        temp.addAll(list);
        return temp;
    }

    public String toString(Object value)
    {
        if(value != null) {
            return value.toString();
        }
        else {
            return null;
        }
    }

    public int toInt(Object value)
    {
        try {
            String temp = "" + value.toString();
            Double temp2 = Double.parseDouble(temp);
            return temp2.intValue();
        }
        catch (Exception e) {
            Log.i("conversion error", e.toString());
            return -1;
        }
    }

    public double toDouble(Object value) {
        try {
            String temp = "" + value.toString();
            Double temp2 = Double.parseDouble(temp);
            return temp2.doubleValue();
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}