package movealarm.kmitl.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GroupActivityProgress extends Model {
    private int numberOfAccept = 0;
    private int numberOfCancel = 0;
    private int cancelActivity = 0;
    private Group group;
    private Date date;

    public GroupActivityProgress()
    {
        this.tableName = "groupActivity_progress";

        this.addRequiredField("numberOfAccept");
        this.addRequiredField("cancelActivity");
        this.addRequiredField("groupID");
    }

    public static GroupActivityProgress findByGroup(Group group)
    {
        Converter converter = Converter.getInstance();
        ArrayList<HashMap<String, Object>> data = modelCollection.where("groupActivity_progress", "groupID", "=", converter.toString(group.getID()));

        if(data == null || data.size() == 0)
            return null;

        HashMap<String, Object> progressData = data.get(0);

        GroupActivityProgress model = new GroupActivityProgress();
        model.id = converter.toInt(progressData.get("id"));
        model.group = group;
        model.numberOfAccept = converter.toInt(progressData.get("numberOfAccept"));
        model.numberOfCancel = converter.toInt(progressData.get("numberOfCancel"));
        model.cancelActivity = converter.toInt(progressData.get("cancelActivity"));
        model.date = (Date) progressData.get("date");
        model.createdDate = (Date) progressData.get("createdDate");
        model.modifiedDate = (Date) progressData.get("modifiedDate");

        return model;
    }


    @Override
    public HashMap<String, Object> getValues() //get all values from model
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap<String, Object> temp = new HashMap<>();

        temp.put("numberOfAccept", numberOfAccept);
        temp.put("numberOfCancel", numberOfCancel);
        temp.put("cancelActivity", cancelActivity);
        temp.put("groupID", group.getID());
        if(date != null)
            temp.put("date", sdf.format(date));
        if(modifiedDate != null)
            temp.put("modifiedDate", sdf2.format(modifiedDate));

        return temp;
    }

    @Override
    public HashMap<String, Object> getGeneralValues() //get only common values
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Object> temp = new HashMap<>();

        temp.put("numberOfAccept", numberOfAccept);
        temp.put("numberOfCancel", numberOfCancel);
        temp.put("cancelActivity", cancelActivity);
        temp.put("group", group.getGeneralValues());
        if(date != null)
            temp.put("date", sdf.format(date));

        return temp;
    }

    public void setNumberOfAccept(int numberOfAccept)
    {
        this.numberOfAccept = numberOfAccept;
        updateModifiedDate();
    }

    public void setNumberOfCancel(int numberOfCancel)
    {
        this.numberOfCancel = numberOfCancel;
        updateModifiedDate();
    }

    public void setCancelActivity(int cancelActivity)
    {
        this.cancelActivity = cancelActivity;
        updateModifiedDate();
    }

    public void setGroup(Group group)
    {
        this.group = group;
        updateModifiedDate();
    }

    public void setDate(Date date)
    {
        this.date = date;
        updateModifiedDate();
    }

    public void increaseAcceptTime(int time)
    {
        this.numberOfAccept += time;
    }

    public void increaseCancelTime(int time)
    {
        this.cancelActivity += time;
    }
}
