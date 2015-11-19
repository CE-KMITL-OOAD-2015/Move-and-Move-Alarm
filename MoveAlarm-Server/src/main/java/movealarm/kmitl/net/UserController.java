package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class UserController {

    private Converter converter = Converter.getInstance();
    private DatabaseInterface databaseInquirer = SQLInquirer.getInstance();
    private Crypto crypto = Crypto.getInstance();
    
    @RequestMapping("/user/findByID")
    public String findByID(@RequestParam(value="id", required = true, defaultValue = "0") int id)
    {
        User user = User.find(id);
        if(user == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required user."));

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("user", user.getGeneralValues()); //put user's common values

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/user/findByWhere")
    public String where(@RequestParam(value="columnName", required = true, defaultValue = "") String columnName,
                        @RequestParam(value="operator", required = true, defaultValue = "") String operator,
                        @RequestParam(value="value", required = true, defaultValue = "") String value)
    {
        User[] users = User.where(columnName, operator, value); //query from database

        if(users == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required users."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(users); //convert array of model to array of HashMap

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true); //create response object
        JSON.put("users", tempMap); //put and convert to JSON
        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/user/findByRank")
    public String findByRank(@RequestParam(value="startRank", required = true, defaultValue = "0") int startRank,
                             @RequestParam(value="endRank", required = true, defaultValue = "0") int endRank)
    {
        ArrayList<HashMap<String, Object>> rankList = new ArrayList<>(); //create list of rank user
        ArrayList<HashMap<String, Object>> userDataList = new ArrayList<>(); //create list to keep each user's data
        
        startRank = Math.abs(startRank); //convert to positive integer
        try {
            databaseInquirer.addBatch("SET @rownum := 0"); //create temporary variable on database to create rank
            rankList = databaseInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM user ORDER BY score DESC ) as result"); //create temporary table to keep group data with order by score and query group id from the temporary table
        } catch (Exception e) {
            e.printStackTrace();
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        endRank = (Math.abs(endRank) > rankList.size()) ? rankList.size() : Math.abs(endRank); //if received endRank is more than query data, assign max rank to endRank

        for(int i = startRank - 1; i < endRank; i++) { //put user data to the list at start rank to end rank
            HashMap<String, Object> item = rankList.get(i);
            HashMap<String, Object> usersData = User.find(converter.toInt(item.get("id"))).getGeneralValues();
            usersData.put("rank", startRank);
            userDataList.add(usersData);
            startRank++;
        }

        HashMap<String, Object>[] usersDataArray = userDataList.toArray(new HashMap[userDataList.size()]); //convert to normal array

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("users", usersDataArray);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/user/getUserRank") //find ranking of received user
    public String getUserRank(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        ArrayList<HashMap<String, Object>> rankList = new ArrayList<>();
        HashMap<String, Object> userData = converter.JSONToHashMap(JSON);
        try {
            databaseInquirer.addBatch("SET @rownum := 0");
            rankList = databaseInquirer.query("SELECT id FROM " +
                    "( SELECT @rownum := @rownum + 1 AS rank, id, score " +
                    "FROM user ORDER BY score DESC ) as result"); //query rank
        } catch (Exception e) {
            e.printStackTrace();
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(
                    false, "An error has occurred while querying data from the database."));
        }

        for(int i = 0; i < rankList.size(); i++) { //find match group id
            int id = converter.toInt(userData.get("id"));
            if(id == converter.toInt(rankList.get(i).get("id")))
                return "" + ++i;
        }
        return "" + -1; //return rank -1 if found nothing
    }

    @RequestMapping("/user/getAllUsers")
    public String all()
    {
        User[] users = User.all();

        if(users == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Not found the required users."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(users);

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("users", tempMap);

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/user/createUser")
    public String createUser(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> userData = converter.JSONToHashMap(JSON); //convert JSON string to HashMap format
        User[] username_match = User.where("userName", "=", converter.toString(userData.get("userName"))); //check match user name

        if(username_match.length > 0) //if this name is already used
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user name is already used by other user."));

        if((converter.toString(userData.get("password"))).length() < 6)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Password should not be less than 6 letters."));
        
        User user = new User();
        user.setUsername(converter.toString(userData.get("userName")));
        user.setEmail(converter.toString(userData.get("email")));
        user.setPassword(crypto.encryption(converter.toString(userData.get("password")))); //encrypt user's password

        try {
            user.setFirstName(converter.toString(userData.get("firstName")));
            user.setLastName(converter.toString(userData.get("lastName")));
            user.setGender(converter.toInt(userData.get("gender")));
            user.setAge(converter.toInt(userData.get("age")));
            user.setFacebookID(converter.toString(userData.get("facebookID")));
            user.setFacebookFirstName(converter.toString(userData.get("facebookFirstName")));
            user.setFacebookLastName(converter.toString(userData.get("facebookLastName")));
        }
        catch (Exception e) {
            //do nothing
        }

        HashMap<String, Object> response = user.save();
        if((boolean) response.get("status")) {
            response.put("user", user.getGeneralValues());
            return converter.HashMapToJSON(response);
        }

        return converter.HashMapToJSON(response);
    }

    @RequestMapping("/user/updateUser")
    public String updateUser(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON); //convert receive data to HashMap format
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));
        User user = User.find(converter.toInt(userData.get("id")));

        if(user == null)  //if user does not exist
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        user.setEmail(converter.toString(userData.get("email")));

        try {
            user.setFirstName(converter.toString(userData.get("firstName")));
            user.setLastName(converter.toString(userData.get("lastName")));
            if(converter.toString(userData.get("gender")).equals("female"))
                user.setGender(0);
            else if(converter.toString(userData.get("gender")).equals("male"))
                user.setGender(1);
            user.setAge(converter.toInt(userData.get("age")));
            user.setFacebookID(converter.toString(userData.get("facebookID")));
            user.setFacebookFirstName(converter.toString(userData.get("facebookFirstName")));
            user.setFacebookLastName(converter.toString(userData.get("facebookLastName")));
        }
        catch (Exception e) {
            //do nothing
        }
        return converter.HashMapToJSON(user.save());
    }

    @RequestMapping("/user/changePassword")
    public String changePassword(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON); //convert to HashMap format
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user"))); //convert nested JSON to HashMap

        User user = User.find(converter.toInt(userData.get("id"))); //find user in daytabase
        if(user == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        String currentPassword = crypto.decryption(user.getPassword()); //decrypt current password
        String oldPassword = converter.toString(data.get("oldPassword"));
        String newPassword = converter.toString(data.get("newPassword"));
        String confirmPassword = converter.toString(data.get("confirmPassword"));

        if(!currentPassword.equals(oldPassword)) //if password mismatch
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Current password incorrect."));

        if(newPassword.length() < 6)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Password should not be less than 6 letters."));

        if(!newPassword.equals(confirmPassword))
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Password and confirm password mismatch."));

        newPassword = crypto.encryption(newPassword); //encrypt new password
        user.setPassword(newPassword); //put to user

        return converter.HashMapToJSON(user.save()); //return save process status
    }

    @RequestMapping("/user/delete")
    public String delete(@RequestParam(value="JSON", required = true, defaultValue = "") String JSON)
    {
        HashMap<String, Object> userData = converter.JSONToHashMap(JSON); //convert to HashMap
        User user = User.find(converter.toInt(userData.get("id"))); //find the user

        if(user == null) //if found nothing
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "This user does not exist."));

        return converter.HashMapToJSON(user.delete()); //send deleting status
    }

    @RequestMapping("/user/countAllUsers")
    public String countAllUsers()
    {
        int amount = 0;
        try {
            HashMap<String, Object> data = databaseInquirer.query("SELECT COUNT(id) AS amount FROM user").get(0); //create temporary variable in database and let database take its process
            amount = converter.toInt(data.get("amount"));
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
        return "" + amount;
    }

    @RequestMapping("/user/increaseScore")
    public String increaseScore(@RequestParam(value="JSON", required = true, defaultValue = "0") String JSON)
    {
        HashMap<String, Object> data = converter.JSONToHashMap(JSON); //convert to HashMap
        HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user"))); //convert nested JSON to HashMap

        User user = User.find(converter.toInt(userData.get("id"))); //find group in the database
        if(user == null)
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "User does not exist."));

        HashMap<String, Object> processStatus = user.increaseScore(converter.toInt(data.get("score")), converter.toString(data.get("description"))); //increase score, put description of increasing and get process status
        if((boolean) processStatus.get("status")) //if success
            return converter.HashMapToJSON(user.save());

        return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "Cannot increase score."));
    }

    @RequestMapping("/user/decreaseScore") //process like increaseScore method but decrease
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

    @RequestMapping("/user/resetScore") //process like decreaseScore method but set to 0
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
        String currentPassword = null;
        try {
            user = User.where("userName", "=", userName)[0]; //user name is unique so when query data by using user name, the database must return only one row
            currentPassword = crypto.decryption(user.getPassword()); //decrypt and check
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(user == null || !currentPassword.equals(password)) //if user does not exist or password missmatch
            return converter.HashMapToJSON(StatusDescription.createProcessStatus(false, "User does not exist or password is incorrect."));

        Group group = null;
        try {
            HashMap<String, Object> userRawData = databaseInquirer.where("user", "id", "=", "" + user.getID()).get(0);
            group = Group.find(converter.toInt(userRawData.get("groupID")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Object> JSON = StatusDescription.createProcessStatus(true); //create response object and put the data
        JSON.put("user", user.getGeneralValues());

        if(group != null)
            JSON.put("group", group.getGeneralValues());

        return converter.HashMapToJSON(JSON); //convert to JSON string
    }

    @RequestMapping("/user/loginFacebook")
    public String loginFacebook(@RequestParam(value = "facebookID", required = true, defaultValue = "0")String facebookID,
                                @RequestParam(value = "facebookFirstName", required = true, defaultValue = "0")String facebookFirstName)
    {
        User user;
        HashMap<String, Object> JSON;
        try {
            user = User.where("facebookID","=",facebookID)[0];
            user.setFacebookFirstName(facebookFirstName);
            JSON = StatusDescription.createProcessStatus(true);
        }
        catch (Exception e) {
            user = new User();
            user.setFacebookID(facebookID);
            user.setFacebookFirstName(facebookFirstName);
            JSON = user.save();
        }

        Group group = null;
        try {
            HashMap<String, Object> userRawData = databaseInquirer.where("user", "facebookID", "=", "" + user.getFacebookID()).get(0);
            group = Group.find(converter.toInt(userRawData.get("groupID")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(group != null)
            JSON.put("group", group.getGeneralValues());

        JSON.put("user",user.getGeneralValues());

        return converter.HashMapToJSON(JSON);
    }

    public void changeDatabaseInquirer(DatabaseInterface databaseInquirer)
    {
        this.databaseInquirer = databaseInquirer; //this controller can be change database inquirer interface
    }
}
