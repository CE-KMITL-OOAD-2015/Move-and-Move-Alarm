package movealarm.kmitl.net;

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

    public Image() {
        this.tableName = "image";
        this.requiredFields = new ArrayList<>();
        this.requiredFields.add("imgData");
    }

    public static Model find(int id)
    {
        HashMap<String,Object> img_map = modelCollection.find("image",id);
        if(img_map == null) {
            return null;
        }
        Image model = new Image();
        model.name = (String)img_map.get("name");
        model.imgData = (int)img_map.get("imgData");
        model.modifiedDate = (Date)img_map.get("modified_date");
        return model;
    }

    public static Image[] where(String colName,String operator,String value)
    {
        ArrayList<HashMap<String, Object>> img_arr = modelCollection.where("image", colName, operator, value);
        ArrayList<Image> collection = new ArrayList<>();
        for(HashMap<String, Object> item : img_arr) {
            Image model = new Image();
            model.name = (String)item.get("name");
            model.imgData = (int)item.get("imgData");
            model.modifiedDate = (Date)item.get("modified_date");
            collection.add(model);
        }
        return collection.toArray(new Image[collection.size()]);
    }

    public static Image[] where(String colName, String operator, String value,String extraCondition)
    {
        return where(colName, operator, value + " " + extraCondition);
    }

    public static Image[] all()
    {
        ArrayList<HashMap<String, Object>> img_arr = modelCollection.all("image");
        ArrayList<Image> collection = new ArrayList<>();
        for(HashMap<String, Object>item : img_arr) {
            Image model = new Image();
            model.name = (String)item.get("name");
            model.imgData = (int)item.get("imgData");
            model.modifiedDate = (Date)item.get("modified_date");
            collection.add(model);
        }
        return collection.toArray(new Image[collection.size()]);
    }

    public HashMap<String, Object> changeImage(String name,int imgData)
    {
        setName(name);
        setImgData(imgData);
        updateModifiedDate();
        return StatusDescription.createProcessStatus(modelCollection.save(this));
    }

    public HashMap<String,Object> getValues()
    {
        HashMap<String,Object> img_map = new HashMap<>();
        img_map.put("name", this.name);
        img_map.put("imgData", this.imgData);
        img_map.put("modified_date", this.modifiedDate);
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

<<<<<<< aa9ec6c019a05932189a2a8b31eb100e009eadcf
    public HashMap<String, Object> getGeneralValues()
    {
        return new HashMap<>();
    }

=======
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
>>>>>>> [#34]Continued implementing image model.
}
