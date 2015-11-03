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
        User user = new User();
        user.setUsername("testJSON_5");
        user.setFirstName("ice");
        user.setLastName("boon");
        user.setPassword("757117");
        //user.setEmail("moobin");
        String JSON = Converter.getInstance().HashMapToJson(user.getValues());
        System.out.println(controller.createUser(JSON));
        //SpringApplication.run(MoveAlarmServerApplication.class, args);
    }
}
