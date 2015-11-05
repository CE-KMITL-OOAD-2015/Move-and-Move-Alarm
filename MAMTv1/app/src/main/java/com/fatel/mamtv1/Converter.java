package com.fatel.mamtv1;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oat90 on 25/10/2558.
 */
public class Converter {
    private Gson gson;
    public static Converter jToH = null;

    public Converter() {
        gson = new Gson();
    }

    public HashMap<String,Object> JsonToHashMap(String json) {
        HashMap<String,Object> map = new HashMap<>();
        map = gson.fromJson(json,map.getClass());
        return map;
    }

    public String HashMapToJson(HashMap<String,Object> map) {
        return gson.toJson(map);
    }

    public static Converter getInstance()
    {
        if(jToH == null) {
            jToH = new Converter();
        }
        return jToH;
    }
}