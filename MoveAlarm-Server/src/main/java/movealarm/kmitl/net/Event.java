package movealarm.kmitl.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by oat90 on 5/11/2558.
 */
public class Event extends Model{
    ArrayList<Posture> postures = null;
    private String postureList = null;
    private Date time;

    public Event()
    {
        this.tableName = "event";
        this.addRequiredField("time");
        postures = new ArrayList<>();
    }

    public static Event find(int id)
    {
        HashMap<String, Object> event_map = modelCollection.find("event", id);
        if(event_map == null) {
            return null;
        }
        Event event = new Event(); //create event and return its value
        event.id = (int)event_map.get("id");
        event.createdDate = (Date)event_map.get("createdDate");
        event.time = (Date)event_map.get("time");
        event.postureList = (String)event_map.get("postureList");
        String[] list = event.postureList.split(",");
        Converter converter = Converter.getInstance();
        for(int i = 0;i < list.length;i++) {
           event.postures.add(Posture.find(converter.toInt(list[i])));
        }
        event.modifiedDate = (Date)event_map.get("modifiedDate");
        return event;
    }

    public static Event[] where(String colName, String operator, String value)
    {
        ArrayList<HashMap<String, Object>> event_arr = modelCollection.where("event",colName,operator,value);
        ArrayList<Event> collection = new ArrayList<>(); //create array of event and return its value
        for(HashMap<String, Object> item : event_arr) {
            Event model = new Event();
            model.id = (int)item.get("id");
            model.createdDate = (Date)item.get("createdDate");
            model.time = (Date)item.get("time");
            String[] list = model.postureList.split(",");
            Converter converter = Converter.getInstance();
            for(int i = 0;i < list.length;i++) {
                model.postures.add(Posture.find(converter.toInt(list[i])));
            }
            model.modifiedDate = (Date)item.get("modifiedDate");
            collection.add(model);
        }
        return collection.toArray(new Event[collection.size()]);
    }

    public static Event[] where(String colName, String operator, String value, String extraCondition)
    {
        return where(colName, operator, value + " " + extraCondition); //where method with extra condition
    }

    public static Event[] all()
    {
        ArrayList<HashMap<String, Object>> event_arr = modelCollection.all("event"); //get all postures
        ArrayList<Event> collection = new ArrayList<>();
        for(HashMap<String, Object> item : event_arr) {
            Event model = new Event();
            model.id = (int)item.get("id");
            model.createdDate = (Date)item.get("createdDate");
            model.time = (Date)item.get("time");
            String[] list = model.postureList.split(",");
            Converter converter = Converter.getInstance();
            for(int i = 0;i < list.length;i++) {
                model.postures.add(Posture.find(converter.toInt(list[i])));
            }
            model.modifiedDate = (Date)item.get("modifiedDate");
            collection.add(model);
        }
        return collection.toArray(new Event[collection.size()]);
    }

    @Override
    public HashMap<String, Object> getValues() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //return all field of model then, return in hashmap type
        HashMap<String, Object> event_map = new HashMap<>();
        event_map.put("time", sdf.format(this.time));
        event_map.put("postureList", this.postureList);
        if(modifiedDate == null)
            event_map.put("modifiedDate", null);
        else
            event_map.put("modifiedDate", sdf.format(this.modifiedDate));
        return event_map;
    }

    @Override
    public HashMap<String, Object> getGeneralValues()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss"); // like getValues method but return only common value
        Converter converter = Converter.getInstance();
        ArrayList<HashMap<String, Object>> temp = new ArrayList<>();

        for(Posture item : postures) {
            temp.add(item.getGeneralValues());
        }

        HashMap<String, Object>[] arrayOfPosture = temp.toArray(new HashMap[temp.size()]);
        HashMap<String, Object> event_map = new HashMap<>();
        event_map.put("time", sdf.format(this.time));
        event_map.put("postures", arrayOfPosture);
        return event_map;
    }

    public void setTime(Date time)
    {
        this.time = time;
        updateModifiedDate();
    }

    public void setPostureList(String postureList)
    {
        this.postureList = postureList;
        updateModifiedDate();
    }

    public void setPostures(ArrayList<Posture> postures)
    {
        this.postures = postures;
        updateModifiedDate();
    }

    public Date getTime()
    {
        return this.time;
    }

    public String getPostureList()
    {
        return this.postureList;
    }

    public ArrayList<Posture> getPostures()
    {
        return this.postures;
    }

}
