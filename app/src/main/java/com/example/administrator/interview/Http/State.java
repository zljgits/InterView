package com.example.administrator.interview.Http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.administrator.interview.Hint.HintDiaLog;


/**
 * Created by Administrator on 2018/5/8.
 */

public class State {
    ConnectivityManager manager;
    public void show(Context context){
        manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        NetworkInfo.State gprs=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        HintDiaLog popDialog=new HintDiaLog();
        if(!wifi.equals(NetworkInfo.State.CONNECTED)&&!gprs.equals(NetworkInfo.State.CONNECTED)){
            popDialog.HintBox(context,"提示","无网络开启");
        }else if (wifi.equals(NetworkInfo.State.CONNECTED)){
            popDialog.HintBox(context,"提示","无线网络开启");
        }else if(gprs.equals(NetworkInfo.State.CONNECTED)){
            popDialog.HintBox(context,"提示","移动网络开启");
        }
    }
}
