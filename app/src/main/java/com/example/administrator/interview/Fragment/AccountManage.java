package com.example.administrator.interview.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.interview.DB.DBHelper;
import com.example.administrator.interview.Hint.HintDiaLog;
import com.example.administrator.interview.Http.HttpThread;
import com.example.administrator.interview.Http.IP;
import com.example.administrator.interview.MainActivity;
import com.example.administrator.interview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AccountManage extends Fragment {
    private TextView infoshow, headnumber, headbiao, headbalance,topshow;
    private ArrayList<AccountManageBean> arrayList;
    private int count = 1;
    private AccountManageAdapter adapter;
    private ListView AccountListView;

    private ArrayAdapter arrayAdapter;
    private Spinner AccountSpinner;
    private int CarId = 1;
    private Button AccountTopBtn;
    private EditText AccountInputMoney;
    private int TopMoney;

    private  HintDiaLog hintDiaLog;


    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s= (String) msg.obj;
            try {
                JSONObject jsonObject=new JSONObject(s);
                AccountManageBean bean = arrayList.get(count - 1);
                bean.carnumber = count;
                bean.carbalance = jsonObject.getInt("Balance");
                count++;
                if (count == 5) {
                    count = 1;
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            StartThread();
            handler1.postDelayed(runnable, 3000);
        }
    };
    private Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s= (String) msg.obj;
            try {
                JSONObject jsonObject=new JSONObject(s);
                if(jsonObject.get("result").equals("ok")){
                    DBHelper dbHelper=new DBHelper(getActivity());
                    dbHelper.InsertCarInfo(CarId,TopMoney);
                    hintDiaLog.HintBox(getActivity(),"提示",jsonObject.getString("error"));
                }else if(jsonObject.get("result").equals("failed")){
                    hintDiaLog.HintBox(getActivity(),"提示",jsonObject.getString("error"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accountmanage, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hintDiaLog=new HintDiaLog();
        infoshow = (TextView) getView().findViewById(R.id.infoshow);
        infoshow.getPaint().setFakeBoldText(true);
        headnumber = (TextView) getView().findViewById(R.id.headnumber);
        headnumber.getPaint().setFakeBoldText(true);
        headbiao = (TextView) getView().findViewById(R.id.headbiao);
        headbiao.getPaint().setFakeBoldText(true);
        headbalance = (TextView) getView().findViewById(R.id.headbalance);
        headbalance.getPaint().setFakeBoldText(true);
        AccountListView= (ListView) getView().findViewById(R.id.AccountListView);
        topshow= (TextView) getView().findViewById(R.id.topshow);
        topshow.getPaint().setFakeBoldText(true);

        arrayList = new ArrayList<>();
        adapter = new AccountManageAdapter();
        AccountManageBean bean1 = new AccountManageBean();
        arrayList.add(bean1);

        AccountManageBean bean2 = new AccountManageBean();
        arrayList.add(bean2);

        AccountManageBean bean3 = new AccountManageBean();
        arrayList.add(bean3);

        AccountManageBean bean4 = new AccountManageBean();
        arrayList.add(bean4);
        AccountListView.setAdapter(adapter);
        runnable.run();

        AccountSpinner= (Spinner) getView().findViewById(R.id.AccountSpinner);
        String CarNumber[] = {"1", "2", "3", "4"};
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, CarNumber);
        AccountSpinner.setAdapter(arrayAdapter);
        AccountTopBtn= (Button) getView().findViewById(R.id.AccountTopBtn);
        AccountInputMoney= (EditText) getView().findViewById(R.id.AccountInputMoney);

        AccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CarId = 1;
                CarId = CarId + i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AccountTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String AccountInputMoneyText=AccountInputMoney.getText().toString().trim();

                if(AccountInputMoneyText.equals("")||AccountInputMoneyText==null){
                    hintDiaLog.HintBox(getActivity(),"提示","充值金额不得为空");
                }else{
                    TopMoney=Integer.parseInt(AccountInputMoneyText);
                    try {
                    if(TopMoney>0){

                        String url="http://"+IP.IP+":8080/AndroidServer/insertCarBalance";
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("CarId",CarId);
                        jsonObject.put("Money",TopMoney);
                        HttpThread httpThread=new HttpThread(url,jsonObject.toString(),handler2);
                        httpThread.start();

                    }else {
                        hintDiaLog.HintBox(getActivity(),"提示","充值金额必须大于0");
                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class AccountManageBean {
        int carnumber;
        int carbiao[] = {R.drawable.bm, R.drawable.bc, R.drawable.mzd, R.drawable.qr};
        int carbalance;
    }

    public static class AccountManageHolder {
        TextView carnumber;
        ImageView carbiao;
        TextView carbalance;
    }

    public class AccountManageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            AccountManageHolder holder;
            if (view == null) {
                holder = new AccountManageHolder();
                view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_accountbean, null);
                holder.carnumber = (TextView) view.findViewById(R.id.carnumber);
                holder.carbiao = (ImageView) view.findViewById(R.id.carbiao);
                holder.carbalance = (TextView) view.findViewById(R.id.carbalance);
                view.setTag(holder);
            } else {
                holder = (AccountManageHolder) view.getTag();
            }
            AccountManageBean bean = arrayList.get(i);
            holder.carnumber.setText("" + bean.carnumber);
            holder.carbiao.setImageResource(bean.carbiao[i]);
            holder.carbalance.setText("" + bean.carbalance + "元");
            return view;
        }
    }

    /**
     * 查询
     */
    private void StartThread() {
        String url = "http://" + IP.IP + ":8080/AndroidServer/findCarBalance";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CarId",count);
            HttpThread httpThread = new HttpThread(url,jsonObject.toString(), handler1);
            httpThread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler1.removeCallbacks(runnable);
    }
}
