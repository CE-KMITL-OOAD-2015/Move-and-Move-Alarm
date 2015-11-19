package movealarm.kmitl.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelCollection {
    public static ModelCollection modelCollection = null;
    private DatabaseInterface databaseInquirer;

    private ModelCollection(DatabaseInterface databaseInquirer)
    {
        this.databaseInquirer = databaseInquirer;
    }

    public static ModelCollection getInstance(DatabaseInterface databaseInquirer)
    {
        if(modelCollection == null)
            modelCollection = new ModelCollection(databaseInquirer);
        return modelCollection;
    }

    public HashMap<String, Object> find(String tableName, int id) //find method will query data by an id, each id is unique so find method will return only 1 object
    {
        try {
            return databaseInquirer.where(tableName,"id", "=", "" +id).get(0);
        } catch (Exception e) {
            System.out.println("An error has occurred in ModelCollection.find()");
            System.out.println(e);
        }
        return null;
    }

    public ArrayList<HashMap<String, Object>> all(String tableName) //return all model
    {
        try {
            return databaseInquirer.all(tableName);
        } catch (Exception e) {
            System.out.println("An error has occurred in ModelCollection.all()");
            System.out.println(e);
        }
        return null;
    }

    public ArrayList<HashMap<String, Object>> where(String tableName, String columnName, String operator, String value) //pass values method
    {
        try {
            return databaseInquirer.where(tableName, columnName, operator, value);
        } catch (Exception e) {
            System.out.println("An error has occurred in ModelCollection.where()");
            System.out.println(e);
        }
        return null;
    }

    public HashMap<String, Object> create(Model model)
    {
        String columnsName = "", values = "";
        HashMap<String, Object> list = model.getValues(); //get all fields from model
        for(Map.Entry<String, Object> data : list.entrySet()) { //get set of key and value from HashMap
            String key = data.getKey(); //get column name from key
            Object value = data.getValue(); //get value of column in each row
            columnsName += key + ","; //create set of column name
            if(value == null) //if value of field is null
                values += "NULL, ";
            else if(value.getClass().equals(String.class)) //if value is string type
                values += "'" + value + "', ";
            else //if value is numeric
                values += "" + value + ", ";
        }

        columnsName = columnsName.substring(0, columnsName.length() - 1); //remove unused quote or period
        values = values.substring(0, values.length() - 2); //remove unused quote or period

        try {
            return databaseInquirer.insert(model.getTableName(), columnsName, values); //insert data to the database
        } catch (Exception e) {
            System.out.println("An error has occurred in while creating a model name '" + model.getClass().getSimpleName() + "'.");
            System.out.println(e);
        }
        return null;
    }

    public boolean save(Model model)
    {
        String valueSet = "";
        HashMap<String, Object> list = model.getValues(); //get all fields from model
        for(Map.Entry<String, Object> data : list.entrySet()) {
            String key = data.getKey(); //get column name
            Object value = data.getValue(); //get value of column
            if(value == null) //if value of field is null
                valueSet += key + "=NULL, ";
            else if(value.getClass().equals(String.class)) //if value is string type
                valueSet += key + "='" + value + "', ";
            else //if value is numeric
                valueSet += key + "=" + value + ", ";
        }

        valueSet = valueSet.substring(0, valueSet.length() - 2); //remove unused quote or period

        try {
            databaseInquirer.update(model.getTableName(), valueSet, "id", "=", "" + model.getID()); //update data to the database
            return true;
        } catch (Exception e) {
            System.out.println("An error has occurred in while saving a model name '" + model.getClass().getSimpleName() + "'.");
            System.out.println(e);
        }
        return false;
    }

    public boolean delete(Model model)
    {
        try {
            databaseInquirer.delete(model.getTableName(), "id = '" + model.getID() + "'"); //delete data on the database
            model = null;
        } catch (Exception e) {
            System.out.println("An error has occurred in while deleting a model name '" + model.getClass().getSimpleName() + "'.");
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean manualEditData(String tableName, String valueSet, String columnName, String operator, String value)
    {
        try {
            databaseInquirer.update(tableName, valueSet, columnName, operator, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error has occurred while updating data.");
            return false;
        }
    }

    public boolean manualInsertDataMultiple(String tableName, String columnNameSet, String[] valuesSet)
    {
        try {
            databaseInquirer.insertMultiple(tableName, columnNameSet, valuesSet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error has occurred while inserting data.");
            return false;
        }
    }

    public boolean manualDeleteData(String tableName, String conditions)
    {
        try {
            databaseInquirer.delete(tableName, conditions);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
