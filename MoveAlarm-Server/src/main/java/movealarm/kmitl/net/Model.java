package movealarm.kmitl.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

abstract class Model {
    protected static ModelCollection modelCollection = ModelCollection.getInstance(SQLInquirer.getInstance());
    protected String tableName;
    protected ArrayList<String> requiredFields = null;
    protected int id;
    protected Date createdDate;
    protected Date modifiedDate;

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

    protected HashMap<String, Object> checkRequiredFields() //check if some required fields are not filled
    {
        if(requiredFields != null) {
            HashMap<String, Object> objectValues = getValues(); //get all fields of model inform of HashMap
            String requiredFieldName = ""; //initial string
            for(String fieldName : requiredFields) {
                if(objectValues.get(fieldName) == null) //if required field is null
                    requiredFieldName += fieldName + " "; //concat remain required fields
            }

            if(!requiredFieldName.equals("")) //if some required fields are not filled
                return StatusDescription.createProcessStatus(false, "Require: " + requiredFieldName + " before save to the database.");
        }

        return null;
    }

    public HashMap<String, Object> save() //save all progresses
    {
        HashMap<String, Object> requiredFields = checkRequiredFields(); //check if some required fields still not filled
        if(requiredFields != null)
            return requiredFields; //return name of need required fields

        if(createdDate == null) { //if this model has never been saved before
            HashMap<String, Object> temp = modelCollection.create(this); //create row on the database first and receive id, created date
            if(temp == null) //if an error has occurred while inserting data to the database
                return StatusDescription.createProcessStatus(false, "Cannot save due to a database error.");

            id = Integer.parseInt("" + temp.get("id")); //set id and created date of this model
            createdDate = (Date) temp.get("created_date");
            return StatusDescription.createProcessStatus(true); //return new model
        }

        return StatusDescription.createProcessStatus(modelCollection.save(this)); //return saving status if all required process are complete
    }

    public HashMap<String, Object> delete() //delete data on the database
    {
        try {
            this.finalize(); //flag this model to let garbage collector collects
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return StatusDescription.createProcessStatus(modelCollection.delete(this)); //return deleting status
    }

    public abstract HashMap<String, Object> getValues();

    public abstract HashMap<String, Object> getGeneralValues();

    protected void addRequiredField(String fieldName) //add required field to the list to flag which field must be filled
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
