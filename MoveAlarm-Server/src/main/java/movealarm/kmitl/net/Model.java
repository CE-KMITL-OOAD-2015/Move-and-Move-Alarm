package movealarm.kmitl.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

abstract class Model {
    protected static ModelCollection modelCollection = ModelCollection.getInstance();
    protected String tableName;
    protected int id;
    protected Date createdDate;

    public static Model find(int id)
    {
        Model model = null;
        return model;
    }

    public static Model[] where(String colName, String operator, String value) {
        Model[] model = null;
        return model;
    }

    public static Model[] where(String colName, String operator, String value, String extraConditions) {
        Model[] model = null;
        return model;
    }

    public static Model[] all()
    {
        Model[] model = null;
        return model;
    }

    public String getID()
    {
        return "" + id;
    }

    public String getTableName()
    {
        return tableName;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public boolean save()
    {
        if(createdDate == null) {
            HashMap<String, Object> temp = modelCollection.create(this);
            if(temp == null)
                return false;
            id = Integer.parseInt("" + temp.get("id"));
            createdDate = (Date) temp.get("created_date");
            return true;
        }

        return modelCollection.save(this);
    }

    public void delete()
    {
        modelCollection.delete(this);
    }

    public abstract HashMap<String, Object> getValues();
}
