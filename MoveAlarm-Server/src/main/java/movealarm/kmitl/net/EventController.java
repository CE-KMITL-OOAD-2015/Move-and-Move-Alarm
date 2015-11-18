package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by oat90 on 11/15/2015.
 */
@RestController
public class EventController {
    private Event dailyEvent = null;
    private Converter converter = Converter.getInstance();

    @RequestMapping("/event/getEvent")
    public String genEvent()
    {
        Calendar calendar = Calendar.getInstance();
        boolean alreadyGen = false;
        if(this.dailyEvent != null) {
            Date checkDate = this.dailyEvent.getTime();
            Calendar cal_temp = Calendar.getInstance();
            cal_temp.setTime(checkDate);
            if(calendar.get(Calendar.DAY_OF_MONTH) == cal_temp.get(Calendar.DAY_OF_MONTH)) {
                alreadyGen = true;
            }
            else {
                alreadyGen = false;
            }
        }
        if(!alreadyGen) {
            Event event = new Event();
            ArrayList<Posture> allPosture = new ArrayList<>(Arrays.asList(Posture.all()));
            ArrayList<Posture> postures = new ArrayList<>();
            for (int i = 0; i < 5; i++) { //random and pick up 5 posture
                int random = (int) (Math.random() * allPosture.size());
                postures.add(allPosture.get(random));
                allPosture.remove(random);
            }

            int hourRandom = (int) (Math.random() * 23); //random time
            int minuteRandom = (int) (Math.random() * 59);
            while (hourRandom < calendar.get(Calendar.HOUR_OF_DAY) || (hourRandom < calendar.get(Calendar.HOUR_OF_DAY) && minuteRandom < calendar.get(Calendar.MINUTE))) {
                hourRandom = (int) (Math.random() * 23);
                minuteRandom = (int) (Math.random() * 59);
            }
            calendar.set(Calendar.HOUR_OF_DAY, hourRandom);
            calendar.set(Calendar.MINUTE, minuteRandom);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date date = calendar.getTime();

            event.setTime(date);
            event.setPostures(postures);
            this.dailyEvent = event;
        }
        HashMap<String ,Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("event",this.dailyEvent.getGeneralValues());

        return converter.HashMapToJSON(JSON);
    }

    @RequestMapping("/event/getEventFixedTime")
    public String getEventFixedTime(@RequestParam(value = "hour", required = true, defaultValue = "0")int hour,
                                    @RequestParam(value = "minute", required = true, defaultValue = "0")int minute)
    {
        genEvent();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date date = calendar.getTime();
        this.dailyEvent.setTime(date);
        HashMap<String ,Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("event",this.dailyEvent.getGeneralValues());

        return converter.HashMapToJSON(JSON);
    }


    public String getEventDetail()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String detail = new String("");
        detail += "Time: " + sdf.format(this.dailyEvent.getTime()) + "\n";
        int count = 1;
        for(Posture posture : this.dailyEvent.getPostures()) {
            detail += "Posture: " + count + "\n";
            detail += "Title: " + posture.getTitle() + "\n";
            detail += "Description: " + posture.getDescription() + "\n\n";
            count++;
        }
        return detail;
    }


}
