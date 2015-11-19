package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
public class GroupProgressController {

    private Converter converter = Converter.getInstance();
    private DatabaseInterface databaseInquirer = SQLInquirer.getInstance();

    @RequestMapping("/groupProgress/getAllGroupLogs")
    public String getAllLog(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);

        Group group = Group.find(converter.toInt(data.get("id")));
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Group does not exist."));

        ArrayList<HashMap<String, Object>> queryData;
        try {
            queryData = databaseInquirer.where("groupActivity_progress", "groupID", "=", converter.toString(group.getID()));
        } catch (Exception e) {
            e.printStackTrace();
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred while qurying data from the database."));
        }

        if(queryData == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "No result found."));

        HashMap<String, Object>[] logs = queryData.toArray(new HashMap[queryData.size()]);
        HashMap<String, Object> response = StatusDescription.createProcessStatus(true);
        response.put("logs", logs);

        return converter.HashMapToJSON(response);
    }

    @RequestMapping("/groupProgress/getByDate")
    public String getByDate(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));

        String startDate = null;
        String endDate = null;
        try {
            startDate = sdf.format(sdf.parse(converter.toString(data.get("startDate"))));
            endDate = sdf.format(sdf.parse(converter.toString(data.get("endDate"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Group group = Group.find(converter.toInt(groupData.get("id")));
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Group does not exist."));

        ArrayList<HashMap<String, Object>> queryData;
        try {
            queryData = databaseInquirer.where("groupActivity_progress", "date", "BETWEEN", startDate + "' AND '" + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred while qurying data from the database."));
        }

        if(queryData == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "No result found."));

        HashMap<String, Object>[] logs = queryData.toArray(new HashMap[queryData.size()]);
        HashMap<String, Object> response = StatusDescription.createProcessStatus(true);
        response.put("logs", logs);

        return converter.HashMapToJSON(response);
    }

    @RequestMapping("/groupProgress/getByGroup")
    public String getByGroup(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));

        Group group = Group.find(converter.toInt(groupData.get("id")));
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "group does not exist."));

        GroupActivityProgress progress = GroupActivityProgress.findByGroup(group);
        if(progress == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "No group activity progress."));

        HashMap<String, Object> response = StatusDescription.createProcessStatus(true);
        response.put("progress", progress.getGeneralValues());

        return converter.HashMapToJSON(response);
    }

    @RequestMapping("/groupProgress/save")
    public String updateProgress(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));
        HashMap<String, Object> progressData = converter.JSONToHashMap(converter.toString(data.get("activityProgress")));

        Group group = Group.find(converter.toInt(groupData.get("id")));
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "group does not exist."));

        GroupActivityProgress progress = GroupActivityProgress.findByGroup(group);
        if(progress == null) {
            progress = new GroupActivityProgress();
            progress.setGroup(group);
        }

        progress.increaseAcceptTime(converter.toInt(progressData.get("numberOfAccept")));
        progress.increaseCancelTime(converter.toInt(progressData.get("cancelActivity")));

        try {
            progress.setDate((Date) progressData.get("date"));
            progress.setNumberOfCancel(converter.toInt(progressData.get("numberOfCancel")));
        } catch (Exception e) {

        }

        return converter.HashMapToJSON(progress.save());
    }

}
