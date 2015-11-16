package movealarm.kmitl.net;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLInquirer implements DatabaseInterface {

    public static SQLInquirer sqlInquirer = null;
    private Connection connector;
    private Statement statement;
    private ResultSet resultSet;
    private boolean connectionStatus = false;

    private SQLInquirer() //make constructor private to use singleton design pattern
    {
        connectionStatus = startConnection();
    }

    public static SQLInquirer getInstance()
    {
        if(sqlInquirer == null)
            sqlInquirer = new SQLInquirer();
        return sqlInquirer;
    }

    @Override
    public void addBatch(String batchCommand) throws SQLException {
        statement.addBatch(batchCommand); //pre-query command
    }

    public ArrayList<HashMap<String, Object>> query(String sqlCommand) throws SQLException {
        ArrayList<HashMap<String, Object>> collection = new ArrayList<>(); //create ArrayList to keep the data

        resultSet = statement.executeQuery(sqlCommand); //query data and keep to resultSet
        ResultSetMetaData metaData = resultSet.getMetaData(); //get meta data of query results such as column name, data type
        while(resultSet.next()) {
            HashMap<String, Object> temp = new HashMap<>(); //create new HashMap to keep each row of table
            for(int i = 1; i <= metaData.getColumnCount(); i++)
                temp.put(metaData.getColumnName(i), resultSet.getObject(i));

            collection.add(temp); //one row one HashMap
        }

        return collection;
    }

    public ArrayList<HashMap<String, Object>> all(String tableName) throws SQLException {
        String sqlCommand = "SELECT * FROM " + tableName; //get all values
        return query(sqlCommand);
    }

    public ArrayList<HashMap<String, Object>> where(String sqlCommand) throws SQLException {
        ArrayList<HashMap<String, Object>> collection = query(sqlCommand); //fill command manually
        return collection;
    }

    public ArrayList<HashMap<String, Object>> where(String tableName, String columnName, String operator, String value, String extraConditions) throws SQLException {
        String sqlCommand = "SELECT * FROM " + tableName + " WHERE " + columnName + " " + operator + " " + value + " " + extraConditions;
        return where(sqlCommand);
    }

    public ArrayList<HashMap<String, Object>> where(String tableName, String columnName, String operator, String value) throws SQLException {
        String sqlCommand = "SELECT * FROM " + tableName + " WHERE " + columnName + " " + operator + " '" + value + "'";
        return where(sqlCommand);
    }

    public void update(String tableName, String valueSet, String columnName, String operator, String value) throws SQLException { //update or edit data
        statement.executeUpdate("UPDATE " + tableName + " SET " + valueSet + " WHERE " + columnName + " " + operator + " " + value);
    }

    public HashMap<String, Object> insert(String tableName, String columnNamesSet, String values) throws SQLException {
        statement.executeUpdate("INSERT INTO " + tableName + " ( " + columnNamesSet + " ) VALUES (" + values + " )");
        resultSet = statement.getGeneratedKeys();
        resultSet.next();

        String id = "" + resultSet.getLong(1); //get id from row
        HashMap<String, Object>temp = new HashMap<>(); //a model that is just created and saved need an id and created date
        temp.put("id", id);
        temp.put("created_date", where(tableName, "id", "=", id).get(0).get("created_date"));
        return temp;
    }

    public void insertMultiple(String tableName, String columnNamesSet, String[] valuesSet) throws SQLException {
        String values = "";
        for(int i = 0; i < valuesSet.length; i++) {
            values += " (" + valuesSet[i] + " )";
            if(i != valuesSet.length - 1)
                values += ", ";
        }
        statement.executeUpdate("INSERT INTO " + tableName + " ( " + columnNamesSet + " ) VALUES " + values);
    }

    public void delete(String tableName, String conditions) throws SQLException {
        statement.executeUpdate("DELETE FROM " + tableName + " WHERE " + conditions);
    }

    public boolean startConnection()
    {
        try{
            Class.forName("org.mariadb.jdbc.Driver"); //cast class
            connector =  DriverManager.getConnection("jdbc:mariadb://203.151.92.198/MoveAlarm" +
                    "?user=MoveAlarmServer&password=pepayoo!&charesultSetet=utf-8"); //database server connection

            if(connector != null){ //if connector can connect to database server
                statement = connector.createStatement(); //create statement to use to query data
                return true;
            } else
                return false;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isConnecting()
    {
        try {
            return !connector.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnection()
    {
        try {
            connector.close();
            statement.close();
            resultSet.close();
            connectionStatus = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
