package com.fatel.mamtv1;

import android.content.Context;

public class UserManage {

    private User currentUser = null;
    private static UserManage instance = null;
    public UserManage(){

    }
    public static UserManage getInstance() {
        if (instance == null) {
            instance = new UserManage();
        }
        return instance;
    }

    public void createNewUser(String username,String password,Context context) {
        int idUser = addNewUser(username, password);
        currentUser = new User(idUser, username, password);
        currentUser.save(context);
    }
    public void createFBUser(){

    }
    public void loginUser(String username,String password,Context context){
        int idUser = findUser(username,password);
//        User user=User.find(idUser, context);
//        if(user!=null){
//            currentUser=user;
//        }
//        else {
            currentUser = new User(idUser, username, password);
            currentUser.save(context);
//        }
    }
    public void loginFBUser(){

    }

    private int addNewUser(String username,String password){

        return 0;

    }
    private int findUser(String username,String password){

        return 0;

    }

    public boolean checkUser(String username,String password)
    {
        //เช็คว่าใน server มีข้อมูลของ username นี้ password นี้ รึป่าว ถ้าไม่มีก้ไม่อนุญาตให้เข้า ถ้ามีก้อนุญาต
        return true;
    }
}