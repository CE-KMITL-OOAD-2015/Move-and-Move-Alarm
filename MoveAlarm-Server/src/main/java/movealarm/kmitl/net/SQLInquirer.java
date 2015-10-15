package movealarm.kmitl.net;

/**
 * Created by Moobi on 15-Oct-15.
 */

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLInquirer {

    private String sqlCommand;
    private Connection connector;
    private Statement stmt;
    private ResultSet rs;
    private ArrayList<Object> temp = new ArrayList<Object>();

    public SQLInquirer()
    {

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

    public boolean startConnecting()
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

    public Object[] getColumn(int lineNum)
    {
        temp = new ArrayList<Object>();
        try {
            while (rs.next())
                temp.add(rs.getString(lineNum));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp.toArray(new String[temp.size()]);
    }

    public Object[] getColumn(String colName)
    {
        temp = new ArrayList<Object>();
        try {
            while (rs.next())
                temp.add(rs.getObject(colName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp.toArray(new Object[temp.size()]);
    }

    public int getRowNum()
    {
        try {
            return rs.getFetchSize();
        } catch (SQLException e) {
            return 0;
        }
    }
}
