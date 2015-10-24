package movealarm.kmitl.net;

import java.sql.SQLException;
import java.util.HashMap;

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
            e.printStackTrace();
        }
        return null;
    }
}
