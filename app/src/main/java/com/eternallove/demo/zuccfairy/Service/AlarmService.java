package com.eternallove.demo.zuccfairy.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.eternallove.demo.zuccfairy.db.FairyDB;
import com.eternallove.demo.zuccfairy.modle.AlarmBean;
import com.eternallove.demo.zuccfairy.ui.activities.CalendarActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by angelzouxin on 2016/12/25.
 */
public class AlarmService extends Service {

    private FairyDB db;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 得到下一条提醒的日程信息
     * @return
     */
    private AlarmBean getNext() {

        db = CalendarActivity.getFairyDB();
        List<AlarmBean> all = db.getAll();
        if (all.size() == 0) {
            return null;
        }

        Collections.sort(all, new Comparator<AlarmBean>() {
            @Override
            public int compare(AlarmBean lhs, AlarmBean rhs) {
                if (lhs.getRealAlarmTime().getTimeInMillis() > rhs.getRealAlarmTime().getTimeInMillis()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getRealAlarmTime().getTimeInMillis() - all.get(i).getCurrentTime().getTimeInMillis() > 0) {
                return all.get(i);
            }
        }

        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmBean alarmBean = getNext();
        if (alarmBean != null) {
            alarmBean.schedule(getApplicationContext());
        }
        return START_NOT_STICKY;
    }
}
