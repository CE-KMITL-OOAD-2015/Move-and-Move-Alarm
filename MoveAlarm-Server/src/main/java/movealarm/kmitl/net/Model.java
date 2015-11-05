package movealarm.kmitl.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

abstract class Model {
    protected static ModelCollection modelCollection = ModelCollection.getInstance();
    protected String tableName;
    protected ArrayList<String> requiredFields = null;
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

    public int getID()
    {
        return id;
    }

    public String getTableName()
    {
        return tableName;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    protected HashMap<String, Object> checkRequiredFields()
    {
        if(requiredFields != null)
        {
            HashMap<String, Object> objectValues = getValues();
            String requiredFieldName = "";
            for(String fieldName : requiredFields) {
                if(objectValues.get(fieldName) == null) {
                    requiredFieldName += fieldName + " ";
                }
            }
            if(!requiredFieldName.equals(""))
                return StatusDescription.createProcessStatus(false, "Require: " + requiredFieldName + " before save to the database.");
        }

        return null;
    }

    public HashMap<String, Object> save()
    {
        HashMap<String, Object> requiredFields = checkRequiredFields();
        if(requiredFields != null)
            return requiredFields;

        if(createdDate == null) {
            HashMap<String, Object> temp = modelCollection.create(this);
            if(temp == null)
                return StatusDescription.createProcessStatus(false, "Cannot save due to a database error.");
            id = Integer.parseInt("" + temp.get("id"));
            createdDate = (Date) temp.get("created_date");
            return StatusDescription.createProcessStatus(true);
        }

        return StatusDescription.createProcessStatus(modelCollection.save(this));
    }

    public HashMap<String, Object> delete()
    {
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return StatusDescription.createProcessStatus(modelCollection.delete(this));
    }

    public abstract HashMap<String, Object> getValues();

    public abstract HashMap<String, Object> getGeneralValues();

    protected void addRequiredField(String fieldName)
    {
        if(requiredFields == null)
            requiredFields = new ArrayList<>();
        requiredFields.add(fieldName);
    }

    protected void updateModifiedDate()
    {
        modifiedDate = new Date();
    }
}
