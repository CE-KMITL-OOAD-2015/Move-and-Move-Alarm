package movealarm.kmitl.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class MoveAlarmServerApplication {

    public static void main(String[] args) {
        SQLInquirer sqlInquirer = SQLInquirer.getInstance();
        System.out.println("connection : " + sqlInquirer.isConnecting());
        UserController controller = new UserController();
        User user = User.find(15);
        HashMap<String, Object> values = user.getValues();
        values.put("oldPassword", "7571179");
        values.put("newPassword", "7571179");
        values.put("confirmPassword", "7571179");
        values.put("id", 15);
        String JSON = Converter.getInstance().HashMapToJson(values);

        System.out.println(controller.changePassword(JSON));
        //SpringApplication.run(MoveAlarmServerApplication.class, args);
    }
}
