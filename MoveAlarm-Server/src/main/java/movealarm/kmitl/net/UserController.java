package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return converter.HashMapArrayToJSON(tempMap);
    }

    @RequestMapping("/user/getAllUsers")
    public String all()
    {
        Converter converter = Converter.getInstance();
        User[] users = User.all();

        if(users == null)
            return converter.HashMapToJson(StatusDescription.createProcessStatus(false, "Not found the required users."));

        HashMap<String, Object>[] tempMap = converter.ModelArrayToHashMapArray(users);
        return converter.HashMapArrayToJSON(tempMap);
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
}
