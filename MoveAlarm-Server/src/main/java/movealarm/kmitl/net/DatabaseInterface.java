package movealarm.kmitl.net;

import java.util.ArrayList;
import java.util.HashMap;

public interface DatabaseInterface
{
    public void addBatch(String batchCommand) throws Exception;
    public ArrayList<HashMap<String, Object>> query(String command) throws Exception;
    public void update(String tableName, String valueSet, String columnName, String operator, String value) throws Exception;
    public HashMap<String, Object> insert(String tableName, String columnNamesSet, String values) throws Exception;
    public void delete(String tableName, String conditions) throws Exception;
    public boolean startConnection();
    public boolean isConnecting();
    public void closeConnection();
}
