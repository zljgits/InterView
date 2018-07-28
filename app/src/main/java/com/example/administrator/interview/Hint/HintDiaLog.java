package com.example.administrator.interview.Hint;

import android.app.AlertDialog;
import android.content.Context;

import com.example.administrator.interview.R;


public class HintDiaLog {
    public void HintBox(Context context,String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok,null);
        builder.show();



    }
}
