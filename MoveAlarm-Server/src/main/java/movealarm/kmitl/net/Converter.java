package movealarm.kmitl.net;

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
    public static Converter instance = null;

    private Converter() { //make constructor as private to implement the class with singleton design pattern
        gson = new Gson();
    }

    public static Converter getInstance() //other classes must use this method to get the object
    {
        if(instance == null) {
            instance = new Converter();
        }
        return instance;
    }

    public HashMap<String,Object> JSONToHashMap(String json) {
        HashMap<String,Object> map = new HashMap<>();
        map = gson.fromJson(json,map.getClass()); //convert from JSON to HashMap
        return map;
    }

    public String HashMapToJSON(HashMap<String, Object> map) {
        return gson.toJson(map); //convert HashMap to JSON
    }

    public HashMap<String, Object>[] ModelArrayToHashMapArray(Model[] models)
    {
        ArrayList<HashMap<String, Object>> mapList = new ArrayList<>();

        for(int i = 0; i < models.length; i++)
            mapList.add(models[i].getGeneralValues());

        HashMap<String, Object>[] arrayOfMap = mapList.toArray((new HashMap[mapList.size()])); //convert ArrayList to normal array

        return arrayOfMap;
    }

    public String HashMapArrayToJSON(HashMap<String, Object>[] arrayOfMap, String key)
    {
        HashMap<String, Object> container = new HashMap<>();
        container.put(key, arrayOfMap);
        return HashMapToJSON(container);
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
            e.printStackTrace();
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

    public ArrayList<HashMap<String, Object>> toHashMapArrayList(HashMap<String, Object> map)
    {
        ArrayList<HashMap<String, Object>> temp_map = new ArrayList<>();
        for(String key : map.keySet()) {
            temp_map = (ArrayList<HashMap<String, Object>>)map.get(key);
        }
        return temp_map;
    }

    public ArrayList<HashMap<String, Object>> JSONtoHashMapArrayList(Object JSON)
    {
        Type listType = new TypeToken<List<HashMap<String, Object>>>(){}.getType();
        List<HashMap<String, Object>> listOfCountry = gson.fromJson(JSON.toString(), listType);
        ArrayList<HashMap<String, Object>> temp = new ArrayList<>(listOfCountry.size());
        temp.addAll(listOfCountry);
        return temp;
    }
}
