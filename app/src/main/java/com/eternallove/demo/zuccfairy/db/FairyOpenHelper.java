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
            + "user_id text primary key,"
            + "account text,"
            + "pwd text,"
            + "name text,"
            + "avatar text,"
            + "data text)";
    public FairyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists User");
        onCreate(db);
    }
}
