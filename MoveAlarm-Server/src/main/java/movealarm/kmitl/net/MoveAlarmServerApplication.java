package movealarm.kmitl.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class MoveAlarmServerApplication {

    public static void main(String[] args) {
        SQLInquirer sqlInquirer = SQLInquirer.getInstance();
        System.out.println("connection : " + sqlInquirer.isConnecting());
        User[] user = User.where("firstName", "=", "Pakin");
        SpringApplication.run(MoveAlarmServerApplication.class, args);
    }
}
