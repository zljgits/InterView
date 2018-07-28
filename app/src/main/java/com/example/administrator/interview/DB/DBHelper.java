package com.example.administrator.interview.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/5/8.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "sjk.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String carInfo="create table if not exists carInfo(_id integer primary key autoincrement not null," +
                "carId integer," +
                "topMoney integer," +
                "time data"+")";
        sqLiteDatabase.execSQL(carInfo);
    }

    public void InsertCarInfo(int carId,int topMoney){
        ContentValues values=new ContentValues();
        values.put("carId",carId);
        values.put("topMoney",topMoney);
        long time=System.currentTimeMillis();
        values.put("time",time);
        getWritableDatabase().insert("carInfo","_id",values);
        getWritableDatabase().close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
