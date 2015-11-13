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
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("admin")));

        Group[] groupName_match = Group.where("name", "=", converter.toString(data.get("name")));

        if(groupName_match.length > 0)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group name already exists."));

        User user = User.find(converter.toInt(userData.get("id")));
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "The group must have at least 1 user."));

        Group group = new Group();
        group.setName(converter.toString(data.get("name")));
        group.setAdmin(user);
        group.setStatus(converter.toString(data.get("status")));
        if((boolean) group.save().get("status")) {
            HashMap<String, Object> temp = StatusDescription.createProcessStatus(true);
            temp.put("group", group.getGeneralValues());
            return converter.HashMapToJSON(temp);
        }

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred due to an internal server error."));
    }

    @RequestMapping("/group/findByID")
    public String findByID(@RequestParam(value="id", required = true, defaultValue = "0") int id)
    {
        Group group = Group.find(id);
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required user."));

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("group", group.getGeneralValues());
        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/group/findByWhere")
    public String where(@RequestParam(value="columnName", required = true, defaultValue = "") String columnName,
                        @RequestParam(value="operator", required = true, defaultValue = "") String operator,
                        @RequestParam(value="value", required = true, defaultValue = "") String value)
    {
        Group[] groups = Group.where(columnName, operator, value);

        if(groups == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required users."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(groups);

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("groups", tempMap);
        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/group/findByRank")
    public String findByRank(@RequestParam(value="startRank", required = true, defaultValue = "0") int startRank,
                             @RequestParam(value="endRank", required = true, defaultValue = "0") int endRank)
    {
        ArrayList<HashMap<String, Object>> rankList = new ArrayList<>();
        ArrayList<HashMap<String, Object>> groupValuesList = new ArrayList<>();

        startRank = Math.abs(startRank);
        try {
            databaseInquirer.addBatch("SET @rownum := 0");
            rankList = databaseInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM groups ORDER BY score DESC ) as result");
        } catch (SQLException e) {
            e.printStackTrace();
            return Converter.getInstance().HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        } catch (Exception e) {
            e.printStackTrace();
            return Converter.getInstance().HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        endRank = (Math.abs(endRank) > rankList.size()) ? rankList.size() : Math.abs(endRank);

        for(int i = startRank - 1; i < endRank; i++) {
            HashMap<String, Object> item = rankList.get(i);
            HashMap<String, Object> groupsData = Group.find(converter.toInt(item.get("id"))).getGeneralValues();
            groupsData.put("rank", startRank);
            groupValuesList.add(groupsData);
            startRank++;
        }

        HashMap<String, Object>[] groupsDataArray = groupValuesList.toArray(new HashMap[groupValuesList.size()]);

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("groups", groupsDataArray);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/group/getGroupRank")
    public String getGroupRank(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        ArrayList<HashMap<String, Object>> rankList = new ArrayList<>();
        HashMap<String, Object> groupData = converter.JSONToHashMap(JSON);
        try {
            databaseInquirer.addBatch("SET @rownum := 0");
            rankList = databaseInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM groups ORDER BY score DESC ) as result");
        } catch (SQLException e) {
            e.printStackTrace();
            return Converter.getInstance().HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        } catch (Exception e) {
            e.printStackTrace();
            return Converter.getInstance().HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        for(int i = 0; i < rankList.size(); i++) {
            int id = converter.toInt(groupData.get("id"));
            if(id == converter.toInt(rankList.get(i).get("id")))
                return "" + ++i;
        }
        return "" + -1;
    }

    @RequestMapping("/group/getAllGroups")
    public String all()
    {
        Group[] groups = Group.all();

        if(groups == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required users."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(groups);

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("groups", tempMap);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/group/updateGroup")
    public String updateUser(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> groupData = converter.JSONToHashMap(JSON);
        Group group = Group.find(converter.toInt(groupData.get("id")));

        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        group.setStatus(converter.toString(groupData.get("status")));

        return converter.HashMapToJSON(group.save());
    }

    @RequestMapping("/group/deleteMember")
    public String deleteMember(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));
        HashMap<String, Object> memberData = converter.JSONToHashMap(converter.toString(data.get("member")));

        Group group = Group.find(converter.toInt(groupData.get("id")));
        User user = User.find(converter.toInt(memberData.get("id")));

        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        if((boolean) group.addMember(user).get("status"))
            return converter.HashMapToJSON(group.save());

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred due to internal server error."));
    }

    @RequestMapping("/group/addMember")
    public String addMember(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));
        HashMap<String, Object> memberData = converter.JSONToHashMap(converter.toString(data.get("member")));

        Group group = Group.find(converter.toInt(groupData.get("id")));
        User user = User.find(converter.toInt(memberData.get("id")));

        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        if((boolean) group.addMember(user).get("status"))
            return converter.HashMapToJSON(group.save());

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "An error has occurred due to internal server error."));
    }

    @RequestMapping("/group/delete")
    public String delete(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> groupValues = converter.JSONToHashMap(JSON);
        Group group = Group.find(converter.toInt(groupValues.get("id")));

        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        return converter.HashMapToJSON(group.delete());
    }

    @RequestMapping("/group/increaseScore")
    public String increaseScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));

        Group group = Group.find(converter.toInt(groupData.get("id")));
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        HashMap<String, Object> processStatus = group.increaseScore(converter.toInt(data.get("score")), converter.toString(data.get("description")));
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJSON(group.save());

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot increase score."));
    }

    @RequestMapping("/group/decreaseScore")
    public String decreaseScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));

        Group group = Group.find(converter.toInt(groupData.get("id")));
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        HashMap<String, Object> processStatus = group.decreaseScore(converter.toInt(data.get("score")), converter.toString(data.get("description")));
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJSON(group.save());
        
        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot decrease score due to internal server error."));
    }

    @RequestMapping("/group/resetScore")
    public String resetScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));

        Group group = Group.find(converter.toInt(groupData.get("id")));
        if(group == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This group does not exist."));

        HashMap<String, Object> processStatus = group.decreaseScore(-group.getScore(), converter.toString(data.get("description")));
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJSON(group.save());

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot reset score due to internal server error."));
    }

    public void changeDatabaseInquirer(DatabaseInterface databaseInquirer)
    {
        this.databaseInquirer = databaseInquirer;
    }
}

