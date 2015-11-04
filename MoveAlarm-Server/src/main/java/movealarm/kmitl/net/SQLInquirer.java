package movealarm.kmitl.net;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLInquirer {

    public static SQLInquirer sqlInquirer = null;
    private Connection connector;
    private Statement stmt;
    private ResultSet rs;
    private boolean connectionStatus = false;
    private String orderBy = "";
    private String orderType = "";

    private SQLInquirer()
    {
        connectionStatus = startConnection();
    }

    public static SQLInquirer getInstance()
    {
        if(sqlInquirer == null)
            sqlInquirer = new SQLInquirer();
        return sqlInquirer;
    }

    public void orderByDESC(String colNames)
    {
        orderBy = "ORDER BY " + colNames;
        orderType = "DESC";
    }

    public void orderByASC(String colNames)
    {
        orderBy = "ORDER BY " + colNames;
        orderType = "ASC";
    }

    public void resetOrderBy()
    {
        orderBy = "ORDER BY ";
        orderBy = "";
    }

    public void addBatch(String batchCommand) throws SQLException {
        stmt.addBatch(batchCommand);
    }

    public boolean isOrderBy()
    {
        return (orderBy.equals("ORDER BY ")) ? false : true;
    }

    public ArrayList<HashMap<String, Object>> query(String sqlCommand) throws SQLException {
        ArrayList<HashMap<String, Object>> collection = new ArrayList<>();
        rs = stmt.executeQuery(sqlCommand);
        ResultSetMetaData metaData = rs.getMetaData();
        while(rs.next()) {
            HashMap<String, Object> temp = new HashMap<>();
            for(int i = 1; i <= metaData.getColumnCount(); i++)
                temp.put(metaData.getColumnName(i), rs.getObject(i));
            collection.add(temp);
        }
        return collection;
    }

    public ArrayList<HashMap<String, Object>> all(String tableName) throws SQLException {
        String sqlCommand = "SELECT * FROM " + tableName;
        return query(sqlCommand);
    }

    public ArrayList<HashMap<String, Object>> where(String sqlCommand) throws SQLException {
        if(isOrderBy())
            sqlCommand += " " + orderBy + " " + orderType;
        ArrayList<HashMap<String, Object>> collection = query(sqlCommand);
        resetOrderBy();
        return collection;
    }

    public ArrayList<HashMap<String, Object>> where(String tableName, String colName, String operator, String value, String extraConditions) throws SQLException {
        String sqlCommand = "SELECT * FROM " + tableName + " WHERE " + colName + " " + operator + " " + value + " " + extraConditions;
        return where(sqlCommand);
    }

    public ArrayList<HashMap<String, Object>> where(String tableName, String colName, String operator, String value) throws SQLException {
        String sqlCommand = "SELECT * FROM " + tableName + " WHERE " + colName + " " + operator + " '" + value + "'";
        return where(sqlCommand);
    }

    public void update(String tableName, String valueSet, String colName, String operator, String value) throws SQLException {
        stmt.executeUpdate("UPDATE " + tableName + " SET " + valueSet + " WHERE " + colName + " " + operator + " " + value);
    }

    public HashMap<String, Object> insert(String tableName, String colNamesSet, String values) throws SQLException {
        stmt.executeUpdate("INSERT INTO " + tableName + " ( " + colNamesSet + " ) VALUES (" + values + " )");
        rs = stmt.getGeneratedKeys();
        rs.next();

        String id = "" + rs.getLong(1);
        HashMap<String, Object>temp = new HashMap<>();
        temp.put("id", id);
        temp.put("created_date", where(tableName, "id", "=", id).get(0).get("created_date"));
        return temp;
    }

    public void insertMultiple(String tableName, String colNamesSet, String[] valuesSet) throws SQLException {
        String values = "";
        for(int i = 0; i < valuesSet.length; i++) {
            values += " (" + valuesSet[i] + " )";
            if(i != valuesSet.length - 1)
                values += ", ";
        }
        stmt.executeUpdate("INSERT INTO " + tableName + " ( " + colNamesSet + " ) VALUES " + values);
    }

    public void delete(String tableName, String conditions) throws SQLException {
        stmt.executeUpdate("DELETE FROM " + tableName + " WHERE " + conditions);
    }

    public boolean startConnection()
    {
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            connector =  DriverManager.getConnection("jdbc:mariadb://203.151.92.198/MoveAlarm" +
                    "?user=oat&password=123454322&charset=utf-8");
            if(connector != null){
                stmt = connector.createStatement();
                return true;
            } else {
                return false;
            }
        } catch (ClassNotFoundException e) {
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isConnecting()
    {
        return connectionStatus;
    }

    public void closeConnection()
    {
        try {
            connector.close();
            stmt.close();
            rs.close();
            connectionStatus = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
