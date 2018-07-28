package com.example.administrator.interview.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.interview.Http.HttpThread;
import com.example.administrator.interview.Http.IP;
import com.example.administrator.interview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/7.
 */

public class PlatformFind extends Fragment {
    private ExpandableListView expandableListView;
    private List<String> groupDate;
    private List<List<String>> childDate1;
    private List<List<String>> childDate2;
    private PlatformFindAdapter adapter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String s = (String) msg.obj;
            try {
                JSONObject jsonObject = new JSONObject(s);
                List<String> list = new ArrayList<>();
                list.clear();
               // Toast.makeText(getActivity(), "aa" + jsonObject.getInt("DistanceOne"), Toast.LENGTH_LONG).show();
                list.add("1号小车距离站台" + jsonObject.getInt("DistanceOne") + "米");
                list.add("2号小车距离站台" + jsonObject.getInt("DistanceTwo") + "米");
                childDate2.add(list);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable, 3000);
            StartThread();

        }
    };

    private void StartThread() {

        new Thread() {
            @Override
            public void run() {
                String URL = "http://" + IP.IP + ":8080/AndroidServer/platformFind";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("CarOne", 1);
                    jsonObject.put("CarTwo", 2);
                    HttpThread http = new HttpThread(URL, jsonObject.toString(), handler);
                    http.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_platformfind, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expandableListView = (ExpandableListView) getView().findViewById(R.id.ExpandableListView);
        groupDate = new ArrayList<>();
        groupDate.add("一号站台");
        groupDate.add("二号站台");
        childDate1 = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        list1.add("1号小车");
        list1.add("2号小车");
        childDate1.add(list1);
        List<String> list2 = new ArrayList<>();
        list2.add("1号小车");
        list2.add("2号小车");
        childDate1.add(list2);
        childDate2 = new ArrayList<>();
        adapter = new PlatformFindAdapter();
        expandableListView.setAdapter(adapter);
        runnable.run();

    }

    public class PlatformFindAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return groupDate.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return childDate1.size();
        }

        @Override
        public Object getGroup(int i) {
            return groupDate.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return childDate1.get(i).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i1) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.pratformfind_title, null);
            TextView pratform_title = (TextView) view.findViewById(R.id.pratform_title);
            pratform_title.setText(groupDate.get(i));
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.pratform_child, null);
            ImageView pratform_image = (ImageView) view.findViewById(R.id.pratform_image);
            TextView pratform_child = (TextView) view.findViewById(R.id.pratform_child);
            pratform_image.setImageResource(R.drawable.icon_busstop);
            if (childDate2.size() == 0 || childDate2.size() == 1) {
                pratform_child.setText(childDate1.get(i).get(i1));
            } else {
                pratform_child.setText(childDate2.get(i).get(i1));
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
