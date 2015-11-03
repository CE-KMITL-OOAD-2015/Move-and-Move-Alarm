package com.fatel.mamtv1;

import android.provider.BaseColumns;

/**
 * Created by Monthon on 3/11/2558.
 */
public class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String facebookid;
    private String age;
    private String gender;
    private int image;
    public static final String DATABASE_NAME = "fatel_user.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "user";
    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
        public static final String FACEBOOKID = "facebookid";
        public static final String AGE = "age";
        public static final String GENDER = "gender";
        public static final String IMAGE = "image";
    }
    public User(){
    }
    public User(int id, String name, String username, String password, String email,
                String facebookid, String age, String gender, int image){
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.facebookid = facebookid;
        this.age = age;
        this.gender = gender;
        this.image = image;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    public String getFacebookid(){
        return facebookid;
    }
    public String getAge(){
        return age;
    }
    public String getGender(){
        return gender;
    }
    public int getImage(){
        return image;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setFacebookid(String facebookid){
        this.facebookid = facebookid;
    }
    public void setAge(String age){
        this.age = age;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setImage(int image){
        this.image = image;
    }
}
