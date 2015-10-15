package movealarm.kmitl.net;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLInquirer {

    private String sqlCommand;
    private Connection connector;
    private Statement stmt;
    private ResultSet rs;
    private ArrayList<HashMap<String, Object>> collection;
    private boolean connectionStatus = false;

    public SQLInquirer()
    {
        connectionStatus = startConnection();
        collection = new ArrayList<HashMap<String, Object>>();
    }

    public boolean startQuery()
    {
        try {
            stmt = connector.createStatement();
            rs = stmt.executeQuery(sqlCommand);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public void setCommand(String sqlCommand)
    {
        this.sqlCommand = sqlCommand;
    }

    public boolean startConnection()
    {
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            connector =  DriverManager.getConnection("jdbc:mariadb://203.151.92.198/MoveAlarm" +
                    "?user=ice&password=7571179");
            if(connector != null){
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

    public ArrayList<HashMap<String, Object>> getCollection()
    {
        return collection;
    }
}
