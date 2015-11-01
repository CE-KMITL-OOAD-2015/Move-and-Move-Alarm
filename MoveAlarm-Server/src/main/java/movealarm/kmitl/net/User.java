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
    public static User model = null;
    //public static String tableName = "user";

    public User()
    {
        this.tableName = "user";
    }

    public static User find(int id)
    {
        HashMap<String, Object> temp = modelCollection.find("user", id);
        model = new User();
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
        System.out.println(model.modifiedDate);
        return model;
    }

    public static User[] where(String colName, String operator, String value)
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.where("user", colName, operator, value);
        ArrayList<User> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
            model = new User();
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
        ArrayList<HashMap<String, Object>> temp = modelCollection.all(tableName);
        ArrayList<User> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
            model = new User();
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
            collection.add(model);
        }

        return  collection.toArray(new User[collection.size()]);
    }

    @Override
    public HashMap<String, Object> getValues()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
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

    public boolean setUsername(String userName)
    {
        if(!(this.userName == null))
            return false;
        //System.out.println(this.password.isEmpty());
        this.userName = userName;
        updateModifiedDate();
        return true;
    }

    public void setAge(int age)
    {
        this.age = age;
        updateModifiedDate();
    }

    public void setGender(int gender)
    {
        this.gender = gender;
        updateModifiedDate();
    }

    public void setEmail(String email)
    {
        this.email = email;
        updateModifiedDate();
    }

    public boolean setPassword(String password)
    {
        if(!(this.password == null))
            return false;
        //System.out.println(this.password.isEmpty());
        this.password = password;
        updateModifiedDate();
        return true;
    }

    public boolean setScore(int score)
    {
        if(score < 0)
            return false;
        this.score = score;
        return true;
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

    public boolean setNewPassword(String oldPassword, String newPassword)
    {
        updateModifiedDate();
        return false;
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
