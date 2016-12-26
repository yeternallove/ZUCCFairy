package com.eternallove.demo.zuccfairy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eternallove.demo.zuccfairy.modle.AlarmBean;
import com.eternallove.demo.zuccfairy.modle.ReceivedBean;
import com.eternallove.demo.zuccfairy.modle.UserBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    /**
     * 保存用户到SQLite
     * @param userBean
     */
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

    /**
     * 登录判断
     * @param account
     * @param pwd
     * @return
     */
    public String login(String account,String pwd){
        final String sql = "SELECT user_id,pwd FROM User WHERE account = ? ";
        Cursor c = db.rawQuery(sql, new String[]{account});
        if (c.moveToFirst()){
            String user_id = c.getString(0);
            String pwd2 = c.getString(1);
            if(pwd.equals(pwd2)){
                return user_id;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    /**
     * 查询用户信息
      * @param user_id
     * @return
     */
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

    /**
     * 更新用户信息
     * @param userBean
     * @return
     */
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

    public void saveReceived(ReceivedBean receivedBean){
        if(receivedBean.getUser_id() == null)
            return;
        final String sql = "INSERT INTO Received(user_id,timestampe,message,picture) VALUES(?,?,?,?)";
        db.execSQL(sql, new Object[]{receivedBean.getUser_id(),
                receivedBean.getTimestampe(), receivedBean.getMessage(),receivedBean.getPicture()});
    }
    public ReceivedBean queryReceived(int id){
        ReceivedBean receivedBean = new ReceivedBean();
        final String sql = "SELECT user_id,timestampe,message,picture FROM Received WHERE id = ?";
        Cursor c = db.rawQuery(sql, new String[]{id+""});
        if (c.moveToFirst()){
            receivedBean.setId(id);
            receivedBean.setUser_id(c.getString(0));
            receivedBean.setTimestampe(c.getLong(1));
            receivedBean.setMessage(c.getString(2));
            receivedBean.setPicture(c.getString(3));
        }
        c.close();
        return receivedBean;
    }
    public List<ReceivedBean> loadReceivedAll(String User_id){
        List<ReceivedBean> list= new ArrayList<>();
        ReceivedBean receivedBean;
        final String sql = "SELECT id,timestampe,message,picture FROM Received WHERE user_id = ? ORDER BY timestampe DESC";
        Cursor c = db.rawQuery(sql, new String[]{User_id});
        while(c.moveToNext()){
            receivedBean = new ReceivedBean();
            receivedBean.setId(c.getInt(0));
            receivedBean.setUser_id(User_id);
            receivedBean.setTimestampe(c.getLong(1));
            receivedBean.setMessage(c.getString(2));
            receivedBean.setPicture(c.getString(3));
            list.add(receivedBean);
        }
        return list;
    }
    /**
     * 插入数据
     */
    public void saveAlarmDate(AlarmBean bean){
        final String sql = "INSERT INTO AlarmList ( user_id,title,isAllday ,isVibrate ,year ,month ,day ,startTimeHour ,startTimeMinute ,endTimeHour" +
                " ,endTimeMinute ,alarmTime ,alarmColor ,alarmTonePath ,local ,description,replay) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql,new Object[]{bean.getUser_id(),bean.getTitle(),bean.getIsAllday(),
                bean.getIsVibrate(),bean.getYear(),bean.getMonth(),bean.getDay(),bean.getStartTimeHour(),bean.getStartTimeMinute(),bean.getEndTimeHour(),
                bean.getEndTimeMinute(),bean.getAlarmTime(),bean.getAlarmColor(),bean.getAlarmTonePath(),bean.getLocal(),bean.getDescription(),bean.getReplay()});
    }

    /**
     * 查询全部
     */
    public List<AlarmBean> getAll(){
        List<AlarmBean> beanList = new ArrayList<AlarmBean>();
        Cursor cursor = getCursor();
        if (cursor.moveToFirst()) {
            do{
                AlarmBean bean = new AlarmBean();
                bean.setId(cursor.getInt(0));
                bean.setTitle(cursor.getString(1));
                bean.setIsAllday(cursor.getInt(2));
                bean.setIsVibrate(cursor.getInt(3));
                bean.setYear(cursor.getInt(4));
                bean.setMonth(cursor.getInt(5));
                bean.setDay(cursor.getInt(6));
                bean.setStartTimeHour(cursor.getInt(7));
                bean.setStartTimeMinute(cursor.getInt(8));
                bean.setEndTimeHour(cursor.getInt(9));
                bean.setEndTimeMinute(cursor.getInt(10));
                bean.setAlarmTime(cursor.getString(11));
                bean.setAlarmColor(cursor.getString(12));
                bean.setAlarmTonePath(cursor.getString(13));
                bean.setLocal(cursor.getString(14));
                bean.setDescription(cursor.getString(15));
                bean.setReplay(cursor.getString(16));

                beanList.add(bean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return beanList;
    }

    /**
     * 按照日期查找
     * @return
     */
    public List<Object> getDataByDay(Calendar calendar){
        List<Object> beanList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from alarmlist where year=? and month=? and day=?",
                new String[]{calendar.get(Calendar.YEAR)+"",calendar.get(Calendar.MONTH)+"",calendar.get(Calendar.DAY_OF_MONTH)+""});
        if(cursor.moveToFirst()){
            do{
                AlarmBean bean = new AlarmBean();
                bean.setId(cursor.getInt(0));
                bean.setTitle(cursor.getString(1));
                bean.setIsAllday(cursor.getInt(2));
                bean.setIsVibrate(cursor.getInt(3));
                bean.setYear(cursor.getInt(4));
                bean.setMonth(cursor.getInt(5));
                bean.setDay(cursor.getInt(6));
                bean.setStartTimeHour(cursor.getInt(7));
                bean.setStartTimeMinute(cursor.getInt(8));
                bean.setEndTimeHour(cursor.getInt(9));
                bean.setEndTimeMinute(cursor.getInt(10));
                bean.setAlarmTime(cursor.getString(11));
                bean.setAlarmColor(cursor.getString(12));
                bean.setAlarmTonePath(cursor.getString(13));
                bean.setLocal(cursor.getString(14));
                bean.setDescription(cursor.getString(15));
                bean.setReplay(cursor.getString(16));

                beanList.add(bean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return beanList;
    }

    /**
     * 按照id查找
     * @return
     */
    public AlarmBean getDataById(int id){
        Cursor cursor = db.rawQuery("select * from alarmlist where id=?", new String[]{id + ""});
        cursor.moveToNext();
        AlarmBean bean = new AlarmBean();
        bean.setId(cursor.getInt(0));
        bean.setTitle(cursor.getString(1));
        bean.setIsAllday(cursor.getInt(2));
        bean.setIsVibrate(cursor.getInt(3));
        bean.setYear(cursor.getInt(4));
        bean.setMonth(cursor.getInt(5));
        bean.setDay(cursor.getInt(6));
        bean.setStartTimeHour(cursor.getInt(7));
        bean.setStartTimeMinute(cursor.getInt(8));
        bean.setEndTimeHour(cursor.getInt(9));
        bean.setEndTimeMinute(cursor.getInt(10));
        bean.setAlarmTime(cursor.getString(11));
        bean.setAlarmColor(cursor.getString(12));
        bean.setAlarmTonePath(cursor.getString(13));
        bean.setLocal(cursor.getString(14));
        bean.setDescription(cursor.getString(15));
        bean.setReplay(cursor.getString(16));

        return bean;
    }

    /**
     * 删除指定数据
     */
    public void deleteDataById(int id) {
        db.delete("alarmlist", "id=?", new String[]{id+""});
    }

    public void updateDataById(int id,AlarmBean bean){
        ContentValues values=new ContentValues();
        values.put("title",bean.getTitle());
        values.put("isAllday",bean.getIsAllday());
        values.put("isVibrate",bean.getIsVibrate());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("startTimeHour",bean.getStartTimeHour());
        values.put("startTimeMinute",bean.getStartTimeMinute());
        values.put("endTimeHour",bean.getEndTimeHour());
        values.put("endTimeMinute",bean.getEndTimeMinute());
        values.put("alarmTime",bean.getAlarmTime());
        values.put("alarmColor",bean.getAlarmColor());
        values.put("alarmTonePath",bean.getAlarmTonePath());
        values.put("local",bean.getLocal());
        values.put("description",bean.getDescription());
        values.put("replay",bean.getReplay());
        db.update("AlarmList",values,"id=?",new String[]{id+""});
    }

    public Cursor getCursor() {
        // TODO Auto-generated method stub
        String[] columns = new String[] {
                "id",
                "user_id",
                "title",
                "isAllday",
                "isVibrate",
                "year",
                "month",
                "day",
                "startTimeHour",
                "startTimeMinute",
                "endTimeHour",
                "endTimeMinute",
                "alarmTime",
                "alarmColor",
                "alarmTonePath",
                "local",
                "description",
                "replay"
        };
        return db.query("AlarmList", columns, null, null, null, null,
                null);
    }
}
