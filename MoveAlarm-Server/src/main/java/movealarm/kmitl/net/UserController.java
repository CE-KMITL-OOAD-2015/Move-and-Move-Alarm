package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
}
