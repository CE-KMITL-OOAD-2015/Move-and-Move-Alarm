package movealarm.kmitl.net;

import javax.swing.text.TabableView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moobi on 16-Oct-15.
 */
public class ModelCollection {
    public static ModelCollection modelCollection = null;
    private SQLInquirer sqlInquirer = SQLInquirer.getInstance();

    private ModelCollection() { }

    public static ModelCollection getInstance()
    {
        if(modelCollection == null)
            modelCollection = new ModelCollection();
        return modelCollection;
    }

    public HashMap<String, Object> find(String tableName, int id)
    {
        try {
            return sqlInquirer.where(tableName,"id", "=", "" +id).get(0);
        } catch (SQLException e) {
            System.out.println("An error has occurred in ModelCollection.find()");
            System.out.println(e);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("An error has occurred in ModelCollection.find()");
            System.out.println(e);
        }
        return null;
    }

    public ArrayList<HashMap<String, Object>> all(String tableName)
    {
        try {
            return sqlInquirer.all(tableName);
        } catch (SQLException e) {
            System.out.println("An error has occurred in ModelCollection.all()");
            System.out.println(e);
        }
        return null;
    }

    public ArrayList<HashMap<String, Object>> where(String tableName, String colName, String operator, String value)
    {
        try {
            return sqlInquirer.where(tableName, colName, operator, value);
        } catch (SQLException e) {
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
            values += value + ",";
        }
        colNames = colNames.substring(0, colNames.length() - 1);
        values = values.substring(0, values.length() - 1);
        try {
            return sqlInquirer.insert(model.getTableName(), colNames, values);
        } catch (SQLException e) {
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
            if(value.getClass().equals(String.class))
                valueSet += key + "='" + value + "', ";
            else
                valueSet += key + "=" + value + ", ";
        }
        valueSet = valueSet.substring(0, valueSet.length() - 2);

        try {
            sqlInquirer.update(model.getTableName(), valueSet, "id", "=", "" + model.getID());
            return true;
        } catch (SQLException e) {
            System.out.println("An error has occurred in while saving a model name '" + model.getClass().getSimpleName() + "'.");
            System.out.println(e);
        }
        return false;
    }

    public boolean delete(Model model)
    {
        try {
            sqlInquirer.delete(model.getTableName(), "id = '" + model.getID() + "'");
            model = null;
        } catch (SQLException e) {
            System.out.println("An error has occurred in while deleting a model name '" + model.getClass().getSimpleName() + "'.");
            System.out.println(e);
            return false;
        }
        return true;
    }
}
