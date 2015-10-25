package movealarm.kmitl.net;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by oat90 on 25/10/2558.
 */
public class JSON2HashMap {
    private Gson gson;
    public static JSON2HashMap jToH = null;

    public JSON2HashMap() {
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

    public static JSON2HashMap getInstance()
    {
        if(jToH == null) {
            jToH = new JSON2HashMap();
        }
        return jToH;
    }
}
