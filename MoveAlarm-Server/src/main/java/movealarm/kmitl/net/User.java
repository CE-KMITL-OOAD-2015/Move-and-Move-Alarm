package movealarm.kmitl.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Moobi on 30-Oct-15.
 */
public class User extends Model{
    private String firstName = null;
    private String lastName = null;
    private String userName = null;
    private int age = 0;
    private int score = 0;
    private int gender = 0;
    private String email = null;
    private String password = null;
    private String facebookID = null;
    private String facebookFirstName = null;
    private String facebookLastName = null;
    private Object profileImage = null;
    private Object coverImage = null;
    private ArrayList<HashMap<String, Object>> temp_scoreLogList = null;
    private ArrayList<HashMap<String, Object>> temp_activityLogList = null;

    public User()
    {
        this.tableName = "user"; //table name of user data in the database
        //this.addRequiredField("firstName"); //add required fields that must be filled before saving
        //this.addRequiredField("lastName");
        //this.addRequiredField("userName");
        //this.addRequiredField("email");
        //this.addRequiredField("password");
        temp_scoreLogList = new ArrayList<>();
        temp_activityLogList = new ArrayList<>();
    }

    public static User find(int id)
    {
        HashMap<String, Object> temp = modelCollection.find("user", id); //query data from the database
        if(temp == null) //if no data
            return null;

        User model = new User(); //create model
        model.id = (int) temp.get("id"); //mapping fields process
        model.createdDate = (Date) temp.get("createdDate");
        model.firstName = "" + temp.get("firstName");
        model.lastName = "" + temp.get("lastName");
        model.userName = "" + temp.get("userName");
        model.age = (int) temp.get("age");
        model.score = (int) temp.get("score");
        model.gender = (int) temp.get("gender");
        model.email = "" + temp.get("email");
        model.password = "" + temp.get("password");
        model.facebookID = "" + temp.get("facebookID");
        model.facebookFirstName = "" + temp.get("facebookFirstName");
        model.facebookLastName = "" + temp.get("facebookLastName");
        model.modifiedDate = (Date) temp.get("modifiedDate");
        return model;
    }

    public static User[] where(String colName, String operator, String value)
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.where("user", colName, operator, value); //query data
        ArrayList<User> collection = new ArrayList<>();

        for(HashMap<String, Object> item : temp) { //mapping values
            User model = new User();
            model.id = (int) item.get("id");
            model.createdDate = (Date) item.get("createdDate");
            model.firstName = (String) item.get("firstName");
            model.lastName = (String) item.get("lastName");
            model.userName = (String) item.get("userName");
            model.age = (int) item.get("age");
            model.score = (int) item.get("score");
            model.gender = (int) item.get("gender");
            model.email = (String) item.get("email");
            model.password = (String) item.get("password");
            model.facebookID = (String) item.get("facebookID");
            model.facebookFirstName = (String) item.get("facebookFirstName");
            model.facebookLastName = (String) item.get("facebookLastName");
            model.modifiedDate = (Date) item.get("modifiedDate");
            collection.add(model);
        }

        return  collection.toArray(new User[collection.size()]); //convert to normal array and return
    }

    public static User[] where(String colName, String operator, String value, String extraCondition)
    {
        return where(colName, operator, value + " " + extraCondition); //where with extra condition
    }

    public static User[] all()
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.all("user"); //quey data
        ArrayList<User> collection = new ArrayList<>();

        for(HashMap<String, Object> item : temp) { //mapping values
            User model = new User();
            model.id = (int) item.get("id");
            model.createdDate = (Date) item.get("createdDate");
            model.firstName = (String) item.get("firstName");
            model.lastName = (String) item.get("lastName");
            model.userName = (String) item.get("userName");
            model.age = (int) item.get("age");
            model.score = (int) item.get("score");
            model.gender = (int) item.get("gender");
            model.email = (String) item.get("email");
            model.password = (String) item.get("password");
            model.facebookID = (String) item.get("facebookID");
            model.facebookFirstName = (String) item.get("facebookFirstName");
            model.facebookLastName = (String) item.get("facebookLastName");
            model.modifiedDate = (Date) item.get("modifiedDate");
            collection.add(model);
        }

        return  collection.toArray(new User[collection.size()]); //convert to normal array and return
    }

    @Override
    public HashMap<String, Object> getValues() //get all values from model
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("firstName", firstName);
        temp.put("lastName", lastName);
        temp.put("userName", userName);
        temp.put("age", age);
        temp.put("score", score);
        temp.put("gender", gender);
        temp.put("email", email);
        temp.put("password", password);
        temp.put("facebookID", facebookID);
        temp.put("facebookFirstName", facebookFirstName);
        temp.put("facebookLastName", facebookLastName);
        if(modifiedDate == null)
            temp.put("modifiedDate", null);
        else
            temp.put("modifiedDate", sdf.format(modifiedDate));
        return temp;
    }

    @Override
    public HashMap<String, Object> getGeneralValues() //get only common values
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("id", id);
        temp.put("firstName", firstName);
        temp.put("lastName", lastName);
        temp.put("userName", userName);
        temp.put("age", age);
        temp.put("score", score);
        temp.put("gender", getGenderDefinition());
        temp.put("email", email);
        temp.put("facebookID", facebookID);
        temp.put("facebookFirstName", facebookFirstName);
        temp.put("facebookLastName", facebookLastName);
        return temp;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
        updateModifiedDate();
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
        updateModifiedDate();
    }

    public void setUsername(String userName)
    {
        this.userName = userName;
        updateModifiedDate();
    }

    public HashMap<String, Object> setAge(int age)
    {
        if(age < 1) //prevent from improper value
            return StatusDescription.createProcessStatus(false, "Age should not be less than 1.");

        this.age = age;
        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    public HashMap<String, Object> setGender(int gender) // 0 = female, 1 = male
    {
        if(gender > 1 || gender < 0) //gender must be 0 or 1
            return StatusDescription.createProcessStatus(false, "Undefined gender.");

        this.gender = gender;
        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    public void setEmail(String email)
    {
        this.email = email;
        updateModifiedDate();
    }

    public void setPassword(String password)
    {
        this.password = password;
        updateModifiedDate();
    }

    public void setScore(int score)
    {
        this.score = score;
        updateModifiedDate();
    }

    public HashMap<String, Object> increaseScore(int score, String description) //this method will create score log and put into temporary log list
    { //score log will not be inserted to the database until method save is called
        HashMap<String, Object> temp_scoreLog = new HashMap<>();
        int changeScore = Math.abs(score); //prevent from improper integer

        this.score += changeScore;
        temp_scoreLog.put("id", id);
        temp_scoreLog.put("currentScore", this.score); //a score after save
        temp_scoreLog.put("modifiedScore", changeScore);
        temp_scoreLog.put("description", description);
        temp_scoreLogList.add(temp_scoreLog); //add to temp

        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    public HashMap<String, Object> decreaseScore(int score, String description) //like increaseScore method but decrease
    {
        HashMap<String, Object> temp_scoreLog = new HashMap<>();
        int changeScore = Math.abs(score);

        if(this.score - changeScore < 0)
            return StatusDescription.createProcessStatus(false, "The score cannot be under zero.");

        this.score -= changeScore;
        temp_scoreLog.put("id", id);
        temp_scoreLog.put("currentScore", this.score);
        temp_scoreLog.put("modifiedScore", -changeScore);
        temp_scoreLog.put("description", description);
        temp_scoreLogList.add(temp_scoreLog);

        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    public void setProfileImage(Object profileImage)
    {
        this.profileImage = profileImage;
        updateModifiedDate();
    }

    public void setCoverImage(Object coverImage)
    {
        this.coverImage = coverImage;
        updateModifiedDate();
    }

    public void setFacebookID(String facebookID)
    {
        this.facebookID = facebookID;
        updateModifiedDate();
    }

    public void setFacebookFirstName(String facebookFirstName)
    {
        this.facebookFirstName = facebookFirstName;
        updateModifiedDate();
    }

    public void setFacebookLastName(String facebookLastName)
    {
        this.facebookLastName = facebookLastName;
        updateModifiedDate();
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public int getAge()
    {
        return age;
    }

    public int getScore()
    {
        return score;
    }

    public int getGender()
    {
        return gender;
    }

    public String getGenderDefinition() //definition of gender number
    {
        switch (gender) {
            case 0:
                return "female";
            case 1:
                return "male";
            default:
                return "undefined";
        }
    }

    public String getPassword() {
        return password;
    }

    public String getEmail()
    {
        return email;
    }

    public Object getProfileImage()
    {
        return profileImage;
    }

    public Object getCoverImage()
    {
        return coverImage;
    }

    public String getFacebookID()
    {
        return facebookID;
    }

    public String getFacebookFirstName()
    {
        return facebookFirstName;
    }

    public String getFacebookLastName()
    {
        return facebookLastName;
    }

    public Date getModifiedDate()
    {
        return modifiedDate;
    }

    @Override
    public HashMap<String, Object> save()
    {
        HashMap<String, Object> requiredFields = checkRequiredFields();
        if(requiredFields != null)
            return requiredFields;

        if(createdDate == null) {
            HashMap<String, Object> temp = modelCollection.create(this);
            if(temp == null)
                return StatusDescription.createProcessStatus(false, "Cannot save due to a database error.");
            id = Integer.parseInt("" + temp.get("id"));
            createdDate = (Date) temp.get("created_date");
            return StatusDescription.createProcessStatus(true);
        }

        HashMap<String, Object> addScoreLogStatus = addScoreLogToDatabase(); //add score log to database
        if(addScoreLogStatus != null) //if an error has occurred while adding score logs to the database
            return addScoreLogStatus; //break saving process and return error description

        temp_scoreLogList = null;
        return StatusDescription.createProcessStatus(modelCollection.save(this));
    }

    @Override
    public HashMap<String, Object> delete()
    {
        if(modelCollection.manualDeleteData("user_score", "user_ID = " + id)) //if deleting is success
            return StatusDescription.createProcessStatus(modelCollection.delete(this));

        return StatusDescription.createProcessStatus(false, "An error has occurred while deleting data.");
    }

    public HashMap<String, Object> addScoreLogToDatabase()
    {
        if(temp_scoreLogList.size() != 0) { //if temporary list is not empty
            String[] valuesSet = new String[temp_scoreLogList.size()]; //create array to keep a set of values of each score log

            for(int i = 0; i < temp_scoreLogList.size(); i++) {
                HashMap<String, Object> item = temp_scoreLogList.get(i);
                valuesSet[i] = "" + item.get("id") + ", " + item.get("currentScore") + ", " + item.get("modifiedScore") + ", '" + item.get("description") + "'"; //concat string
            }

            String colNameSet = "user_id, currentScore, modifiedScore, description"; //set of column name of values

            if(!modelCollection.manualInsertDataMultiple("user_score", colNameSet, valuesSet)) { //if inserting data to the database is error
                System.out.println("An error has occurred while adding a score log.");
                return StatusDescription.createProcessStatus(false, "An error has occurred while adding a score log.");
            }

            temp_scoreLogList.clear(); //clear temp score log list
        }

        return null;
    }
}
