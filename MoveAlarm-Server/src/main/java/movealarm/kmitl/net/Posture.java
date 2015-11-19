package movealarm.kmitl.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by oat90 on 5/11/2558.
 */
public class Posture extends Model
{
    private int imageData = 0;
    private String title = null;
    private String description = null;

    public Posture()
    {
        this.tableName = "posture";
        this.addRequiredField("imageData");
        this.addRequiredField("title");
    }

    public static Posture find(int id)
    {
        HashMap<String, Object> posture_map = modelCollection.find("posture", id);
        if(posture_map == null) { //if found nothing, return null
            return null;
        }
        Posture model = new Posture(); //create posture and return its value
        model.id = (int)posture_map.get("id");
        model.createdDate = (Date)posture_map.get("createdDate");
        model.imageData = (int)posture_map.get("imageData");
        model.title = (String)posture_map.get("title");
        model.description = (String)posture_map.get("description");
        model.modifiedDate = (Date)posture_map.get("modifiedDate");
        return model;
    }

    public static Posture[] where(String colName, String operator, String value)
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.where("posture",colName,operator,value);
        ArrayList<Posture> collection = new ArrayList<>(); //create array of posture and return its value
        for(HashMap<String, Object> item : temp) {
            Posture model = new Posture();
            model.id = (int)item.get("id");
            model.createdDate = (Date)item.get("createdDate");
            model.imageData = (int)item.get("imageData");
            model.title = (String)item.get("title");
            model.description = (String)item.get("description");
            model.modifiedDate = (Date)item.get("modifiedDate");
            collection.add(model);
        }
        return collection.toArray(new Posture[collection.size()]);
    }

    public static Posture[] where(String colName, String operator, String value,String extraCondition)
    {
        return where(colName, operator, value + " " + extraCondition); //where method with extra condition
    }
    public static Posture[] all()
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.all("posture"); //get all postures
        ArrayList<Posture> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
            Posture model = new Posture();
            model.id = (int)item.get("id");
            model.createdDate = (Date)item.get("createdDate");
            model.imageData = (int)item.get("imageData");
            model.title = (String)item.get("title");
            model.description = (String)item.get("description");
            model.modifiedDate = (Date)item.get("modifiedDate");
            collection.add(model);
        }
        return collection.toArray(new Posture[collection.size()]);
    }

    @Override
    public HashMap<String, Object> getValues()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //return all field of model then, return in Hashmap type
        HashMap<String, Object> posture_map = new HashMap<>();
        posture_map.put("title",this.title);
        posture_map.put("description",this.description);
        posture_map.put("imageData",this.imageData);
        if(this.modifiedDate == null) {
            posture_map.put("modifiedDate", null);
        }
        else {
            posture_map.put("modifiedDate",sdf.format(this.modifiedDate));
        }
        return posture_map;
    }

    @Override
    public HashMap<String, Object> getGeneralValues() { //like getValues method but return only common field
        HashMap<String, Object> posture_map = new HashMap<>();
        posture_map.put("title",this.title);
        posture_map.put("description",this.description);
        posture_map.put("imageData",this.imageData);
        return posture_map;
    }

    public void setImageData(int imageData)
    {
        this.imageData = imageData;
        updateModifiedDate();
    }

    public void setTitle(String title)
    {
        this.title = title;
        updateModifiedDate();
    }

    public void setDescription(String description)
    {
        this.description = description;
        updateModifiedDate();
    }

    public int getImageData()
    {
        return this.imageData;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Date getModifiedDate()
    {
        return this.modifiedDate;
    }
}
