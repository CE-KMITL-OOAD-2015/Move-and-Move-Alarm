package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class UserController {

    Converter converter = Converter.getInstance();
    DatabaseInterface databaseInquirer = SQLInquirer.getInstance();
    Crypto crypto = Crypto.getInstance();
    
    @RequestMapping("/user/findByID")
    public String findByID(@RequestParam(value="id", required = true, defaultValue = "0") int id)
    {
        User user = User.find(id);
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required user."));

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("user", user.getGeneralValues());

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/user/findByWhere")
    public String where(@RequestParam(value="columnName", required = true, defaultValue = "") String columnName,
                        @RequestParam(value="operator", required = true, defaultValue = "") String operator,
                        @RequestParam(value="value", required = true, defaultValue = "") String value)
    {
        User[] users = User.where(columnName, operator, value);

        if(users == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required users."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(users);

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("users", tempMap);
        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/user/findByRank")
    public String findByRank(@RequestParam(value="startRank", required = true, defaultValue = "0") int startRank,
                             @RequestParam(value="endRank", required = true, defaultValue = "0") int endRank)
    {
        ArrayList<HashMap<String, Object>> rankList = new ArrayList<>();
        ArrayList<HashMap<String, Object>> userValuesList = new ArrayList<>();
        
        startRank = Math.abs(startRank);
        try {
            databaseInquirer.addBatch("SET @rownum := 0");
            rankList = databaseInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM user ORDER BY score DESC ) as result");
        } catch (Exception e) {
            e.printStackTrace();
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        endRank = (Math.abs(endRank) > rankList.size()) ? rankList.size() : Math.abs(endRank);

        for(int i = startRank - 1; i < endRank; i++) {
            HashMap<String, Object> item = rankList.get(i);
            HashMap<String, Object> usersData = User.find(converter.toInt(item.get("id"))).getGeneralValues();
            usersData.put("rank", startRank);
            userValuesList.add(usersData);
            startRank++;
        }

        HashMap<String, Object>[] usersDataArray = userValuesList.toArray(new HashMap[userValuesList.size()]);

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("users", usersDataArray);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/user/getAllUsers")
    public String all()
    {
        User[] users = User.all();

        if(users == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required users."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(users);

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("users", tempMap);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/user/createUser")
    public String createUser(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> userValues = converter.JSONToHashMap(JSON);
        User[] username_match = User.where("userName", "=", converter.toString(userValues.get("userName")));

        if(username_match.length > 0)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user name is already used by other user."));

        if((converter.toString(userValues.get("password"))).length() < 6)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Password should not be less than 6 letters."));
        
        User user = new User();
        user.setUsername(converter.toString(userValues.get("userName")));
        user.setEmail(converter.toString(userValues.get("email")));
        user.setPassword(Crypto.getInstance().encryption(converter.toString(userValues.get("password"))));

        try {
            user.setFirstName(converter.toString(userValues.get("firstName")));
            user.setLastName(converter.toString(userValues.get("lastName")));
            user.setGender(converter.toInt(userValues.get("gender")));
            user.setAge(converter.toInt(userValues.get("age")));
            user.setFacebookID(converter.toString(userValues.get("facebookID")));
            user.setFacebookFirstName(converter.toString(userValues.get("facebookFirstName")));
            user.setFacebookLastName(converter.toString(userValues.get("facebookLastName")));
        }
        catch (Exception e) {

        }
        return converter.HashMapToJSON(user.save());
    }

    @RequestMapping("/user/updateUser")
    public String updateUser(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> userValues = converter.JSONToHashMap(JSON);
        User user = User.find(((Double) userValues.get("id")).intValue());

        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        user.setEmail(converter.toString(userValues.get("email")));

        try {
            user.setFirstName(converter.toString(userValues.get("firstName")));
            user.setLastName(converter.toString(userValues.get("lastName")));
            user.setGender(converter.toInt(userValues.get("gender")));
            user.setAge(converter.toInt(userValues.get("age")));
            user.setFacebookID(converter.toString(userValues.get("facebookID")));
            user.setFacebookFirstName(converter.toString(userValues.get("facebookFirstName")));
            user.setFacebookLastName(converter.toString(userValues.get("facebookLastName")));
        }
        catch (Exception e) {

        }
        return converter.HashMapToJSON(user.save());
    }

    @RequestMapping("/user/changePassword")
    public String changePassword(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));

        User user = User.find(converter.toInt(userData.get("id")));

        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        String currentPassword = crypto.decryption(user.getPassword());
        String oldPassword = "" + data.get("oldPassword");
        String newPassword = "" + data.get("newPassword");
        String confirmPassword = "" + data.get("confirmPassword");

        if(!currentPassword.equals(oldPassword))
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Current password incorrect."));

        if(newPassword.length() < 6)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Password should not be less than 6 letters."));

        if(!newPassword.equals(confirmPassword))
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Password and confirm password mismatch."));

        newPassword = crypto.encryption(newPassword);
        user.setPassword(newPassword);

        return converter.HashMapToJSON(user.save());
    }

    @RequestMapping("/user/delete")
    public String delete(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> userValues = converter.JSONToHashMap(JSON);
        User user = User.find(converter.toInt(userValues.get("id")));

        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        return converter.HashMapToJSON(user.delete());
    }

    @RequestMapping("/user/countAllUsers")
    public String countAllUsers()
    {
        int amount = 0;
        try {
            HashMap<String, Object> data = databaseInquirer.query("SELECT COUNT(id) AS amount FROM user").get(0);
            amount = converter.toInt(data.get("amount"));
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
        return "" + amount;
    }

    @RequestMapping("/user/getUserRank")
    public String getUserRank(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        ArrayList<HashMap<String, Object>> rankList = new ArrayList<>();
        HashMap<String, Object> userData = converter.JSONToHashMap(JSON);
        try {
            databaseInquirer.addBatch("SET @rownum := 0");
            rankList = databaseInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM user ORDER BY score DESC ) as result");
        } catch (Exception e) {
            e.printStackTrace();
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        for(int i = 0; i < rankList.size(); i++) {
            int id = converter.toInt(userData.get("id"));
            if(id == converter.toInt(rankList.get(i).get("id")))
                return "" + ++i;
        }
        return "" + -1;
    }

    @RequestMapping("/user/increaseScore")
    public String increaseScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));
        User user = User.find(converter.toInt(userData.get("id")));
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "User does not exist."));
        HashMap<String, Object> processStatus = user.increaseScore(converter.toInt(data.get("score")), converter.toString(data.get("description")));
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJSON(user.save());
        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot increase score."));
    }

    @RequestMapping("/user/decreaseScore")
    public String decreaseScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));
        User user = User.find(converter.toInt(userData.get("id")));
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "User does not exist."));
        HashMap<String, Object> processStatus = user.decreaseScore(converter.toInt(data.get("score")), converter.toString(data.get("description")));
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJSON(user.save());
        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot decrease score due to internal server error."));
    }

    @RequestMapping("/user/resetScore")
    public String resetScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON);
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));
        User user = User.find(converter.toInt(userData.get("id")));
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "User does not exist."));
        HashMap<String, Object> processStatus = user.decreaseScore(-user.getScore(), converter.toString(data.get("description")));
        if((boolean) processStatus.get("status"))
            return converter.HashMapToJSON(user.save());
        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot reset score due to internal server error."));
    }

    @RequestMapping("/user/login")
    public String login(@RequestParam(value="userName", required = true, defaultValue = "0") String userName,
                        @RequestParam(value="password", required = true, defaultValue = "0") String password)
    {
        User user = null;
        String currentPassword = "";
        try {
            user = User.where("userName", "=", userName)[0];
            currentPassword = Crypto.getInstance().decryption(user.getPassword());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if(user == null || !currentPassword.equals(password))
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "User does not exist or password is incorrect."));

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("user", user.getGeneralValues());

        return converter.HashMapToJSON(JSON);
    }

    public void changeDatabaseInquirer(DatabaseInterface databaseInquirer)
    {
        this.databaseInquirer = databaseInquirer;
    }
}
