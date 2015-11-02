package movealarm.kmitl.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoveAlarmServerApplication {

    public static void main(String[] args) {
        SQLInquirer sqlInquirer = SQLInquirer.getInstance();
        System.out.println("connection : " + sqlInquirer.isConnecting());
        Group
        System.out.println(group.getModifiedDate());
        SpringApplication.run(MoveAlarmServerApplication.class, args);
    }
}
