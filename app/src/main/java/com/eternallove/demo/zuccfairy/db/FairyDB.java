package com.eternallove.demo.zuccfairy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eternallove.demo.zuccfairy.modle.AlarmBean;
import com.eternallove.demo.zuccfairy.modle.CardBean;
import com.eternallove.demo.zuccfairy.modle.ChatMessageBean;
import com.eternallove.demo.zuccfairy.modle.ReplyBean;
import com.eternallove.demo.zuccfairy.modle.UserBean;
import com.eternallove.demo.zuccfairy.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/12
 */
public class FairyDB {
    public static final String DB_NAME = "Eternal";
    public static final int VERSION = 3;

    private static FairyDB mfairyDB;
    private SQLiteDatabase db;
    private Context mContext;

    private FairyDB(Context context) {
        this.mContext = context;
        FairyOpenHelper dbHelper = new FairyOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static FairyDB getInstance(Context context) {
        if (mfairyDB == null) {
            mfairyDB = new FairyDB(context);
        }
        return mfairyDB;
    }

    /**
     * 保存用户到SQLite
     *
     * @param userBean
     */
    public void saveUser(UserBean userBean) {
        final String sql_s = "SELECT * FROM User WHERE user_id = ? ";//select
        final String sql_i = "INSERT INTO User(user_id,account,pwd,name,avatar,data) VALUES(?,?,?,?,?,?)";//insert
        Cursor c = db.rawQuery(sql_s, new String[]{userBean.getUser_id()});
        if (c.moveToFirst()) {
            c.close();
            return;
        }
        db.execSQL(sql_i, new Object[]{userBean.getUser_id(),
                userBean.getAccount(), userBean.getPwd()
                , userBean.getAvatar(), userBean.getName(), userBean.getData()});
    }

    /**
     * 登录判断
     *
     * @param account
     * @param pwd
     * @return
     */
    public String login(String account, String pwd) {
        final String sql = "SELECT user_id,pwd FROM User WHERE account = ? ";
        Cursor c = db.rawQuery(sql, new String[]{account});
        if (c.moveToFirst()) {
            String user_id = c.getString(0);
            String pwd2 = c.getString(1);
            if (pwd.equals(pwd2)) {
                return user_id;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 查询用户信息
     *
     * @param user_id
     * @return
     */
    public UserBean queryUser(String user_id) {
        UserBean userBean = new UserBean();
        final String sql = "SELECT user_id,account,pwd,name,avatar,data FROM User WHERE user_id = ? ";
        Cursor c = db.rawQuery(sql, new String[]{user_id});
        if (c.moveToFirst()) {
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
     *
     * @param userBean
     * @return
     */
    public boolean updateUser(UserBean userBean) {
        final String sql = "UPDATE User " +
                "SET user_id = ?, account = ?, pwd = ?, name = ?, avatar = ?, data = ? " +
                "WHERE user_id = ?; ";
        try {
            db.execSQL(sql, new Object[]{userBean.getUser_id(),
                    userBean.getAccount(), userBean.getPwd()
                    , userBean.getAvatar(), userBean.getName(),
                    userBean.getData(), userBean.getUser_id()});
        } catch (Error e) {
            return false;
        }
        return true;
    }

    /**
     * @param chatMessageBean
     */
    public void saveChat(ChatMessageBean chatMessageBean) {
        if (chatMessageBean.getSender_id() == null || chatMessageBean.getRecipient_id() == null)
            return;
        final String sql = "INSERT INTO Chat(sender_id,recipient_id,timestampe,message,picture) VALUES(?,?,?,?,?)";
        db.execSQL(sql, new Object[]{chatMessageBean.getSender_id(), chatMessageBean.getRecipient_id(),
                chatMessageBean.getTimestampe(), chatMessageBean.getMessage(), chatMessageBean.getPicture()});
    }

    public ChatMessageBean queryChat(int id) {
        ChatMessageBean chatMessageBean = new ChatMessageBean();
        final String sql = "SELECT sender_id,recipient_id,timestampe,message,picture FROM Chat WHERE id = ?";
        Cursor c = db.rawQuery(sql, new String[]{id + ""});
        if (c.moveToFirst()) {
            chatMessageBean.setId(id);
            chatMessageBean.setSender_id(c.getString(0));
            chatMessageBean.setRecipient_id(c.getString(1));
            chatMessageBean.setTimestampe(c.getLong(2));
            chatMessageBean.setMessage(c.getString(3));
            chatMessageBean.setPicture(c.getString(4));
        }
        c.close();
        return chatMessageBean;
    }

    /**
     * @param recipient_id
     * @return
     */
    public List<ChatMessageBean> loadChatAll(String recipient_id) {
        List<ChatMessageBean> list = new ArrayList<>();
        ChatMessageBean chatMessageBean;
        final String sql = "SELECT id,sender_id,timestampe,message,picture FROM Chat WHERE recipient_id = ? ORDER BY timestampe DESC";
        Cursor c = db.rawQuery(sql, new String[]{recipient_id});
        while (c.moveToNext()) {
            chatMessageBean = new ChatMessageBean();
            chatMessageBean.setId(c.getInt(0));
            chatMessageBean.setSender_id(c.getString(1));
            chatMessageBean.setRecipient_id(recipient_id);
            chatMessageBean.setTimestampe(c.getLong(2));
            chatMessageBean.setMessage(c.getString(3));
            chatMessageBean.setPicture(c.getString(4));
            list.add(chatMessageBean);
        }
        return list;
    }

    public boolean deleteChat(int id) {
        final String sql_s = "SELECT * FROM Chat WHERE id = ?";
        final String sql_d = "DELETE FROM Chat WHERE id = ?";
        Cursor c = db.rawQuery(sql_s, new String[]{id + ""});
        if (!c.moveToFirst()) {
            return false;
        }
        c.close();
        db.execSQL(sql_d, new Object[]{id});
        return true;
    }

    /**
     * @param cardBean
     */
    public void saveCard(CardBean cardBean) {
        final String sql_s = "SELECT * FROM Card WHERE chat_id = ?";
        final String sql_i = "INSERT INTO Card (user_id,chat_id,timestampe,num,days,percentage) VALUES(?,?,?,?,?,?)";
        Cursor c = db.rawQuery(sql_s, new String[]{cardBean.getChat_id() + ""});
        if (c.moveToFirst()) {
            c.close();
            return;
        }
        c.close();
        db.execSQL(sql_i,new Object[]{cardBean.getUser_id(),cardBean.getChat_id(),cardBean.getTimestampe(),
                cardBean.getNum(),cardBean.getDays(),cardBean.getPercentage()});
    }
    public CardBean queryCard(int chat_id){
        CardBean cardBean = new CardBean();
        final String sql = "SELECT id,user_id,chat_id,timestampe,num,days,percentage FROM Card WHERE chat_id = ?";
        Cursor c = db.rawQuery(sql,new String[]{chat_id+""});
        if(c.moveToFirst()){
            cardBean.setId(c.getInt(0));
            cardBean.setUser_id(c.getString(1));
            cardBean.setChat_id(c.getInt(2));
            cardBean.setTimestampe(c.getLong(3));
            cardBean.setNum(c.getInt(4));
            cardBean.setDays(c.getInt(5));
            cardBean.setPercentage(c.getInt(6));
        }
        return cardBean;
    }
    public boolean ispushCard(){
        long start = DateUtil.getStartTime();
        long end = DateUtil.getEndTime();
        final String sql = "SELECT * FROM Card WHERE timestampe > ? AND timestampe < ?";
        Cursor c = db.rawQuery(sql,new String[]{start+"",end+""});
        if(c.moveToFirst()){
            return true;
        }
        return false;
    }
//    public void saveReply(ReplyBean replyBean){
//        final String sql_s = "SELECT * FROM Reply WHERE key = ?";
//        final String sql_i = "INSERT INTO Reply (key,content) VALUES(?,?)";
//        Cursor c = db.rawQuery(sql_s,new String[]{replyBean.getKey()});
//        if(c.moveToFirst()){
//            c.close();
//            return;
//         }
//        c.close();
//        db.execSQL(sql_i,new Object[]{replyBean.getKey(),replyBean.getContent()});
//    }
//    public Map<String,String> loadReply(){
//        Map<String,String> replymap = new HashMap<>();
//        String key;
//        String content;
//        final String sql = "SELECT key,content FROM Reply";
//        Cursor c = db.rawQuery(sql,null);
//        while (c.moveToNext()){
//            key = c.getString(0);
//            content = c.getString(1);
//            replymap.put(key,content);
//        }
//        return replymap;
//    }
    /**
     * 插入数据
     */
    public void saveAlarmDate(AlarmBean bean) {
        final String sql = "INSERT INTO AlarmList ( user_id,title,isAllday ,isVibrate ,year ,month ,day ,startTimeHour ,startTimeMinute ,endTimeHour" +
                " ,endTimeMinute ,alarmTime ,alarmColor ,alarmTonePath ,local ,description,replay) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{bean.getUser_id(), bean.getTitle(), bean.getIsAllday(),
                bean.getIsVibrate(), bean.getYear(), bean.getMonth(), bean.getDay(), bean.getStartTimeHour(), bean.getStartTimeMinute(), bean.getEndTimeHour(),
                bean.getEndTimeMinute(), bean.getAlarmTime(), bean.getAlarmColor(), bean.getAlarmTonePath(), bean.getLocal(), bean.getDescription(), bean.getReplay()});
    }

    /**
     * 查询全部
     */
    public List<AlarmBean> getAll() {
        List<AlarmBean> beanList = new ArrayList<AlarmBean>();
        Cursor cursor = getCursor();
        if (cursor.moveToFirst()) {
            do {
                AlarmBean bean = new AlarmBean();
                bean.setId(cursor.getInt(0));
                bean.setReplay(cursor.getString(1));
                bean.setTitle(cursor.getString(2));
                bean.setIsAllday(cursor.getInt(3));
                bean.setIsVibrate(cursor.getInt(4));
                bean.setYear(cursor.getInt(5));
                bean.setMonth(cursor.getInt(6));
                bean.setDay(cursor.getInt(7));
                bean.setStartTimeHour(cursor.getInt(8));
                bean.setStartTimeMinute(cursor.getInt(9));
                bean.setEndTimeHour(cursor.getInt(10));
                bean.setEndTimeMinute(cursor.getInt(11));
                bean.setAlarmTime(cursor.getString(12));
                bean.setAlarmColor(cursor.getString(13));
                bean.setAlarmTonePath(cursor.getString(14));
                bean.setLocal(cursor.getString(15));
                bean.setDescription(cursor.getString(16));


                beanList.add(bean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return beanList;
    }

    /**
     * 按照日期查找
     *
     * @return
     */
    public List<Object> getDataByDay(Calendar calendar) {
        List<Object> beanList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from alarmlist where year=? and month=? and day=?",
                new String[]{calendar.get(Calendar.YEAR) + "", calendar.get(Calendar.MONTH) + "", calendar.get(Calendar.DAY_OF_MONTH) + ""});
        if (cursor.moveToFirst()) {
            do {
                AlarmBean bean = new AlarmBean();
                bean.setId(cursor.getInt(0));
                bean.setReplay(cursor.getString(1));
                bean.setTitle(cursor.getString(2));
                bean.setIsAllday(cursor.getInt(3));
                bean.setIsVibrate(cursor.getInt(4));
                bean.setYear(cursor.getInt(5));
                bean.setMonth(cursor.getInt(6));
                bean.setDay(cursor.getInt(7));
                bean.setStartTimeHour(cursor.getInt(8));
                bean.setStartTimeMinute(cursor.getInt(9));
                bean.setEndTimeHour(cursor.getInt(10));
                bean.setEndTimeMinute(cursor.getInt(11));
                bean.setAlarmTime(cursor.getString(12));
                bean.setAlarmColor(cursor.getString(13));
                bean.setAlarmTonePath(cursor.getString(14));
                bean.setLocal(cursor.getString(15));
                bean.setDescription(cursor.getString(16));

                beanList.add(bean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return beanList;
    }

    /**
     * 按照id查找
     *
     * @return
     */
    public AlarmBean getDataById(int id) {
        Cursor cursor = db.rawQuery("select * from alarmlist where id=?", new String[]{id + ""});
        cursor.moveToNext();
        AlarmBean bean = new AlarmBean();
        bean.setId(cursor.getInt(0));
        bean.setReplay(cursor.getString(1));
        bean.setTitle(cursor.getString(2));
        bean.setIsAllday(cursor.getInt(3));
        bean.setIsVibrate(cursor.getInt(4));
        bean.setYear(cursor.getInt(5));
        bean.setMonth(cursor.getInt(6));
        bean.setDay(cursor.getInt(7));
        bean.setStartTimeHour(cursor.getInt(8));
        bean.setStartTimeMinute(cursor.getInt(9));
        bean.setEndTimeHour(cursor.getInt(10));
        bean.setEndTimeMinute(cursor.getInt(11));
        bean.setAlarmTime(cursor.getString(12));
        bean.setAlarmColor(cursor.getString(13));
        bean.setAlarmTonePath(cursor.getString(14));
        bean.setLocal(cursor.getString(15));
        bean.setDescription(cursor.getString(16));

        return bean;
    }

    /**
     * 删除指定数据
     */
    public void deleteDataById(int id) {
        db.delete("alarmlist", "id=?", new String[]{id + ""});
    }

    public void updateDataById(int id, AlarmBean bean) {
        ContentValues values = new ContentValues();
        values.put("title", bean.getTitle());
        values.put("isAllday", bean.getIsAllday());
        values.put("isVibrate", bean.getIsVibrate());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("startTimeHour", bean.getStartTimeHour());
        values.put("startTimeMinute", bean.getStartTimeMinute());
        values.put("endTimeHour", bean.getEndTimeHour());
        values.put("endTimeMinute", bean.getEndTimeMinute());
        values.put("alarmTime", bean.getAlarmTime());
        values.put("alarmColor", bean.getAlarmColor());
        values.put("alarmTonePath", bean.getAlarmTonePath());
        values.put("local", bean.getLocal());
        values.put("description", bean.getDescription());
        values.put("replay", bean.getReplay());
        values.put("user_id", bean.getUser_id());
        db.update("AlarmList", values, "id=?", new String[]{id + ""});
    }

    public Cursor getCursor() {
        // TODO Auto-generated method stub
        String[] columns = new String[]{
                "id",
                "replay",
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
                "user_id"
        };
        return db.query("AlarmList", columns, null, null, null, null,
                null);
    }
}
