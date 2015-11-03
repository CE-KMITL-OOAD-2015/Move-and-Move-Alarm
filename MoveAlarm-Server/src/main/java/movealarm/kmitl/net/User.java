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
    //public static String tableName = "user";

    public User()
    {
        this.tableName = "user";
        this.requiredFields = new ArrayList<>();
        this.requiredFields.add("firstName");
        this.requiredFields.add("lastName");
        this.requiredFields.add("userName");
        this.requiredFields.add("email");
        this.requiredFields.add("password");
    }

    public static User find(int id)
    {
        HashMap<String, Object> temp = modelCollection.find("user", id);
        if(temp == null)
            return null;
        User model = new User();
        model.id = (int) temp.get("id");
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
        ArrayList<HashMap<String, Object>> temp = modelCollection.where("user", colName, operator, value);
        ArrayList<User> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
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

        return  collection.toArray(new User[collection.size()]);
    }

    public static User[] where(String colName, String operator, String value, String extraCondition)
    {
        return where(colName, operator, value + " " + extraCondition);
    }

    public static User[] all()
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.all("user");
        ArrayList<User> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
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

        return  collection.toArray(new User[collection.size()]);
    }

    @Override
    public HashMap<String, Object> getValues()
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
        if(age < 1)
            return createProcessStatus(false, "Age should not be less than 1.");

        this.age = age;
        updateModifiedDate();

        return createProcessStatus(true);
    }

    public HashMap<String, Object> setGender(int gender)
    {
        if(gender > 1 || gender < 0)
            return createProcessStatus(false, "Undefined gender.");

        this.gender = gender;
        updateModifiedDate();

        return createProcessStatus(true);
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

    public int increaseScore(int score)
    {
        this.score += Math.abs(score);
        updateModifiedDate();
        return this.score;
    }

    public int decreaseScore(int score)
    {
        this.score -= Math.abs(score);
        updateModifiedDate();
        return this.score;
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

    public int getGender()
    {
        return gender;
    }

    public String getGenderDefinition()
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
}
