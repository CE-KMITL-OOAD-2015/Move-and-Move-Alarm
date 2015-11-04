package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class UserController {

    @RequestMapping("/user/findByID")
    public String findByID(@RequestParam(value="id", required = true, defaultValue = "0") int id)
    {
        Converter converter = Converter.getInstance();
        User user = User.find(id);
        if(user == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Not found the required user."));
        return converter.HashMapToJson(user.getValues());
    }

    @RequestMapping("/user/findByWhere")
    public String where(@RequestParam(value="columnName", required = true, defaultValue = "") String columnName,
                        @RequestParam(value="operator", required = true, defaultValue = "") String operator,
                        @RequestParam(value="value", required = true, defaultValue = "") String value)
    {
        Converter converter = Converter.getInstance();
        User[] users = User.where(columnName, operator, value);

        if(users == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Not found the required users."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(users);
        return converter.HashMapArrayToJSON(tempMap, "users");
    }

    @RequestMapping("/user/findByRank")
    public String findByRank(@RequestParam(value="startRank", required = true, defaultValue = "0") int startRank,
                             @RequestParam(value="endRank", required = true, defaultValue = "0") int endRank)
    {
        SQLInquirer sqlInquirer = SQLInquirer.getInstance();
        ArrayList<HashMap<String, Object>> rankList = new ArrayList<>();
        ArrayList<HashMap<String, Object>> userValuesList = new ArrayList<>();
        Converter converter = Converter.getInstance();
        startRank = Math.abs(startRank);
        endRank = Math.abs(endRank);
        try {
            sqlInquirer.addBatch("SET @rownum := 0");
            rankList = sqlInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM user ORDER BY score DESC ) as result");
        } catch (SQLException e) {
            e.printStackTrace();
            return Converter.getInstance().HashMapToJson(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        for(int i = startRank - 1; i < rankList.size() - 1; i++) {
            HashMap<String, Object> item = rankList.get(i);
            HashMap<String, Object> usersData = User.find(Integer.parseInt("" + item.get("id"))).getValues();
            usersData.put("rank", startRank);
            userValuesList.add(usersData);
            startRank++;
        }

        HashMap<String, Object>[] usersDataArray = userValuesList.toArray(new HashMap[userValuesList.size()]);
        return converter.HashMapArrayToJSON(usersDataArray, "users");
    }

    @RequestMapping("/user/getAllUsers")
    public String all()
    {
        Converter converter = Converter.getInstance();
        User[] users = User.all();

        if(users == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Not found the required users."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(users);
        return converter.HashMapArrayToJSON(tempMap, "users");
    }

    @RequestMapping("/user/createUser")
    public String createUser(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        Converter converter = Converter.getInstance();
        HashMap<String, Object> userValues = converter.JsonToHashMap(JSON);

        User[] username_match = User.where("userName", "=", "" + userValues.get("userName"));

        if(username_match.length > 0)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "This user name is already used by other user."));

        if(("" + userValues.get("password")).length() < 6)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Password should not be less than 6 letters."));
        
        User user = new User();
        user.setUsername((String) userValues.get("userName"));
        user.setEmail((String) userValues.get("email"));
        user.setPassword(Crypto.getInstance().encryption((String) userValues.get("password")));

        try {
            user.setFirstName("" + userValues.get("firstName"));
            user.setLastName("" + userValues.get("lastName"));
            user.setGender((int) userValues.get("gender"));
            user.setAge((int) userValues.get("age"));
            user.setFacebookID("" + userValues.get("facebookID"));
            user.setFacebookFirstName("" + userValues.get("facebookFirstName"));
            user.setFacebookLastName("" + userValues.get("facebookLastName"));
        }
        catch (Exception e) {

        }
        return converter.HashMapToJson(user.save());
    }

    @RequestMapping("/user/updateUser")
    public String updateUser(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        Converter converter = Converter.getInstance();
        HashMap<String, Object> userValues = converter.JsonToHashMap(JSON);

        User user = User.find(((Double) userValues.get("id")).intValue());

        if(user == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "This user does not exist."));

        user.setEmail((String) userValues.get("email"));

        try {
            user.setFirstName("" + userValues.get("firstName"));
            user.setLastName("" + userValues.get("lastName"));
            user.setGender(((Double) userValues.get("gender")).intValue());
            user.setAge(((Double) userValues.get("age")).intValue());
            user.setFacebookID("" + userValues.get("facebookID"));
            user.setFacebookFirstName("" + userValues.get("facebookFirstName"));
            user.setFacebookLastName("" + userValues.get("facebookLastName"));
        }
        catch (Exception e) {

        }
        return converter.HashMapToJson(user.save());
    }

    @RequestMapping("/user/changePassword")
    public String changePassword(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        Converter converter = Converter.getInstance();
        HashMap<String, Object> userValues = converter.JsonToHashMap(JSON);
        Crypto crypto = Crypto.getInstance();

        User user = User.find(((Double) userValues.get("id")).intValue());

        if(user == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "This user does not exist."));

        String currentPassword = crypto.decryption(user.getPassword());
        String oldPassword = "" + userValues.get("oldPassword");
        String newPassword = "" + userValues.get("newPassword");
        String confirmPassword = "" + userValues.get("confirmPassword");

        if(!currentPassword.equals(oldPassword))
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Current password incorrect."));

        if(newPassword.length() < 6)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Password should not be less than 6 letters."));

        if(!newPassword.equals(confirmPassword))
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Password and confirm password mismatch."));

        newPassword = crypto.encryption(newPassword);
        user.setPassword(newPassword);

        return converter.HashMapToJson(user.save());
    }

    @RequestMapping("/user/delete")
    public String delete(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        Converter converter = Converter.getInstance();
        HashMap<String, Object> userValues = converter.JsonToHashMap(JSON);

        User user = User.find(((Double) userValues.get("id")).intValue());

        if(user == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "This user does not exist."));

        return converter.HashMapToJson(user.delete());
    }

    @RequestMapping("/user/countAllUsers")
    public String countAllUsers()
    {
        SQLInquirer sqlInquirer = SQLInquirer.getInstance();
        int amount = 0;
        try {
            HashMap<String, Object> data = sqlInquirer.query("SELECT COUNT(id) AS amount FROM user").get(0);
            amount = Integer.parseInt("" + data.get("amount"));
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
        return "" + amount;
    }

    @RequestMapping("/user/getUserRank")
    public String getUserRank(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        Converter converter = Converter.getInstance();
        SQLInquirer sqlInquirer = SQLInquirer.getInstance();
        ArrayList<HashMap<String, Object>> rankList = new ArrayList<>();
        HashMap<String, Object> userData = converter.JsonToHashMap(JSON);
        try {
            sqlInquirer.addBatch("SET @rownum := 0");
            rankList = sqlInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM user ORDER BY score DESC ) as result");
        } catch (SQLException e) {
            e.printStackTrace();
            return Converter.getInstance().HashMapToJson(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        for(int i = 0; i < rankList.size(); i++) {
            int id = (int) Double.parseDouble("" + userData.get("id"));
            if(id == Integer.parseInt("" + rankList.get(i).get("id")))
                return "" + ++i;
        }
        return "" + -1;
    }

    @RequestMapping("/user/increaseScore")
    public String increaseScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        Converter converter = Converter.getInstance();
        HashMap<String, Object> data = converter.JsonToHashMap(JSON);
        HashMap<String, Object> userData = converter.JsonToHashMap("" + data.get("user"));
        User user = User.find(((Double) userData.get("id")).intValue());
        if(user == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "User does not exist."));
        HashMap<String, Object> processStatus = user.increaseScore((int) Double.parseDouble("" + data.get("score")), "" + data.get("description"));
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJson(user.save());
        return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Cannot increase score."));
    }

    @RequestMapping("/user/decreaseScore")
    public String decreaseScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        Converter converter = Converter.getInstance();
        HashMap<String, Object> data = converter.JsonToHashMap(JSON);
        HashMap<String, Object> userData = converter.JsonToHashMap("" + data.get("user"));
        User user = User.find(((Double) userData.get("id")).intValue());
        if(user == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "User does not exist."));
        HashMap<String, Object> processStatus = user.decreaseScore((int) Double.parseDouble("" + data.get("score")), "" + data.get("description"));
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJson(user.save());
        return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Cannot decrease score."));
    }

    @RequestMapping("/user/resetScore")
    public String resetScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        Converter converter = Converter.getInstance();
        HashMap<String, Object> data = converter.JsonToHashMap(JSON);
        HashMap<String, Object> userData = converter.JsonToHashMap("" + data.get("user"));
        User user = User.find(((Double) userData.get("id")).intValue());
        if(user == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "User does not exist."));
        HashMap<String, Object> processStatus = user.decreaseScore(-user.getScore(), "" + data.get("description"));
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJson(user.save());
        return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Cannot reset score."));
    }
}
