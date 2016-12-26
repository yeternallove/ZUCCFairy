package com.eternallove.demo.zuccfairy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/12
 */
public class FairyOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_USER = "create table User("
            + "user_id char(20) primary key,"
            + "account char(20),"
            + "pwd char(20),"
            + "name char(20),"
            + "avatar text,"
            + "data text)";

    public static  final String CREATE_ALARM = "create table AlarmList(id integer primary key autoincrement,"
            + "user_id char(20),"
            + "title char(20),"
            + "isAllday int(20),"
            + "isVibrate int(20),"
            + "year int(20),"
            + "month int(20),"
            + "day int(20),"
            + "startTimeHour int(20),"
            + "startTimeMinute int(20),"
            + "endTimeHour int(20),"
            + "endTimeMinute int(20),"
            + "alarmTime char(20),"
            + "alarmColor char(20),"
            + "alarmTonePath char(20),"
            + "local char(20),"
            + "description char(100),"
            + "replay char(20))";

    public static final String CREATE_RECEIVED = "create table Received(id integer primary key autoincrement," +
            "user_id char(20)," +
            "timestampe long ," +
            "message text ," +
            "picture text )";
    public FairyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_ALARM);
        db.execSQL(CREATE_RECEIVED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists AlarmList");
        db.execSQL("drop table if exists Received");
        onCreate(db);
    }
}
