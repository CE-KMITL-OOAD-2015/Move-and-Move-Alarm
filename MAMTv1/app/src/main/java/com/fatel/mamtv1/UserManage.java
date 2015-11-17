package com.fatel.mamtv1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

                Log.i("User", "loginuser!=null :"+currentUser.getUserName());

            }
        }
        return instance;
    }
    public User getCurrentUser(){
        return currentUser;
    }
    private  void setCurrentUser(User user){
       currentUser= user;
    }

    public void createFBUser(String facebookID,String facebookFirstName,Context context){
        int idUser = addNewUserFB(facebookID, facebookFirstName);
        currentUser = new User(idUser,facebookID,facebookFirstName);
        currentUser.setLogin(1);
        currentUser.save(context);
    }
    public void loginUser (String username,String password, final Context context){

        String url = "http://203.151.92.196:8080/user/login"; //url of login API
        final String un = username;
        final String pw = password;
        StringRequest loginRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Converter converter = Converter.getInstance();
                        Context context = (Context) Cache.getInstance().getData("loginContext");
                        HashMap<String, Object> data = converter.JSONToHashMap(response); //convert JSON to HashMap format


                        if((boolean) data.get("status")) {
                            HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));
                            Log.i("volley", userData.toString()); //throw the message to the console.
                            Log.i("User", "login");
                            int idUser = converter.toInt(userData.get("id"));
                            String username = converter.toString(userData.get("userName"));
                            Log.i("User", "login :" + idUser);
                            if((User.find(idUser, context))!= null){
                                User user=User.find(idUser, context);
                                UserManage.getInstance(context).setCurrentUser(user);
                            }
                            else{
                                User user = new User(idUser, username);
                                UserManage.getInstance(context).setCurrentUser(user);

                            }
                            UserManage.getInstance(context).getCurrentUser().setFirstName(converter.toString(userData.get("firstName")));
                            UserManage.getInstance(context).getCurrentUser().setLastName(converter.toString(userData.get("lastName")));
                            UserManage.getInstance(context).getCurrentUser().setUserName(username);
                            UserManage.getInstance(context).getCurrentUser().setAge(converter.toInt(userData.get("age")));
                            UserManage.getInstance(context).getCurrentUser().setScore(converter.toInt(userData.get("score")));
                            UserManage.getInstance(context).getCurrentUser().setGender(converter.toInt(userData.get("gender")));
                            UserManage.getInstance(context).getCurrentUser().setEmail(converter.toString(userData.get("email")));
                            UserManage.getInstance(context).getCurrentUser().setFacebookID(converter.toString(userData.get("facebookID")));
                            UserManage.getInstance(context).getCurrentUser().setFacebookFirstName(converter.toString(userData.get("facebookFirstName")));
                            UserManage.getInstance(context).getCurrentUser().setFacebookLastName(converter.toString(userData.get("facebookLastName ")));
                            UserManage.getInstance(context).getCurrentUser().setLogin(1);
                            UserManage.getInstance(context).getCurrentUser().save(context);

                            Toast toast = Toast.makeText(context, "Hello "+username, Toast.LENGTH_SHORT);
                            toast.show();
                            context.startActivity(new Intent(context, MainActivity.class));
                        }
                        else {
                            Toast toast = Toast.makeText(context, converter.toString(data.get("description")), Toast.LENGTH_SHORT);
                            toast.show();
                        }



                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString()); //throw the error message to the console
                  }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>(); //create map to keep variables
                map.put("userName", un); //API variable name
                map.put("password", pw);

                return map;
            }
        };

        HttpConnector.getInstance(context).addToRequestQueue(loginRequest); //add the request to HTTPConnector, the class will respond the request automatically at separated thread


        /////
    }
    public void loginFBUser(String facebookID,String facebookFirstName,Context context){

        String url = "http://203.151.92.196:8080/user/loginFacebook"; //url of login API
        final String id = facebookID;
        final String name = facebookFirstName;
        StringRequest loginFBRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Converter converter = Converter.getInstance();
                        Context context = (Context) Cache.getInstance().getData("loginFBContext");
                        HashMap<String, Object> data = converter.JSONToHashMap(response); //convert JSON to HashMap format

                        if((boolean) data.get("status")) {
                            HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));
                            Log.i("volley", userData.toString()); //throw the message to the console.
                            Log.i("User", "loginFB");
                            int idUser = converter.toInt(userData.get("id"));
                            String facebookID = converter.toString(userData.get("facebookID"));
                            String facebookFirstName = converter.toString(userData.get("facebookFirstName"));
                            Log.i("User", "login :" + idUser);
                            if((User.find(idUser, context))!= null){
                                User user=User.find(idUser, context);
                                UserManage.getInstance(context).setCurrentUser(user);
                            }
                            else{
                                User user = new User(idUser, facebookID,facebookFirstName);
                                UserManage.getInstance(context).setCurrentUser(user);

                            }
                            UserManage.getInstance(context).getCurrentUser().setFirstName(converter.toString(userData.get("firstName")));
                            UserManage.getInstance(context).getCurrentUser().setLastName(converter.toString(userData.get("lastName")));
                            UserManage.getInstance(context).getCurrentUser().setUserName(converter.toString(userData.get("userName")));
                            UserManage.getInstance(context).getCurrentUser().setAge(converter.toInt(userData.get("age")));
                            UserManage.getInstance(context).getCurrentUser().setScore(converter.toInt(userData.get("score")));
                            UserManage.getInstance(context).getCurrentUser().setGender(converter.toInt(userData.get("gender")));
                            UserManage.getInstance(context).getCurrentUser().setEmail(converter.toString(userData.get("email")));
                            UserManage.getInstance(context).getCurrentUser().setFacebookID(converter.toString(userData.get("facebookID")));
                            UserManage.getInstance(context).getCurrentUser().setFacebookFirstName(converter.toString(userData.get("facebookFirstName")));
                            UserManage.getInstance(context).getCurrentUser().setFacebookLastName(converter.toString(userData.get("facebookLastName ")));
                            UserManage.getInstance(context).getCurrentUser().setLogin(1);
                            UserManage.getInstance(context).getCurrentUser().save(context);

                            Toast toast = Toast.makeText(context, "Hello "+facebookFirstName, Toast.LENGTH_SHORT);
                            toast.show();
                            context.startActivity(new Intent(context, MainActivity.class));
                        }
                        else {
                            Toast toast = Toast.makeText(context, converter.toString(data.get("description")), Toast.LENGTH_SHORT);
                            toast.show();
                        }



                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString()); //throw the error message to the console
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>(); //create map to keep variables
                map.put("facebookID", id); //API variable name
                map.put("facebookFirstName", name);

                return map;
            }
        };

        HttpConnector.getInstance(context).addToRequestQueue(loginFBRequest); //add the request to HTTPConnector, the class will respond the request automatically at separated thread

////
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


    public void createNewUser(String username,String password, final Context context){

        String url = "http://203.151.92.196:8080/user/createUser";
        final String un = username;
        final String pw = password;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() { //create new request
            @Override
            public void onResponse(String s) { //when the results have come
                if (s!=null) {
                    Converter converter = Converter.getInstance();
                    Context context = (Context) Cache.getInstance().getData("CreateAccountContext");
                    Log.i("volley", s); //throw the result to the console.
                    HashMap<String, Object> data = converter.JSONToHashMap(s);
                    HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(data.get("user")));

                    if((boolean) data.get("status")) {
                        int idUser = converter.toInt(userData.get("id"));
                        String username = converter.toString(data.get("userName"));
                        Log.i("User", "createnewuser :" + idUser);
                        User currentUser = new User(idUser, username);
                        currentUser.setLogin(1);
                        currentUser.save(context); Log.i("User", "save :" + idUser);
                        UserManage.getInstance(context).setCurrentUser(currentUser);
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                    else {
                        Toast toast = Toast.makeText(context, converter.toString(data.get("description")), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else
                    Log.i("volley", "s==null");

                /*HashMap<String, Object> userData = Converter.getInstance().JsonToHashMap(s); //convert the result into HashMap format
                Cache.getInstance().putData("idUser", userData.get("id"));
                Log.i("User", "addnewuser :" + userData.get("id"));*/

            }
        }, new Response.ErrorListener() { //create error listener to catch when the error has occurred
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when the error that the server cannot handle by itself has occurred
                Log.i("volley error", volleyError.toString()); //show the error

            }
        }) {
            @Override //override the send parameters to server manually by POST method
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, Object> user = new HashMap<>(); //create HashMap to keep all the values in one place to be 1 object
                user.put("userName", un);
                user.put("password", pw);
                user.put("facebookID","0");
                Map<String, String> JSON = new HashMap<>(); //create HashMap again to keep the above user object
                Log.i("info", user.toString());
                JSON.put("JSON", Converter.getInstance().HashMapToJSON(user)); //the API receive the values in one parameter name JSON
                return JSON; //send the value name JSON to the server
            }
        }; //end of request's details
        Log.i("User", "addnewuser http:");
        HttpConnector.getInstance(context).addToRequestQueue(stringRequest); //add the request to HTTPConnector, the class will respond the request automatically at separated thread

    }
    private int addNewUserFB(String facebookID,String facebookFirstName){
        return 0;
    }
    private int findUserFB(String facebookID,String facebookFirstName){
        return 0;
    }



    public int checkUser(String username,String password) {
        return 1;
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

    //public int getCurrentIdUser (){
//        return currentUser.getIdUser();
//    }
    public String getCurrentFacebookID(){
        return currentUser.getFacebookID();
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
public String getCurrentUsername(){return currentUser.getUserName();}
    public int getCurrentIdUser() {
        return currentUser.getIdUser();
    }
    public int getCurrentIdGroup(){
        return currentUser.getIdGroup();
    }
    public void setIdGroup(int idGroup,Context context){
        if(currentUser!=null){
            currentUser.setIdGroup(idGroup);
            currentUser.save(context);
            updateUser();
        }
    }
}