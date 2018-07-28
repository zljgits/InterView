package com.example.administrator.interview;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.interview.Fragment.AccountManage;
import com.example.administrator.interview.Fragment.Drawing;
import com.example.administrator.interview.Fragment.FindTopRecord;
import com.example.administrator.interview.Fragment.PlatformFind;
import com.example.administrator.interview.Fragment.Welcome;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends FragmentActivity {
    private SlidingPaneLayout MySlidingPaneLayout;
    private ImageView icon;
    private ListView Main_listView;
    private TextView title;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, Object>> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindById();
        Listener();
    }


    private void FindById() {
        MySlidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.MySlidingPaneLayout);
        icon = (ImageView) findViewById(R.id.icon);
        Main_listView = (ListView) findViewById(R.id.Main_listView);
        title = (TextView) findViewById(R.id.title);
        final String iconName[] = {"账户充值", "账单查看", "站台查询", "实时环境"};
        final int iconImage[] = {R.drawable.account, R.drawable.accountform
               , R.drawable.carfind, R.drawable.hj};
        arrayList = new ArrayList<>();
        for (int i = 0; i < iconImage.length; i++) {
            HashMap hashMap = new HashMap();
            hashMap.put("iconImage", iconImage[i]);
            hashMap.put("iconName", iconName[i]);
            arrayList.add(hashMap);
        }
        String name[] = {"iconImage", "iconName"};
        int image[] = {R.id.iconImage, R.id.iconName};
        simpleAdapter = new SimpleAdapter(MainActivity.this, arrayList, R.layout.main_listview, name, image);
        Main_listView.setAdapter(simpleAdapter);

    }

    private void Listener() {
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentMain, new Welcome()).commit();

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySlidingPaneLayout.isOpen()) {
                    MySlidingPaneLayout.closePane();
                } else {
                    MySlidingPaneLayout.openPane();
                }
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySlidingPaneLayout.closePane();
            }
        });

        Main_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.in, R.anim.out);
                        fragmentTransaction.replace(R.id.FragmentMain, new AccountManage()).commit();
                        MySlidingPaneLayout.closePane();
                        title.setText("账户充值");
                        break;

                    }

                    case 1: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.in, R.anim.out);
                        fragmentTransaction.replace(R.id.FragmentMain, new FindTopRecord()).commit();
                        MySlidingPaneLayout.closePane();
                        title.setText("账单查看");
                        break;

                    }

                    case 2: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.in, R.anim.out);
                        fragmentTransaction.replace(R.id.FragmentMain, new PlatformFind()).commit();
                        MySlidingPaneLayout.closePane();
                        title.setText("账单查看");
                        break;

                    }

                    case 3: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.in, R.anim.out);
                        fragmentTransaction.replace(R.id.FragmentMain, new Drawing()).commit();
                        MySlidingPaneLayout.closePane();
                        title.setText("账单查看");
                        break;

                    }


                }
            }
        });

    }
}
