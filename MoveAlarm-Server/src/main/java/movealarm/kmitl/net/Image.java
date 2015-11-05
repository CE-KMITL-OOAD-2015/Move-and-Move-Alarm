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
    private int imgData = 0;
    private String description = null;

    public Image() {
        this.tableName = "image";
        this.requiredFields = new ArrayList<>();
        this.requiredFields.add("name");
        this.requiredFields.add("imgData");
    }

    public static Model find(int id)
    {
        HashMap<String,Object> img_map = modelCollection.find("image",id);
        if(img_map == null) {
            return null;
        }
        Image model = new Image();
        model.id = (int)img_map.get("id");
        model.name = (String)img_map.get("name");
        model.imgData = (int)img_map.get("imgData");
        model.description = (String)img_map.get("description");
        model.modifiedDate = (Date)img_map.get("modified_date");
        return model;
    }

    public static Image[] where(String colName,String operator,String value)
    {
        ArrayList<HashMap<String, Object>> img_arr = modelCollection.where("image",colName,operator,value);
        ArrayList<Image> collection = new ArrayList<>();
        for(HashMap<String, Object> item : img_arr) {
            Image model = new Image();
            model.id = (int)item.get("id");
            model.name = (String)item.get("name");
            model.imgData = (int)item.get("imgData");
            model.description = (String)item.get("description");
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
        ArrayList<HashMap<String, Object>> img_arr = modelCollection.all("image");
        ArrayList<Image> collection = new ArrayList<>();
        for(HashMap<String, Object>item : img_arr) {
            Image model = new Image();
            model.id = (int)item.get("id");
            model.name = (String)item.get("name");
            model.imgData = (int)item.get("imgData");
            model.description = (String)item.get("description");
            model.modifiedDate = (Date)item.get("modified_date");
            collection.add(model);
        }
        return collection.toArray(new Image[collection.size()]);
    }

    public HashMap<String, Object> changeImage(String name,int imgData,String description)
    {
        setName(name);
        setImgData(imgData);
        setDescription(description);
        updateModifiedDate();
        return save();
    }

    public HashMap<String,Object> getValues()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HashMap<String,Object> img_map = new HashMap<>();
        img_map.put("name",this.name);
        img_map.put("imgData",this.imgData);
        if(this.modifiedDate == null) {
            img_map.put("modified_date",null);
        }
        else {
            img_map.put("modified_date",sdf.format(this.modifiedDate));
        }
        return img_map;
    }

    @Override
    public HashMap<String, Object> getGeneralValue() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HashMap<String,Object> img_map = new HashMap<>();
        img_map.put("name",this.name);
        img_map.put("imgData",this.imgData);
        if(this.modifiedDate == null) {
            img_map.put("modified_date",null);
        }
        else {
            img_map.put("modified_date",sdf.format(this.modifiedDate));
        }
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

    public void setDescription(String description)
    {
        this.description = description;
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

    public String getDescription() {
        return this.description;
    }

    public Date getModifiedDate()
    {
        return modifiedDate;
    }

    @Override
    public HashMap<String, Object> save()
    {
        HashMap<String, Object> requireFields = checkRequiredFields();
        if(requireFields != null) {
            return requireFields;
        }
        if(createdDate == null) {
            HashMap<String, Object> temp = modelCollection.create(this);
            if(temp == null) {
                return StatusDescription.createProcessStatus(false,"Cannot save due to database error.");
            }
        }
        return StatusDescription.createProcessStatus(modelCollection.save(this));
    }

    @Override
    public HashMap<String, Object> delete()
    {
        return StatusDescription.createProcessStatus(modelCollection.delete(this));
    }
}
