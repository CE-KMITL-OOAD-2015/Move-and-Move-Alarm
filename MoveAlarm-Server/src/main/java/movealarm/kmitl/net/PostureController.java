package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by oat90 on 11/15/2015.
 */
public class PostureController {
    Converter converter = Converter.getInstance();
    SQLInquirer sqlInquirer = SQLInquirer.getInstance();

    @RequestMapping("/posture/findByID")
    public String findByID(@RequestParam(value="id", required = true, defaultValue = "0") int id)
    {
        Posture posture = Posture.find(id);
        if(posture == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false,"Not found the required posture."));
        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("posture",posture.getGeneralValues());

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/posture/findByWhere")
    public String findByWhere(@RequestParam(value="columnName", required = true, defaultValue = "") String columnName,
                        @RequestParam(value="operator", required = true, defaultValue = "") String operator,
                        @RequestParam(value="value", required = true, defaultValue = "") String value)
    {
        Posture[] postures = Posture.where(columnName,operator,value);
        if(postures == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false,"Not found the required posture."));
        HashMap<String, Object>[] tempPosture = converter.ModelArrayToHashMapArray(postures);

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("postures",tempPosture);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/posture/getAllPostures")
    public String getAllPostures()
    {
        Posture[] postures = Posture.all();
        if(postures == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false,"Not found the required posture."));
        HashMap<String, Object>[] tempPosture = converter.ModelArrayToHashMapArray(postures);

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("postures",tempPosture);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/posture/addPosture")
    public String addPosture(@RequestParam(value="JSON", required = true, defaultValue = "")String JSON)
    {
        HashMap<String, Object> postureValues = converter.JSONToHashMap(JSON);
        Posture posture = new Posture();
        posture.setTitle(converter.toString(postureValues.get("title")));
        posture.setImageData(converter.toInt(postureValues.get("imageData")));
        posture.setDescription(converter.toString(postureValues.get("description")));
        return converter.HashMapToJSON(posture.save());
    }

    @RequestMapping("/posture/editPosture")
    public String editPosture(@RequestParam(value="JSON", required = true, defaultValue = "")String JSON)
    {
        HashMap<String, Object> postureValues = converter.JSONToHashMap(JSON);
        Posture posture = Posture.find(converter.toInt(postureValues.get("id")));

        if(posture == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This posture does not exist."));

        posture.setTitle(converter.toString(postureValues.get("title")));
        posture.setImageData(converter.toInt(postureValues.get("imageData")));
        posture.setDescription(converter.toString(postureValues.get("description")));
        return converter.HashMapToJSON(posture.save());
    }

    @RequestMapping("/posture/remove")
    public String removePosture(@RequestParam(value="JSON", required = true, defaultValue = "")String JSON)
    {
        HashMap<String, Object> postureValues = converter.JSONToHashMap(JSON);
        Posture posture = Posture.find(converter.toInt(postureValues.get("id")));

        if(posture == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This posture does not exist."));

        return converter.HashMapToJSON(posture.delete());
    }

    @RequestMapping("/posture/countAllPostures")
    public String countAllPostures()
    {
        int amount = 0;
        try {
            HashMap<String, Object> data = sqlInquirer.query("SELECT COUNT(id) AS amount FROM posture").get(0);
            amount = converter.toInt(data.get("amount"));
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
        return "" + amount;
    }
}
