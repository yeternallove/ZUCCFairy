package com.eternallove.demo.zuccfairy.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eternallove.demo.zuccfairy.modle.UserBean;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/12
 */
public class FairyDB {
    public static final String DB_NAME ="Eternal";
    public static final int VERSION = 1;

    private static FairyDB mfairyDB;
    private SQLiteDatabase db;
    private Context mcontext;

    private FairyDB(Context context){
        FairyOpenHelper dbHelper = new FairyOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
        this.mcontext = context;
    }
    public synchronized static FairyDB getInstance(Context context){
        if(mfairyDB == null){
            mfairyDB = new FairyDB(context);
        }
        return mfairyDB;
    }

    public void saveUser(UserBean userBean){
        final String sql_s = "SELECT * FROM User WHERE user_id = ? ";//select
        final String sql_i = "INSERT INTO User(user_id,account,pwd,name,avatar,data) VALUES(?,?,?,?,?,?)";//insert
        Cursor c = db.rawQuery(sql_s, new String[]{userBean.getUser_id()});
        if (c.moveToFirst()){
            c.close();
            return;
        }
        db.execSQL(sql_i, new Object[]{userBean.getUser_id(),
                userBean.getAccount(), userBean.getPwd()
                , userBean.getAvatar(), userBean.getName(), userBean.getData()});
    }
    public boolean login(String account,String pwd){
        final String sql = "SELECT pwd FROM User WHERE account = ? ";
        Cursor c = db.rawQuery(sql, new String[]{account});
        if (c.moveToFirst()){
            String pwd2 = c.getString(0);
            if(pwd.equals(pwd2)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
    public UserBean queryUser(String user_id){
        UserBean userBean = new UserBean();
        final String sql = "SELECT user_id,account,pwd,name,avatar,data FROM User WHERE user_id = ? ";
        Cursor c = db.rawQuery(sql, new String[]{user_id});
        if (c.moveToFirst()){
            userBean.setUser_id(c.getString(0));
            userBean.setAccount(c.getString(1));
            userBean.setPwd(c.getString(2));
            userBean.setName(c.getString(3));
            userBean.setAvatar(c.getString(4));
            userBean.setData(c.getString(5));
        }
        c.close();
        return userBean;
    }
    public boolean updateUser(UserBean userBean){
        final String sql = "UPDATE User " +
                "SET user_id = ?, account = ?, pwd = ?, name = ?, avatar = ?, data = ? " +
                "WHERE user_id = ?; ";
        try{
            db.execSQL(sql,new Object[]{userBean.getUser_id(),
                    userBean.getAccount(), userBean.getPwd()
                    , userBean.getAvatar(), userBean.getName(),
                    userBean.getData(),userBean.getUser_id()});
        }catch (Error e){
            return false;
        }
        return true;
    }
}
