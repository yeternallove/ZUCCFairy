package com.eternallove.demo.zuccfairy.util;

import android.util.Log;

import com.eternallove.demo.zuccfairy.modle.ChatMessageBean;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import com.google.zxing.Result;

import org.json.JSONObject;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/11/20
 */
public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();
    private static final String URL_KEY = "http://www.tuling123.com/openapi/api";
    private static final String APP_KEY = "8995f168133f4216b0698682d7758318";

    public HttpHandler() {
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 发送消息到服务器
     *
     * @param message
     *            ：发送的消息
     * @return：消息对象
     */
    public static ChatMessageBean sendMessage(String message,String mId) {
        ChatMessageBean chatMessage = new ChatMessageBean();
        String gsonResult = doGet(message);
        if (gsonResult != null) {
            try {
                JSONObject jsonObj = new JSONObject(gsonResult);
                if(jsonObj.has("text")){
                    chatMessage.setMessage(jsonObj.getString("text"));
                }else{
                    chatMessage.setMessage(null);
                }
            } catch (Exception e) {
                chatMessage.setMessage("服务器繁忙，请稍候再试...");
            }
        }
        chatMessage.setPicture(null);
        chatMessage.setRecipient_id(mId);
        chatMessage.setSender_id("laiye");
        chatMessage.setTimestampe(System.currentTimeMillis());
        return chatMessage;
    }

    /**
     * get请求
     *
     * @param message
     *            ：发送的话
     * @return：数据
     */
    public static String doGet(String message) {
        String result = "";
        String url = setParmat(message);
        Log.i(TAG,"------------url = " + url);
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL urls = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urls
                    .openConnection();
            connection.setReadTimeout(5 * 1000);
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestMethod("GET");

            is = connection.getInputStream();
            baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            baos.flush();
            result = new String(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 设置参数
     *
     * @param message
     *            : 信息
     * @return ： url
     */
    private static String setParmat(String message) {
        String url = "";
        try {
            url = URL_KEY + "?" + "key=" + APP_KEY + "&info="
                    + URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
