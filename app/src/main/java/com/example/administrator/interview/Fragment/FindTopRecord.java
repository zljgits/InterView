package com.example.administrator.interview.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.interview.DB.DBHelper;
import com.example.administrator.interview.Http.HttpThread;
import com.example.administrator.interview.Http.IP;
import com.example.administrator.interview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/7.
 */

public class FindTopRecord extends Fragment {
    private TextView hid, hcarid, hbalance, htime;
    private ListView FindTopRecordListView;
    private ArrayList<FindTopRecordBean> arrayList;
    private FindTopRecordAdapter adapter;
    private SQLiteDatabase sqLiteDatabase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findtoprecord, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hid = (TextView) getView().findViewById(R.id.hid);
        hcarid = (TextView) getView().findViewById(R.id.hcarid);
        hbalance = (TextView) getView().findViewById(R.id.hbalance);
        htime = (TextView) getView().findViewById(R.id.htime);
        hid.getPaint().setFakeBoldText(true);
        hcarid.getPaint().setFakeBoldText(true);
        hbalance.getPaint().setFakeBoldText(true);
        htime.getPaint().setFakeBoldText(true);
        FindTopRecordListView = (ListView) getView().findViewById(R.id.FindTopRecordListView);
        arrayList = new ArrayList<>();
        adapter = new FindTopRecordAdapter();
        DBHelper dbHelper=new DBHelper(getActivity());
        sqLiteDatabase=dbHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from carInfo where time order by time asc ",null);
        while (cursor.moveToNext()){
            FindTopRecordBean bean=new FindTopRecordBean();
            bean.rebid=cursor.getInt(cursor.getColumnIndex("_id"));
            bean.rebcarid=cursor.getInt(cursor.getColumnIndex("carId"));
            bean.rebbalance=cursor.getInt(cursor.getColumnIndex("topMoney"));
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long t=cursor.getLong(cursor.getColumnIndex("time"));
            Date date=new Date(t);
            bean.rebtime=simpleDateFormat.format(date);
            arrayList.add(bean);
            FindTopRecordListView.setAdapter(adapter);
            sqLiteDatabase.close();
        }






    }

    private static class FindTopRecordHolder {
        TextView rebid, rebcarid, rebbalance, rebtime;
    }

    private class FindTopRecordBean {
        int rebid, rebcarid, rebbalance;
        String rebtime;
    }

    private class FindTopRecordAdapter extends BaseAdapter {

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
            FindTopRecordHolder holder;
            if (view == null) {
                holder = new FindTopRecordHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_findtoprecordbean, null);
                holder.rebid = (TextView) view.findViewById(R.id.rebid);
                holder.rebcarid = (TextView) view.findViewById(R.id.rebcarid);
                holder.rebbalance = (TextView) view.findViewById(R.id.rebbalance);
                holder.rebtime = (TextView) view.findViewById(R.id.rebtime);
                view.setTag(holder);
            } else {
                holder = (FindTopRecordHolder) view.getTag();
            }
            FindTopRecordBean bean = arrayList.get(i);

            holder.rebid.setText("" + bean.rebid);
            holder.rebcarid.setText("" + bean.rebcarid);
            holder.rebbalance.setText("" + bean.rebbalance);
            holder.rebtime.setText("" + bean.rebtime);

            return view;
        }
    }

}
