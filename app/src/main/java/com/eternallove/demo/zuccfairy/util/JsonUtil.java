package com.eternallove.demo.zuccfairy.util;

import android.content.Context;
import android.util.Log;

import com.eternallove.demo.zuccfairy.db.FairyDB;
import com.eternallove.demo.zuccfairy.modle.ReplyBean;
import com.eternallove.demo.zuccfairy.modle.UserBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/4
 */
public class JsonUtil {
    private String mjsonStr;
    private Context mcontext;
    private FairyDB mfairyDB;
    public JsonUtil(Context context, String jsonStr){
        this.mjsonStr = jsonStr;
        this.mcontext = context;
    }
    public void refreshSQLite(){
        if(this.mjsonStr == null) return;
        this.mfairyDB = FairyDB.getInstance(mcontext);
        try {
            JSONObject jsonObj = new JSONObject(this.mjsonStr);
            if(jsonObj.has("user")) {
                JSONArray user = jsonObj.getJSONArray("user");
                String user_id;
                String account;
                String pwd;
                String name;
                String avatar;
                String data;
                UserBean userBean;
                for (int i = 0; i < user.length(); i++) {
                    JSONObject c = user.getJSONObject(i);
                    user_id = c.getString("user_id");
                    account = c.getString("account");
                    pwd = c.getString("pwd");
                    name = c.getString("name");
                    if(c.has("avatar")){
                        avatar = c.getString("avatar");
                    }else{
                        avatar = null;
                    }
                    if(c.has("data")) {
                        data = c.getString("data");
                    }else {
                        data = null;
                    }
                    userBean = new UserBean(user_id,account,pwd,name,avatar,data);
                    this.mfairyDB.saveUser(userBean);
                }
            }
//            if(jsonObj.has("reply")){
//                JSONArray reply = jsonObj.getJSONArray("reply");
//                String key;
//                String content;
//                ReplyBean replyBean;
//                for(int i=0;i<reply.length();i++){
//                    JSONObject r = reply.getJSONObject(i);
//                    key = r.getString("key");
//                    content = r.getString("content");
//                    replyBean = new ReplyBean(key,content);
//                    this.mfairyDB.saveReply(replyBean);
//                }
//            }
        } catch (final JSONException e) {
            Log.e(JsonUtil.class.getName(), "Json parsing error: " + e.getMessage());
        }
    }

    public String getJsonStr() {
        return mjsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.mjsonStr = jsonStr;
    }
}