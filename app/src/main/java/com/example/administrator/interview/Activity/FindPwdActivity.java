package com.example.administrator.interview.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.interview.Hint.HintDiaLog;
import com.example.administrator.interview.Http.HttpThread;
import com.example.administrator.interview.Http.IP;
import com.example.administrator.interview.Http.SystemNetWork;
import com.example.administrator.interview.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FindPwdActivity extends Activity {
    private TextView FindPwdName,back;//找回密码，返回
    private LinearLayout FindPwdInput,LayoutBack;//input布局,返回布局
    private EditText FindPwdAccount,FindPwdNewPwd,FindPwdSecondPwd;//账号，新密码，确认密码
    private Button FindPwdSubmit;//提交按钮
    private HintDiaLog hintDiaLog;
    private ConnectivityManager manager;
    private  NetworkInfo.State wifi;
    private NetworkInfo.State gprs;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s= (String) msg.obj;
            try {
                JSONObject jsonObject=new JSONObject(s);
                if(jsonObject.get("result").equals("ok")){
                    Intent intent=new Intent(FindPwdActivity.this,LoginActivity.class);
                    startActivity(intent);
                    FindPwdActivity.this.finish();
                    Toast.makeText(FindPwdActivity.this,jsonObject.getString("error"),Toast.LENGTH_LONG).show();
                }else if(jsonObject.get("result").equals("failed")){
                    hintDiaLog.HintBox(FindPwdActivity.this,"提示",jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_findpwd);
        FindBydId();
        Style();
        Listener();

    }


    private void FindBydId() {
        FindPwdName=(TextView)findViewById(R.id.FindPwdName);//找回密码
        back=(TextView) findViewById(R.id.back);//返回
        FindPwdInput=(LinearLayout)findViewById(R.id.FindPwdInput);//input布局
        LayoutBack=(LinearLayout) findViewById(R.id.LayoutBack);
        FindPwdAccount=(EditText) findViewById(R.id.FindPwdAccount);//账号
        FindPwdNewPwd=(EditText) findViewById(R.id.FindPwdNewPwd);//新密码
        FindPwdSecondPwd=(EditText) findViewById(R.id.FindPwdSecondPwd);//确认密码
        FindPwdSubmit=(Button) findViewById(R.id.FindPwdSubmit);//提交按钮

        hintDiaLog=new HintDiaLog();

        //网络判断初始化
        manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        gprs=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
    }
    private void Style() {
        FindPwdName.getPaint().setFakeBoldText(true);
        back.getPaint().setFakeBoldText(true);
        FindPwdInput.getBackground().setAlpha(160);
        LayoutBack.getBackground().setAlpha(160);
        FindPwdSubmit.getBackground().setAlpha(160);

    }

    private void Listener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FindPwdActivity.this,LoginActivity.class);
                startActivity(intent);
                FindPwdActivity.this.finish();
            }
        });

        FindPwdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FindPwdAccountText=FindPwdAccount.getText().toString().trim();
                String FindPwdNewPwdText=FindPwdNewPwd.getText().toString().trim();
                String FindPwdSecondPwdText=FindPwdSecondPwd.getText().toString().trim();
                if(FindPwdAccountText.equals("")||FindPwdNewPwdText.equals("")||FindPwdSecondPwdText.equals("")||
                        FindPwdAccountText==null||FindPwdNewPwdText==null||FindPwdSecondPwdText==null){
                    hintDiaLog.HintBox(FindPwdActivity.this,"提示","请将修改信息填写完整");
                    return;
                }else if(!FindPwdAccountText.equals("")&&!FindPwdNewPwdText.equals("")&&!FindPwdSecondPwdText.equals("")||
                        FindPwdAccountText!=null||FindPwdNewPwdText!=null||FindPwdSecondPwdText!=null){
                    if(!wifi.equals(NetworkInfo.State.CONNECTED)&&!gprs.equals(NetworkInfo.State.CONNECTED)){
                        SystemNetWork systemNetWork=new SystemNetWork();
                        systemNetWork.HintBox(FindPwdActivity.this,"提示","无网络开启，是否进入系统设置");
                    }else{
                        if(FindPwdNewPwdText.equals(FindPwdSecondPwdText)){
                            String url = "http://"+ IP.IP+":8080/AndroidServer/findPwd";
                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put("UserName",FindPwdAccountText);
                                jsonObject.put("UserNewPwd",FindPwdNewPwdText);
                                HttpThread httpThread=new HttpThread(url,jsonObject.toString(),handler);
                                httpThread.start();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            hintDiaLog.HintBox(FindPwdActivity.this,"提示","两次密码输入不一致");
                            return;
                        }



                    }
                }
            }
        });
    }
}
