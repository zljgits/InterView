package com.example.administrator.interview.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.interview.Bean.LoginInfoBean;
import com.example.administrator.interview.Bean.LoginInfoUtil;
import com.example.administrator.interview.Hint.HintDiaLog;
import com.example.administrator.interview.Hint.Wait;
import com.example.administrator.interview.Http.HttpThread;
import com.example.administrator.interview.Http.IP;
import com.example.administrator.interview.Http.State;
import com.example.administrator.interview.Http.SystemNetWork;
import com.example.administrator.interview.MainActivity;
import com.example.administrator.interview.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    /**
     * 声明控件
     */
    private TextView LoginIconName,FindPwd;//智能交通，找回密码
    private Button Login;//登录按钮
    private LinearLayout input;//包裹输入框的布局id
    private CheckBox RememberPwd;//记住密码
    private EditText Account,Pwd;//账号，密码
    private ConnectivityManager manager;
    private  NetworkInfo.State wifi;
    private NetworkInfo.State gprs;
    private LoginInfoBean loginInfoBean;
    private SharedPreferences sharedPreferences;
    private HintDiaLog hintDiaLog;

   private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           String s= (String) msg.obj;
           try {
               JSONObject jsonObject=new JSONObject(s);
               if (jsonObject.getString("result").equals("ok")) {
                   Toast.makeText(LoginActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                   Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                   loginInfoBean.setUserRole(jsonObject.getString("UserRole"));
                   //intent.putExtra("ShenFen",jsonObject.getString("UserRole"));
                   startActivity(intent);
                   LoginActivity.this.finish();
                   Wait.Close();
               } else if (jsonObject.getString("result").equals("failed")) {
                   hintDiaLog.HintBox(LoginActivity.this,"提示",jsonObject.getString("error"));
                   //Toast.makeText(LoginActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                   Wait.Close();
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
        setContentView(R.layout.activity_login);
        FindById();//初始化控件
        Style();//控件风格
        Listener();//事件监听


    }



    /**
     * 初始化控件
     */
    private void FindById() {
        LoginIconName=(TextView)findViewById(R.id.LoginIconName);//智能交通四个字
        Login=(Button)findViewById(R.id.Login);//登录按钮
        input=(LinearLayout)findViewById(R.id.input);//包裹输入框的布局id
        RememberPwd=(CheckBox)findViewById(R.id.RememberPwd);//记住密码
        FindPwd=(TextView)findViewById(R.id.FindPwd);//找回密码
        Account=(EditText) findViewById(R.id.Account);
        Pwd=(EditText)findViewById(R.id.Pwd);
        Account.setSelection(Account.length());
        Pwd.setSelection(Pwd.length());
        //网络判断初始化
        manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        gprs=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        hintDiaLog=new HintDiaLog();

        loginInfoBean=new LoginInfoBean();
        loginInfoBean= LoginInfoUtil.loading(LoginActivity.this);

        sharedPreferences=getSharedPreferences("SharedPreferences",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("RememberPwd",true)){
            RememberPwd.setChecked(true);
            Account.setText(loginInfoBean.getUsername());
            Pwd.setText(loginInfoBean.getPassword());
        }else {
            RememberPwd.setChecked(false);
            Account.setText("");
            Pwd.setText("");
        }
    }

    /**
     * 控件风格
     */
    private void Style() {
        LoginIconName.getPaint().setFakeBoldText(true);//智能交通四个字字体加粗
        Login.getBackground().setAlpha(160);//登录按钮设置背景透明度
        input.getBackground().setAlpha(160);//包裹输入框的布局id设置背景透明度
        RememberPwd.getPaint().setFakeBoldText(true);//记住密码字体加粗
        FindPwd.getPaint().setFakeBoldText(true);//找回密码字体加粗
    }

    /**
     * 事件监听
     */
    private void Listener() {
        RememberPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(RememberPwd.isChecked()){
                    sharedPreferences.edit().putBoolean("RememberPwd",true).commit();
                }else{
                    sharedPreferences.edit().putBoolean("RememberPwd",false).commit();
                }
            }
        });

        /**
         * 找回密码
         */
        FindPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,FindPwdActivity.class));
            }
        });

        /**
         * 登录
         */
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AccountText=Account.getText().toString().trim();
                String PwdText=Pwd.getText().toString().trim();
              //  HintDiaLog hintDiaLog=new HintDiaLog();
                if(AccountText.equals("")||PwdText.equals("")||AccountText==null||PwdText==null){
                    hintDiaLog.HintBox(LoginActivity.this,"提示","请将登录信息填写完整");
                    return;
                }else if(!AccountText.equals("")&&!PwdText.equals("")&&AccountText!=null&&PwdText!=null){
                    if(!wifi.equals(NetworkInfo.State.CONNECTED)&&!gprs.equals(NetworkInfo.State.CONNECTED)){
                        SystemNetWork  systemNetWork=new SystemNetWork();
                        systemNetWork.HintBox(LoginActivity.this,"提示","无网络开启，是否进入系统设置");
                    }else {
                        if(RememberPwd.isChecked()){
                            LoginInfoUtil.save(LoginActivity.this,AccountText,PwdText);
                        }
                        String url = "http://"+ IP.IP+":8080/AndroidServer/login";
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("UserName",AccountText);
                            jsonObject.put("UserPwd",PwdText);
                            HttpThread httpThread=new HttpThread(url,jsonObject.toString(),handler);
                            httpThread.start();
                            Wait.OPEN(LoginActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

    }



}
