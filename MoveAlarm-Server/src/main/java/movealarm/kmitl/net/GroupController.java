package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class GroupController {

    Converter converter = Converter.getInstance();
    DatabaseInterface databaseInquirer = SQLInquirer.getInstance();

    @RequestMapping("/group/createGroup")
    public String createGroup(@RequestParam(value = "JSON", required = true, defaultValue = "0")String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON); //convert JSON string to HashMap format
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(groupData.get("admin"))); //convert nested JSON

        Group[] groupName_match = Group.where("name", "=", converter.toString(groupData.get("name"))); //check match group name
        if(groupName_match.length > 0) //if this name is already used
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group name already exists.")); //send error description to client

        User user = User.find(converter.toInt(userData.get("id")));
        if(user == null) //check if this user is exist
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        Group group = new Group();
        group.setName(converter.toString(groupData.get("name")));
        group.setAdmin(user);
        group.setStatus(converter.toString(groupData.get("status")));
        if((boolean) group.save().get("status")) { //if saving process is success

            HashMap<String, Object> temp = StatusDescription.createProcessStatus(true); //create response object
            temp.put("group", group.getGeneralValues()); //put the new group include id and created date
            return converter.HashMapToJSON(temp); //return to client
        }

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred due to an internal server error."));
    }

    @RequestMapping("/group/findByID")
    public String findByID(@RequestParam(value="id", required = true, defaultValue = "0") int id)
    {
        Group group = Group.find(id);
        if(group == null) //if group does not exist
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required group.")); //return error description

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true); //create response object
        JSON.put("group", group.getGeneralValues()); //attach group to JSON
        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/group/findByWhere")
    public String where(@RequestParam(value="columnName", required = true, defaultValue = "") String columnName,
                        @RequestParam(value="operator", required = true, defaultValue = "") String operator,
                        @RequestParam(value="value", required = true, defaultValue = "") String value)
    {
        Group[] groups = Group.where(columnName, operator, value);
        if(groups == null) //if cannot find any groups
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required groups.")); //return result

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(groups); //convert array of group to array of HashMap
        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("groups", tempMap);
        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/group/findByRank")
    public String findByRank(@RequestParam(value="startRank", required = true, defaultValue = "0") int startRank,
                             @RequestParam(value="endRank", required = true, defaultValue = "0") int endRank)
    {
        ArrayList<HashMap<String, Object>> rankList; //create list of rank group
        ArrayList<HashMap<String, Object>> groupDataList = new ArrayList<>(); //create list to keep each group's data

        startRank = Math.abs(startRank); //convert to positive integer
        try {
            databaseInquirer.addBatch("SET @rownum := 0"); //create temporary variable on database to create rank
            rankList = databaseInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM groups ORDER BY score DESC ) as result"); //create temporary table to keep group data with order by score and query group id from the temporary table
        } catch (Exception e) {
            e.printStackTrace();
            return Converter.getInstance().HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        endRank = (Math.abs(endRank) > rankList.size()) ? rankList.size() : Math.abs(endRank); //if received endRank is more than query data, assign max rank to endRank

        for(int i = startRank - 1; i < endRank; i++) { //put group data to the list at start rank to end rank
            HashMap<String, Object> item = rankList.get(i);
            HashMap<String, Object> groupsData = Group.find(converter.toInt(item.get("id"))).getGeneralValues();
            groupsData.put("rank", startRank);
            groupDataList.add(groupsData);
            startRank++;
        }

        HashMap<String, Object>[] groupsDataArray = groupDataList.toArray(new HashMap[groupDataList.size()]); //convert to normal array

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("groups", groupsDataArray);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/group/getGroupRank") //find ranking of received group
    public String getGroupRank(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        ArrayList<HashMap<String, Object>> rankList; //create
        HashMap<String, Object> groupData = converter.JSONToHashMap(JSON);
        try {
            databaseInquirer.addBatch("SET @rownum := 0");
            rankList = databaseInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM groups ORDER BY score DESC ) as result"); //query rank
        } catch (Exception e) {
            e.printStackTrace();
            return Converter.getInstance().HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        for(int i = 0; i < rankList.size(); i++) { //find match group id
            int id = converter.toInt(groupData.get("id"));
            if(id == converter.toInt(rankList.get(i).get("id")))
                return "" + ++i;
        }

        return "" + -1; //return rank -1 if found nothing
    }

    @RequestMapping("/group/getAllGroups")
    public String all()
    {
        Group[] groups = Group.all();

        if(groups == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found any group."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(groups); //convert to normal array

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("groups", tempMap);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/group/updateGroup")
    public String updateGroup(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> groupData = converter.JSONToHashMap(JSON); //convert receive data to HashMap format
        Group group = Group.find(converter.toInt(groupData.get("id")));
        if(group == null) //if group does not exist
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        group.setStatus(converter.toString(groupData.get("status")));

        return converter.HashMapToJSON(group.save()); //return process status
    }

    @RequestMapping("/group/deleteMember")
    public String deleteMember(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON); //convert JSON string to HashMap format
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group"))); //convert nested JSON
        HashMap<String, Object> memberData = converter.JSONToHashMap(converter.toString(data.get("member"))); //convert nested JSON

        Group group = Group.find(converter.toInt(groupData.get("id"))); //find group
        User user = User.find(converter.toInt(memberData.get("id"))); //find a user that will be removed from the group

        if(group == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        if(user == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        if((boolean) group.addMember(user).get("status")) //check if adding member not cause any issue
            return converter.HashMapToJSON(group.save()); //return save status

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred due to internal server error."));
    }

    @RequestMapping("/group/addMember")
    public String addMember(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON); //convert to HashMap format
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group"))); //convert nested JSON to HashMap
        HashMap<String, Object> memberData = converter.JSONToHashMap(converter.toString(data.get("member")));

        Group group = Group.find(converter.toInt(groupData.get("id"))); //find group by id
        User user = User.find(converter.toInt(memberData.get("id"))); //find group by id

        if(group == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        if(group.getAmountMember() > 9)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "The group has reached the maximum members limit now."));

        if(user == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        if((boolean) group.addMember(user).get("status")) { //check if adding member not cause any issue {
            HashMap<String, Object> response = group.save();
            response.put("group", group.getGeneralValues());
            return converter.HashMapToJSON(response); //return save status
        }

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred due to internal server error."));
    }

    @RequestMapping("/group/delete")
    public String delete(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> groupData = converter.JSONToHashMap(JSON); //convert to HashMap
        Group group = Group.find(converter.toInt(groupData.get("id"))); //find group in the database

        if(group == null) //found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        return converter.HashMapToJSON(group.delete());
    }

    @RequestMapping("/group/increaseScore")
    public String increaseScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON); //convert to HashMap
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group"))); //convert nested JSON to HashMap

        Group group = Group.find(converter.toInt(groupData.get("id"))); //find group in the database
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        HashMap<String, Object> processStatus = group.increaseScore(converter.toInt(data.get("score")), converter.toString(data.get("description"))); //increase score, put description of increasing and get process status
        if((boolean) processStatus.get("status")) //if success
            return converter.HashMapToJSON(group.save());

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot increase score."));
    }

    @RequestMapping("/group/decreaseScore")
    public String decreaseScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON); //convert to HashMap
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group"))); //convert nested JSON to HashMap

        Group group = Group.find(converter.toInt(groupData.get("id"))); //find group in the database
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        HashMap<String, Object> processStatus = group.decreaseScore(converter.toInt(data.get("score")), converter.toString(data.get("description"))); //increase score, put description of increasing and get process status
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJSON(group.save());
        
        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot decrease score due to internal server error."));
    }

    @RequestMapping("/group/resetScore")
    public String resetScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON); //convert to HAshMap
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group"))); //convert to HashMap

        Group group = Group.find(converter.toInt(groupData.get("id"))); //find group in the database
        if(group == null) //found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        HashMap<String, Object> processStatus = group.decreaseScore(-group.getScore(), converter.toString(data.get("description"))); //reset score to 0 and get process status
        if((boolean) processStatus.get("status")) //if process is success
            return converter.HashMapToJSON(group.save()); //return saving status

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot reset score due to internal server error."));
    }

    public void changeDatabaseInquirer(DatabaseInterface databaseInquirer) //method for changing database interface
    {
        this.databaseInquirer = databaseInquirer;
    }
}

