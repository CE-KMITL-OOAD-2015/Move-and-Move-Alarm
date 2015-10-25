package movealarm.kmitl.net;

import java.util.Date;
import java.util.HashMap;

abstract class Model {
    protected static ModelCollection modelCollection = ModelCollection.getInstance();
    private String tableName;
    private int id;
    private Date createdDate;

    public static Model find(int id)
    {
        Model model = null;
        return model;
    }

    public static Model[] where(String colName, String operator, String value) {
        Model[] model = null;
        return model;
    }

    public static Model[] all()
    {
        Model[] model = null;
        return model;
    }

    public abstract boolean save();
    public abstract void delete();
    public abstract HashMap<String, Object> getValues();
}
