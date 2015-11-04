package com.fatel.mamtv1;

import android.content.Context;
import android.provider.BaseColumns;

/**
 * Created by Monthon on 3/11/2558.
 */
public class User {
    private int id;
    private int idUser;
    private String firstName;
    private String lastName;
    private String userName;
    private int age;
    private int score;
    private int gender;
    private String email;
    private String password;
    private String facebookID;
    private String facebookFirstName;
    private String facebookLastName;
    //no sure int for image
    private int profileImage;

    public static final String DATABASE_NAME = "fatel_user.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "user";
    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String IDUSER = "iduser";
        public static final String FIRSTNAME = "firstname";
        public static final String LASTNAME = "lastname";
        public static final String USERNAME = "username";
        public static final String AGE = "age";
        public static final String SCORE = "score";
        public static final String GENDER = "gender";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String FACEBOOKID = "facebookid";
        public static final String FACEBOOKFIRSTNAME = "facebookfirstname";
        public static final String FACEBOOKLASTNAME = "facebooklastname";
        public static final String PROFILEIMAGE = "profileimage";
    }
    public User(){
    }
    public User(int idUser, String username,String password){
        this.id=-1;
        this.idUser = idUser;
        this.userName = username;
        this.password = password;
    }
    public User(int id,int idUser,String firstName, String lastName, String username,int age,int score
            ,int gender,String email, String password, String facebookID, String facebookFirstName,
                String facebookLastName, int profileImage){
        this.id=id;
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = username;
        this.age = age;
        this.score = score;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.facebookID=facebookID;
        this.facebookFirstName=facebookFirstName;
        this.facebookLastName=facebookLastName;
        this.profileImage = profileImage;
    }
    public void save (Context context){

        UserHelper userHelper = new UserHelper(context);
        if(this.id != -1)
            this.id = userHelper.addUser(this);
        else
            userHelper.updateUser(this);
    }
    public static User find(int idUser,Context context){
        UserHelper userHelper = new UserHelper(context);
        return userHelper.getUser(idUser);
    }

    public int getId(){
        return id;
    }
    public int getIdUser(){
        return idUser;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getUserName(){
        return userName;
    }
    public int getAge(){
        return age;
    }
    public int getScore(){
        return score;
    }
    public int getGender(){
        return gender;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getFacebookID(){
        return facebookID;
    }
    public String getFacebookFirstName(){
        return facebookFirstName;
    }
    public String getFacebookLastName(){
        return facebookLastName;
    }
    public int getProfileImage(){
        return profileImage;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setIdUser(int idUser){
        this.idUser = idUser;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setAge(int age){
        this.age = age;
    }
    public void setScore(int score){
        this.score = score;
    }
    public void setGender(int gender){
        this.gender = gender;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setFacebookID(String facebookID){
        this.facebookID = facebookID;
    }
    public  void setFacebookFirstName(String facebookFirstName){
        this.facebookFirstName = facebookFirstName;
    }
    public void setFacebookLastName(String facebookLastName){
        this.facebookLastName = facebookLastName;
    }
    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
