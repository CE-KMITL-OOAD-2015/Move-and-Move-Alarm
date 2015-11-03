package movealarm.kmitl.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Moobi on 25-Oct-15.
 */
public class TestModel extends Model{
    private String data1 = null;
    private String data2 = null;
    public static TestModel model = null;

    public static TestModel find(int id)
    {
        HashMap<String, Object> temp = modelCollection.find("testTable2", id);
        model = new TestModel();
        model.id = (int) temp.get("id");
        model.createdDate = (Date) temp.get("created_date");
        model.data1 = (String) temp.get("data1");
        model.data2 = (String) temp.get("data1");
        return model;
    }

    public static TestModel[] where(String colName, String operator, String value)
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.where("testTable2", colName, operator, value);
        ArrayList<TestModel> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
            model = new TestModel();
            model.id = (int) item.get("id");
            model.createdDate = (Date) item.get("created_date");
            model.data1 = (String) item.get("data1");
            model.data2 = (String) item.get("data2");
            collection.add(model);
        }

        return  collection.toArray(new TestModel[collection.size()]);
    }

    public static TestModel[] where(String colName, String operator, String value, String extraCondition)
    {
        return where(colName, operator, value + " " + extraCondition);
    }

    public static TestModel[] all()
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.all("testTable2");
        ArrayList<TestModel> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
            model = new TestModel();
            model.id = (int) item.get("id");
            model.createdDate = (Date) item.get("created_date");
            model.data1 = (String) item.get("data1");
            model.data2 = (String) item.get("data2");
            collection.add(model);
        }

        return  collection.toArray(new TestModel[collection.size()]);
    }

    public TestModel()
    {
        this.tableName = "testTable2";
    }

    public HashMap<String, Object> getValues()
    {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("data1", "'" + data1 + "'");
        temp.put("data2", "'" + data2 + "'");
        return temp;
    }

    public void setAttr(String data1, String data2)
    {
        this.data1 = data1;
        this.data2 = data2;
    }

    public String getAttr()
    {
        return data1 + " " + data2;
    }
}
