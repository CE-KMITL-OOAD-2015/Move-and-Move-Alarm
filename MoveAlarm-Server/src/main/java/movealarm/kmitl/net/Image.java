package movealarm.kmitl.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by oat90 on 31/10/2558.
 */
public class Image extends Model
{
    private String name = null;
    private int imageData = 0;
    private String description = null;

    public Image() {
        this.tableName = "image";
        this.requiredFields = new ArrayList<>();
        addRequiredField("name");
        addRequiredField("imageData");
    }

    public static Image find(int id)
    {
        HashMap<String,Object> img_map = modelCollection.find("image",id);
        if(img_map == null) {
            return null;
        }
        Image model = new Image();
        model.createdDate = (Date)img_map.get("createdDate");
        model.id = (int)img_map.get("id");
        model.name = (String)img_map.get("name");
        model.imageData = (int)img_map.get("imageData");
        model.description = (String)img_map.get("description");
        model.modifiedDate = (Date)img_map.get("modifiedDate");
        return model;
    }

    public static Image[] where(String colName,String operator,String value)
    {
        ArrayList<HashMap<String, Object>> img_arr = modelCollection.where("image",colName,operator,value);
        ArrayList<Image> collection = new ArrayList<>();
        for(HashMap<String, Object> item : img_arr) {
            Image model = new Image();
            model.createdDate = (Date)item.get("createdDate");
            model.id = (int)item.get("id");
            model.name = (String)item.get("name");
            model.imageData = (int)item.get("imageData");
            model.description = (String)item.get("description");
            model.modifiedDate = (Date)item.get("modifiedDate");
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
        ArrayList<HashMap<String, Object>> img_arr = modelCollection.all("image");
        ArrayList<Image> collection = new ArrayList<>();
        for(HashMap<String, Object>item : img_arr) {
            Image model = new Image();
            model.createdDate = (Date)item.get("createdDate");
            model.id = (int)item.get("id");
            model.name = (String)item.get("name");
            model.imageData = (int)item.get("imageData");
            model.description = (String)item.get("description");
            model.modifiedDate = (Date)item.get("modifiedDate");
            collection.add(model);
        }
        return collection.toArray(new Image[collection.size()]);
    }

    public HashMap<String, Object> changeImage(String name,int imageData,String description)
    {
        setName(name);
        setimageData(imageData);
        setDescription(description);
        updateModifiedDate();
        return save();
    }

    public HashMap<String,Object> getValues()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HashMap<String,Object> img_map = new HashMap<>();
        img_map.put("name",this.name);
        img_map.put("imageData",this.imageData);
        img_map.put("description",this.description);
        if(this.modifiedDate == null) {
            img_map.put("modifiedDate",null);
        }
        else {
            img_map.put("modifiedDate",sdf.format(this.modifiedDate));
        }
        return img_map;
    }

    @Override
    public HashMap<String, Object> getGeneralValues() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HashMap<String,Object> img_map = new HashMap<>();
        img_map.put("name",this.name);
        img_map.put("imageData",this.imageData);
        img_map.put("description",this.description);
        if(this.modifiedDate == null) {
            img_map.put("modifiedDate",null);
        }
        else {
            img_map.put("modifiedDate",sdf.format(this.modifiedDate));
        }
        return img_map;
    }

    public void setName(String name)
    {
        this.name = name;
        updateModifiedDate();
    }

    public void setimageData(int imageData)
    {
        this.imageData = imageData;
        updateModifiedDate();
    }

    public void setDescription(String description)
    {
        this.description = description;
        updateModifiedDate();
    }

    public String getName()
    {
        return this.name;
    }

    public int getimageData()
    {
        return this.imageData;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getModifiedDate()
    {
        return modifiedDate;
    }

    @Override
    public HashMap<String, Object> delete()
    {
        return StatusDescription.createProcessStatus(modelCollection.delete(this));
    }
}
