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
        this.requiredFields.add("first_name");
        this.requiredFields.add("last_name");
        this.requiredFields.add("user_name");
        this.requiredFields.add("email");
        this.requiredFields.add("password");
    }

    public static User find(int id)
    {
        HashMap<String, Object> temp = modelCollection.find("user", id);
        User model = new User();
        model.id = (int) temp.get("id");
        model.createdDate = (Date) temp.get("created_date");
        model.firstName = "" + temp.get("first_name");
        model.lastName = "" + temp.get("last_name");
        model.userName = "" + temp.get("user_name");
        model.age = (int) temp.get("age");
        model.score = (int) temp.get("score");
        model.gender = (int) temp.get("gender");
        model.email = "" + temp.get("email");
        model.password = "" + temp.get("password");
        model.facebookID = "" + temp.get("facebook_id");
        model.facebookFirstName = "" + temp.get("facebook_firstname");
        model.facebookLastName = "" + temp.get("facebook_lastname");
        model.modifiedDate = (Date) temp.get("modified_date");
        return model;
    }

    public static User[] where(String colName, String operator, String value)
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.where("user", colName, operator, value);
        ArrayList<User> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
            User model = new User();
            model.id = (int) item.get("id");
            model.createdDate = (Date) item.get("created_date");
            model.firstName = (String) item.get("first_name");
            model.lastName = (String) item.get("last_name");
            model.userName = (String) item.get("user_name");
            model.age = (int) item.get("age");
            model.score = (int) item.get("score");
            model.gender = (int) item.get("gender");
            model.email = (String) item.get("email");
            model.password = (String) item.get("password");
            model.facebookID = (String) item.get("facebook_id");
            model.facebookFirstName = (String) item.get("facebook_firstname");
            model.facebookLastName = (String) item.get("facebook_lastname");
            model.modifiedDate = (Date) item.get("modified_date");
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
            model.createdDate = (Date) item.get("created_date");
            model.firstName = (String) item.get("first_name");
            model.lastName = (String) item.get("last_name");
            model.userName = (String) item.get("user_name");
            model.age = (int) item.get("age");
            model.score = (int) item.get("score");
            model.gender = (int) item.get("gender");
            model.email = (String) item.get("email");
            model.password = (String) item.get("password");
            model.facebookID = (String) item.get("facebook_id");
            model.facebookFirstName = (String) item.get("facebook_firstname");
            model.facebookLastName = (String) item.get("facebook_lastname");
            model.modifiedDate = (Date) item.get("modified_date");
            collection.add(model);
        }

        return  collection.toArray(new User[collection.size()]);
    }

    @Override
    public HashMap<String, Object> getValues()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("first_name", "'" + firstName + "'");
        temp.put("last_name", "'" + lastName + "'");
        temp.put("user_name", "'" + userName + "'");
        temp.put("age", "'" + age + "'");
        temp.put("score", "'" + score + "'");
        temp.put("gender", "'" + gender + "'");
        temp.put("email", "'" + email + "'");
        temp.put("password", "'" + password + "'");
        temp.put("facebook_id", "'" + facebookID + "'");
        temp.put("facebook_firstname", "'" + facebookFirstName + "'");
        temp.put("facebook_lastname", "'" + facebookLastName + "'");
        if(modifiedDate == null)
            temp.put("modified_date", "'" + null + "'");
        else
            temp.put("modified_date", "'" + sdf.format(modifiedDate) + "'");
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
