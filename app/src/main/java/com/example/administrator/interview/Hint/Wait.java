package com.example.administrator.interview.Hint;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.administrator.interview.R;

public class Wait {
    public static Dialog dialog;
    public static final void OPEN(Context context){
        dialog =new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wait_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static final void Close(){
        dialog.dismiss();
    }

}
