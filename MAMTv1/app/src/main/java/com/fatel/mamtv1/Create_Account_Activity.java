package com.fatel.mamtv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Create_Account_Activity extends AppCompatActivity {

    UserManage mUserManage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create__account_layout);
        mUserManage = UserManage.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create__account_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createAccountAndLinkMain(View view)
    {
        EditText username;
        EditText password;
        EditText rePassword;
        username = (EditText)findViewById(R.id.enter_username);
        password = (EditText)findViewById(R.id.enter_password);
        rePassword = (EditText)findViewById(R.id.enter_repassword);
         Cache.getInstance().putData("CreateAccountContext", this);



        if(username.getText().toString().equals("") || password.getText().toString().equals(""))
        {
            Toast toast = Toast.makeText(this, "Please enter Username and Password", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if((username.getText().toString().length()<6) || (password.getText().toString().length()<6))
        {
            Toast toast = Toast.makeText(this, "Please enter Username and Password at least 6 characters", Toast.LENGTH_SHORT);
            toast.show();
        }

        else if(password.getText().toString().equals(rePassword.getText().toString()))
        {

            //ลงทะเบียน username กับ password ลงฐานข้อมูลก่อนไปหน้า main
            mUserManage.createNewUser(username.getText().toString(),password.getText().toString(),this);

        }
    }
}