package com.eternallove.demo.zuccfairy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


import com.eternallove.demo.zuccfairy.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

import static android.os.SystemClock.sleep;


/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/25
 */
public class SplashActivity extends AppCompatActivity {

    //    @BindView(R.id.version_code)
//    TextView tvVersionCode;
    //TODO wait update
    private String mVersionName;//版本名
    private int mVersionCode;//版本号
    private String mDesc;//版本描述
    private String mdownloadUrl;//新版本下载网站

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        boolean Login = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("Login", false);
        if (Login) {
            //3S的延迟
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    MainActivity.actionStart(SplashActivity.this);
                    finish();
                }
            };
            timer.schedule(task, 3000);

        } else {
            LoginActivity.actionStart(this);
            finish();
        }
    }
}
