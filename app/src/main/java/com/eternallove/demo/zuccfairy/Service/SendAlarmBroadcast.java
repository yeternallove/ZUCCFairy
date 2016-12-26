package com.eternallove.demo.zuccfairy.service;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by angelzouxin on 2016/12/25.
 */
public class SendAlarmBroadcast {

    public static void startAlarmService(Activity activity){
        Intent startAlarmServiceIntent = new Intent(activity,AlarmServiceBroadcastReceiver.class);
        activity.sendBroadcast(startAlarmServiceIntent,null);
    }
}
