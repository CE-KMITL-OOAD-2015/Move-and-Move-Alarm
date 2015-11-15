package movealarm.kmitl.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserActivityProgress extends Model {
    private int numberOfAccept = 0;
    private int numberOfCancel = 0;
    private int cancelActivity = 0;
    private User user;
    private Date date;

    public UserActivityProgress()
    {
        this.tableName = "userActivity_progress";

        this.addRequiredField("numberOfAccept");
        this.addRequiredField("numberOfCancel");
        this.addRequiredField("cancelActivity");
        this.addRequiredField("date");
        this.addRequiredField("userID");
    }

    @Override
    public HashMap<String, Object> getValues() //get all values from model
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Object> temp = new HashMap<>();

        temp.put("numberOfAccept", numberOfAccept);
        temp.put("numberOfCancel", numberOfCancel);
        temp.put("cancelActivity", cancelActivity);
        temp.put("userID", user.getID());
        temp.put("date", sdf.format(date));

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
        temp.put("user", user);
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

    public void setUser(User user)
    {
        this.user = user;
        updateModifiedDate();
    }

    public void setDate(Date date)
    {
        this.date = date;
        updateModifiedDate();
    }
}
