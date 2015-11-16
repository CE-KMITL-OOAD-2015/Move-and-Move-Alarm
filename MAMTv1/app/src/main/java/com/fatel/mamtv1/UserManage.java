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
            }
        }
        return instance;
    }
    public User getCurrentUser(){
        return currentUser;
    }
    public  void setCurrentUser(User user){
       currentUser= user;
    }

    public void createNewUser(String username,String password,Context context) {

        addNewUser(username, password, context);
        //อันที่เรียก createn
        //int idUser = Integer.parseInt((Cache.getInstance().getData("idUser")).toString());
        //Log.i("User", "funh createnewuser :" + idUser);
        //currentUser = new User(idUser, username);
        //currentUser.setLogin(1);
        //currentUser.save(context); Log.i("User", "funh save :" + idUser);

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


    private void addNewUser(String username,String password, final Context context){

        String url = "http://203.151.92.196:8080/user/createUser";
        final String un = username;
        final String pw = password;
        Log.i("User", "funh addnewuser :"+un+pw );
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
                        Log.i("User", "funh createnewuser :" + idUser);
                        User currentUser = new User(idUser, username);
                        currentUser.setLogin(1);
                        currentUser.save(context); Log.i("User", "funh save :" + idUser);
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
                HashMap<String, Object> userData = Converter.getInstance().JSONToHashMap(s); //convert the result into HashMap format
                Cache.getInstance().putData("idUser", userData.get("id"));
                Log.i("User", "funh addnewuser :" + userData.get("id"));
            }
        }, new Response.ErrorListener() { //create error listener to catch when the error has occurred
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when the error that the server cannot handle by itself has occurred
                Log.i("volley error", volleyError.toString()); //show the error
                Log.i("User", "funh addnewuser :" +volleyError.toString());

            }
        }) {
            @Override //override the send parameters to server manually by POST method
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, Object> user = new HashMap<>(); //create HashMap to keep all the values in one place to be 1 object
                user.put("userName", un);
                user.put("password", pw);
                Map<String, String> JSON = new HashMap<>(); //create HashMap again to keep the above user object
                Log.i("info", user.toString());
                JSON.put("JSON", Converter.getInstance().HashMapToJSON(user)); //the API receive the values in one parameter name JSON
                return JSON; //send the value name JSON to the server
            }
        }; //end of request's details
        Log.i("User", "funh addnewuser http:");
        HttpConnector.getInstance(context).addToRequestQueue(stringRequest); //add the request to HTTPConnector, the class will respond the request automatically at separated thread





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

    public int getCurrentIdUser (){
        return currentUser.getIdUser();
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