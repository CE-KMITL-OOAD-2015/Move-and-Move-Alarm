package movealarm.kmitl.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by oat90 on 31/10/2558.
 */
public class Image extends Model
{
    private String name;
    private int imgData;
    private static Image model = null;

    public Image(String name,int imgData)
    {
        this.tableName = "image";
        this.name = name;
        this.imgData = imgData;
    }
    public Image() {
        this.tableName = "image";
        this.name = null;
        this.imgData = 0;
    }

    public static Model find(int id)
    {
        HashMap<String,Object> img_map = modelCollection.find("image",id);
        model = new Image();
        model.name = (String)img_map.get("name");
        model.imgData = (int)img_map.get("imgData");
        model.modifiedDate = (Date)img_map.get("modified_date");
        return model;
    }

    public static Image[] where(String colName,String operator,String value)
    {
        ArrayList<HashMap<String, Object>> img_arr = modelCollection.where("image",colName,operator,value);
        ArrayList<Image> collection = new ArrayList<>();
        for(HashMap<String, Object> item : img_arr) {
            model = new Image();
            model.name = (String)item.get("name");
            model.imgData = (int)item.get("imgData");
            model.modifiedDate = (Date)item.get("modified_date");
            collection.add(model);
        }
        return collection.toArray(new Image[collection.size()]);
    }

    public static Image[] where(String colName, String operator, String value,String extraCondition)
    {
        return where(colName,  operator, value + " " + extraCondition);
    }

    public static Image[] all()
    {
        ArrayList<HashMap<String, Object>> img_arr = modelCollection.all(tableName);
        ArrayList<Image> collection = new ArrayList<>();
        for(HashMap<String, Object>item : img_arr) {
            model = new Image();
            model.name = (String)item.get("name");
            model.imgData = (int)item.get("imgData");
            model.modifiedDate = (Date)item.get("modified_date");
            collection.add(model);
        }
        return collection.toArray(new Image[collection.size()]);
    }

    public void changeImage(String name,int imgData)
    {
        setName(name);
        setImgData(imgData);
        updateModifiedDate();
    }

    public HashMap<String,Object> getValues()
    {
        HashMap<String,Object> img_map = new HashMap<>();
        img_map.put("name",this.name);
        img_map.put("imgData",this.imgData);
        img_map.put("modified_date",this.modifiedDate);
        return img_map;
    }

    public void setName(String name)
    {
        this.name = name;
        updateModifiedDate();
    }

    public void setImgData(int imgData)
    {
        this.imgData = imgData;
        updateModifiedDate();
    }

    public String getName()
    {
        return this.name;
    }

    public int getImgData()
    {
        return this.imgData;
    }

    public Date getModifiedDate()
    {
        return modifiedDate;
    }

}
