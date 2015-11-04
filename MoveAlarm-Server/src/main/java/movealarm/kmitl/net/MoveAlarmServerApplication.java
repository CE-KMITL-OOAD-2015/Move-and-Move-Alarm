package movealarm.kmitl.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class MoveAlarmServerApplication {

    public static void main(String[] args) {
        SQLInquirer sqlInquirer = SQLInquirer.getInstance();
        System.out.println("database connection : " + ((sqlInquirer.isConnecting()) ? "connected" : "not connected"));
        SpringApplication.run(MoveAlarmServerApplication.class, args);
    }
}
