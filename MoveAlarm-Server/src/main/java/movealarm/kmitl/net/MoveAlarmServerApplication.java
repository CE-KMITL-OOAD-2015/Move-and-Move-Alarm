package movealarm.kmitl.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoveAlarmServerApplication {

    public static void main(String[] args) {
        User user = new User();
        //User user = User.find(3);
        user.setAge(20);
        user.setFirstName("Pakin");
        user.setLastName("Boonchoopirach");
        user.setUsername("Enceladus3");
        user.setEmail("moobinkung@windowslive.com");
        user.setGender(1);
        user.setPassword("test");
        user.setScore(50);
        user.save();
        System.out.print(user.getModifiedDate().toString());
        SpringApplication.run(MoveAlarmServerApplication.class, args);
    }
}
