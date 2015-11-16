package movealarm.kmitl.net;

import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by oat90 on 11/15/2015.
 */
public class EventController {
    private Event dailyEvent = null;

    public EventController()
    {

    }

    @RequestMapping("/event/getEvent")
    public String genEvent()
    {
        Event event = new Event();
        Converter converter = Converter.getInstance();
        ArrayList<Posture> allPosture = new ArrayList<>(Arrays.asList(Posture.all()));
        ArrayList<Posture> postures = new ArrayList<>();
        for(int i = 0;i < 5;i++) { //random and pick up 5 posture
            int random = (int)(Math.random() * allPosture.size());
            postures.add(allPosture.get(random));
            allPosture.remove(random);
        }
        Calendar calendar = Calendar.getInstance(); //random time
        calendar.set(Calendar.HOUR_OF_DAY,(int)(Math.random()*24));
        calendar.set(Calendar.MINUTE,(int)(Math.random()*59));
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) + 1);
        Date date = calendar.getTime();

        event.setTime(date);
        event.setPostures(postures);
        this.dailyEvent = event;
        HashMap<String ,Object> JSON = StatusDescription.createProcessStatus(true);
        JSON.put("event",this.dailyEvent.getGeneralValues());

        return converter.HashMapToJSON(JSON);
    }

    public void editEventTime(Date date)
    {
        this.dailyEvent.setTime(date);
    }


    public String getEventDetail()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
