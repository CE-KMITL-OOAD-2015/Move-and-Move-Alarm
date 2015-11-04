package movealarm.kmitl.net;

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

    public HashMap<String, Object>[] ModelArrayToHashMapArray(Model[] models)
    {
        ArrayList<HashMap<String, Object>> mapList = new ArrayList<>();

        for(int i = 0; i < models.length; i++)
            mapList.add(models[i].getValues());

        HashMap<String, Object>[] arrayOfMap = mapList.toArray((new HashMap[mapList.size()]));

        return arrayOfMap;
    }

    public String HashMapArrayToJSON(HashMap<String, Object>[] arrayOfMap, String key)
    {
        HashMap<String, Object> container = new HashMap<>();
        container.put(key, arrayOfMap);
        return HashMapToJson(container);
    }
}
