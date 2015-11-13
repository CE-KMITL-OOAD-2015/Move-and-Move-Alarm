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

    public HashMap<String, Object> find(String tableName, int id)
    {
        try {
            return databaseInquirer.where(tableName,"id", "=", "" +id).get(0);
        } catch (Exception e) {
            System.out.println("An error has occurred in ModelCollection.find()");
            System.out.println(e);
        }
        return null;
    }

    public ArrayList<HashMap<String, Object>> all(String tableName)
    {
        try {
            return databaseInquirer.all(tableName);
        } catch (Exception e) {
            System.out.println("An error has occurred in ModelCollection.all()");
            System.out.println(e);
        }
        return null;
    }

    public ArrayList<HashMap<String, Object>> where(String tableName, String colName, String operator, String value)
    {
        try {
            return databaseInquirer.where(tableName, colName, operator, value);
        } catch (Exception e) {
            System.out.println("An error has occurred in ModelCollection.where()");
            System.out.println(e);
        }
        return null;
    }

    public HashMap<String, Object> create(Model model)
    {
        String colNames = "", values = "";
        HashMap<String, Object> list = model.getValues();
        for(Map.Entry<String, Object> data : list.entrySet()) {
            String key = data.getKey();
            Object value = data.getValue();
            colNames += key + ",";
            if(value == null)
                values += "NULL, ";
            else if(value.getClass().equals(String.class))
                values += "'" + value + "', ";
            else
                values += "" + value + ", ";
        }
        colNames = colNames.substring(0, colNames.length() - 1);
        values = values.substring(0, values.length() - 2);
        try {
            return databaseInquirer.insert(model.getTableName(), colNames, values);
        } catch (Exception e) {
            System.out.println("An error has occurred in while creating a model name '" + model.getClass().getSimpleName() + "'.");
            System.out.println(e);
        }
        return null;
    }

    public boolean save(Model model)
    {
        String valueSet = "";
        HashMap<String, Object> list = model.getValues();
        for(Map.Entry<String, Object> data : list.entrySet()) {
            String key = data.getKey();
            Object value = data.getValue();
            if(value == null)
                valueSet += key + "=NULL, ";
            else if(value.getClass().equals(String.class))
                valueSet += key + "='" + value + "', ";
            else
                valueSet += key + "=" + value + ", ";
        }
        valueSet = valueSet.substring(0, valueSet.length() - 2);

        try {
            databaseInquirer.update(model.getTableName(), valueSet, "id", "=", "" + model.getID());
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
            databaseInquirer.delete(model.getTableName(), "id = '" + model.getID() + "'");
            model = null;
        } catch (Exception e) {
            System.out.println("An error has occurred in while deleting a model name '" + model.getClass().getSimpleName() + "'.");
            System.out.println(e);
            return false;
        }
        return true;
    }
}
