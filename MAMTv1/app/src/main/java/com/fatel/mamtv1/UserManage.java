package com.fatel.mamtv1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserManage {
    private static User currentUser = null;
    private static UserManage instance = null;
    private UserManage(){

    }
    public static UserManage getInstance(Context context) {
        if (instance == null) {
            instance = new UserManage();
            User user = User.checkLogin(context);
            if(user!=null){
                currentUser = user;
                Log.i("User", "funh loginuser!=null :"+currentUser.getUserName());
            }
        }
        return instance;
    }

    public void createNewUser(String username,String password,Context context) {
        int idUser = addNewUser(username, password,context);
        currentUser = new User(idUser, username); Log.i("User", "funh createnewuser :" + idUser);
        currentUser.setLogin(1);
        currentUser.save(context); Log.i("User", "funh save :" + idUser);
    }
    public void createFBUser(String facebookID,String facebookFirstName,Context context){
        int idUser = addNewUserFB(facebookID, facebookFirstName);
        currentUser = new User(idUser,facebookID,facebookFirstName);
        currentUser.setLogin(1);
        currentUser.save(context);
    }
    public void loginUser (String username,String password,Context context){
        int idUser = findUser(username, password);
        User user=User.find(idUser, context);
        Log.i("User", "funh get iduser:" + idUser + " id: " + user.getId() + " u:" + user.getUserName());
        if(user!=null){
            currentUser=user;
        }
        else {
            currentUser = new User(idUser, username);
        }
        currentUser.setLogin(1);
        currentUser.save(context);
    }
    public void loginFBUser(String facebookID,String facebookFirstName,Context context){
        int idUser = findUserFB(facebookID, facebookFirstName);
        User user=User.find(idUser, context);
        if(user!=null){
            currentUser=user;
        }
        else {
            currentUser = new User(idUser,facebookID,facebookFirstName);

        }
        currentUser.setLogin(1);
        currentUser.save(context);
    }
    public void logoutUser(Context context){
        currentUser.setLogin(0);
        currentUser.save(context);
        currentUser=null;
        Log.i("User", "funh looutuser :" + currentUser);
    }
    public boolean checkCurrentLogin(Context context){
        User user = User.checkLogin(context);
        if(user!=null){
            return true;
        }
        
        return false;

    }
    // server
    public int checkUser(String username,String password) {
        return 1;
    }

    private int addNewUser(String username,String password,Context context){
        Cache.getInstance().putData("SignUpContext", this);
        String url = "http://203.151.92.196:8080/user/createUser";
        final String un = username;
        final String pw = password;
        final int idUser;
        /////////////edit
       /* StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() { //create new request
            @Override
            public void onResponse(String s) { //when the results have come
                Log.i("volley", s); //throw the result to the console.
                HashMap<String, Object> userData = Converter.getInstance().JsonToHashMap(s); //convert the result into HashMap format
                idUser = Integer.parseInt(userData.get("id").toString());

            }
        }, new Response.ErrorListener() { //create error listener to catch when the error has occurred
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when the error that the server cannot handle by itself has occurred
                Log.i("volley error", volleyError.toString()); //show the error
            }
        }) {
            @Override //override the send parameters to server manually by POST method
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> user = new HashMap<>(); //create HashMap to keep all the values in one place to be 1 object
                user.put("userName", un);
                user.put("password", pw);
                Map<String, String> JSON = new HashMap<>(); //create HashMap again to keep the above user object
                Log.i("info", user.toString());
                JSON.put("JSON", Converter.getInstance().HashMapToJson(user)); //the API receive the values in one parameter name JSON
                return JSON; //send the value name JSON to the server
            }
        }; //end of request's details
        HttpConnector.getInstance(context).addToRequestQueue(stringRequest); //add the request to HTTPConnector, the class will respond the request automatically at separated thread
*/
        //////////////end


        return 0;

    }
    private int findUser(String username,String password){

        return 0;

    }
    private int addNewUserFB(String facebookID,String facebookFirstName){
        return 0;
    }
    private int findUserFB(String facebookID,String facebookFirstName){
        return 0;
    }
    private void updateUser(){
        //update currentuser to server
    }

    // get set
    public void addScore(int score,Context context){
        if(currentUser!=null){
            currentUser.addScore(score);
            currentUser.save(context);
            updateUser();
        }
    }
    public void setFirstName(String firstName,Context context){
        if(currentUser!=null){
            currentUser.setFirstName(firstName);
            currentUser.save(context);
            updateUser();
        }
    }
    public void setLastName(String lastName,Context context){
        if(currentUser!=null){
            currentUser.setLastName(lastName);
            currentUser.save(context);
            updateUser();
        }
    }
    public void setAge(int age,Context context){
        if(currentUser!=null){
            currentUser.setAge(age);
            currentUser.save(context);
            updateUser();
        }
    }
    public void setGender(int gender,Context context){
        if(currentUser!=null){
            currentUser.setGender(gender);
            currentUser.save(context);
            updateUser();
        }
    }
    public void setEmail(String email,Context context){
        if(currentUser!=null){
            currentUser.setEmail(email);
            currentUser.save(context);
            updateUser();
        }
    }
    public void setFacebookID(String facebookID,Context context){
        if(currentUser!=null){
            currentUser.setFacebookID(facebookID);
            currentUser.save(context);
            updateUser();
        }
    }
    public  void setFacebookFirstName(String facebookFirstName,Context context){
        if(currentUser!=null){
            currentUser.setFacebookFirstName(facebookFirstName);
            currentUser.save(context);
            updateUser();
        }
    }
    public void setFacebookLastName(String facebookLastName,Context context){
        if(currentUser!=null){
            currentUser.setFacebookLastName(facebookLastName);
            currentUser.save(context);
            updateUser();
        }
    }
    public void setProfileImage(int profileImage,Context context) {
        if(currentUser!=null){
            currentUser.setProfileImage(profileImage);
            currentUser.save(context);
            updateUser();
        }
    }

    public String getCurrentFirstName (){
        return currentUser.getFirstName();
    }
    public String getCurrentLastName (){
        return currentUser.getLastName();
    }
    public int getCurrentProfileImage(){
        return currentUser.getProfileImage();
    }
    public int getCurrentAge(){
        return currentUser.getAge();
    }
    public int getCurrentScore(){
        return currentUser.getScore();
    }
    public int getCurrentGender() {
        return currentUser.getGender();
    }
    public int getCurrentIdUser(){return currentUser.getIdUser();}
    public String getCurrentEmail(){
        return currentUser.getEmail();
    }
    public String getCurrentFacebookFirstName(){
        return currentUser.getFacebookFirstName();
    }
    public String getCurrentFacebookLastName(){
        return currentUser.getFacebookLastName();
    }
public String getCurrentFacebookId(){return currentUser.getFacebookID();}

}