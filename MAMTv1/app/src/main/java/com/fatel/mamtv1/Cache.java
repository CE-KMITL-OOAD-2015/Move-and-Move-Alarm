package com.fatel.mamtv1;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Forunh on 11/15/2015.
 */

public class Cache {

    private static Cache instance;
    private Map<String, Object> cache;

    private Cache()
    {
        cache = new HashMap<>();
    }

    public static Cache getInstance()
    {
        if(instance == null)
            instance = new Cache();
        return instance;
    }

    public void putData(String key, Object data)
    {
        cache.put(key, data);
    }

    public Object getData(String key)
    {
        return cache.get(key);
    }
}

