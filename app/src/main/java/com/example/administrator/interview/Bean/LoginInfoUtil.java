package com.example.administrator.interview.Bean;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginInfoUtil {

    public static void save(Context context, String username, String password){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
    }

    public static LoginInfoBean loading(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE);
        LoginInfoBean loginInfoBean=new LoginInfoBean();
        loginInfoBean.setUsername(sharedPreferences.getString("username",""));
        loginInfoBean.setPassword(sharedPreferences.getString("password",""));
        return loginInfoBean;
    }

}
