package movealarm.kmitl.net;

import java.util.HashMap;

/**
 * Created by Moobi on 03-Nov-15.
 */
public class StatusDescription {

    /*use to create process status description
      consists of status of process and description of status of process
      true = process is success, false = process is not success or error has occurred
    */

    public static HashMap<String, Object> createProcessStatus(boolean status)
    {
        HashMap<String, Object> processStatus = new HashMap<>();
        processStatus.put("status", status);
        return processStatus;
    }

    public static HashMap<String, Object> createProcessStatus(boolean status, String description)
    {
        HashMap<String, Object> processStatus = new HashMap<>();
        processStatus.put("description", description);
        processStatus.put("status", status);
        return processStatus;
    }
}
