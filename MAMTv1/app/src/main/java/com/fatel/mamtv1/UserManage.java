package com.fatel.mamtv1;

import android.content.Context;
import android.util.Log;

public class UserManage {

    private User currentUser = null;
    private static UserManage instance = null;
    private UserManage(){

    }
    public static UserManage getInstance() {
        if (instance == null) {
            instance = new UserManage();
        }
        return instance;
    }

    public void createNewUser(String username,String password,Context context) {
        int idUser = addNewUser(username, password);
        currentUser = new User(idUser, username); Log.i("User", "funh createnewuser :"+idUser);
        currentUser.save(context); Log.i("User", "funh save :" + idUser);
    }
    public void createFBUser(String facebookID,String facebookFirstName,Context context){
        int idUser = addNewUserFB(facebookID, facebookFirstName);
        currentUser = new User(idUser,facebookID,facebookFirstName);
        currentUser.save(context);
    }
    public void loginUser (String username,String password,Context context){
        int idUser = findUser(username, password);
        User user=User.find(idUser, context);
        Log.i("User", "funh get iduser:" + idUser+" id: "+user.getId()+" u:"+user.getUserName());
        if(user!=null){
            currentUser=user;
        }
        else {
            currentUser = new User(idUser, username);
            currentUser.save(context);
        }
    }
    public void loginFBUser(String facebookID,String facebookFirstName,Context context){
        int idUser = findUserFB(facebookID, facebookFirstName);
        User user=User.find(idUser, context);
        if(user!=null){
            currentUser=user;
        }
        else {
            currentUser = new User(idUser,facebookID,facebookFirstName);
            currentUser.save(context);
        }
    }
    public void logoutUser(){
        currentUser=null;
    }

    private int addNewUser(String username,String password){

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

    public int checkUser(String username,String password) {
        return 1;
    }

}