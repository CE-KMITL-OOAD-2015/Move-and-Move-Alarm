package movealarm.kmitl.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

abstract class Model {
    protected static ModelCollection modelCollection = ModelCollection.getInstance();
    public static String tableName;
    protected int id;
    protected Date createdDate;
    protected Date modifiedDate;

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
<<<<<<< 9ddf2a751f07a5287b6f455d82c0d458fdc9710d
=======

    protected void updateModifiedDate()
    {
        modifiedDate = new Date();
    }
>>>>>>> [#33] created the user class with basic functionality.
}
