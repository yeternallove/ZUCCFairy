package com.eternallove.demo.zuccfairy.ui.activities.alarmset;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;


import com.eternallove.demo.zuccfairy.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by angelzouxin on 2016/12/25.
 */
public class SetAlarmTimeActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox no_remind, min10_remind, hour1_remind, day1_remind;

    @OnClick(R.id.left_alarm_back)
    void finishClose() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm_time);
        ButterKnife.bind(this);
        no_remind = (CheckBox) findViewById(R.id.no_remind);
        min10_remind = (CheckBox) findViewById(R.id.min10_remind);
        hour1_remind = (CheckBox) findViewById(R.id.hour1_remind);
        day1_remind = (CheckBox) findViewById(R.id.day1_remind);

        no_remind.setChecked(true);

        no_remind.setOnClickListener(this);
        min10_remind.setOnClickListener(this);
        hour1_remind.setOnClickListener(this);
        day1_remind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.no_remind:
                isChecked(false);
                no_remind.setChecked(true);
                intent.putExtra("remind", "无");
                setResult(1, intent);
                finish();
                break;
            case R.id.min10_remind:
                isChecked(false);
                min10_remind.setChecked(true);
                intent.putExtra("remind", "提前十分钟提醒");
                setResult(1, intent);
                finish();
                break;
            case R.id.hour1_remind:
                isChecked(false);
                hour1_remind.setChecked(true);
                intent.putExtra("remind", "提前一个小时通知");
                setResult(1, intent);
                finish();
                break;
            case R.id.day1_remind:
                isChecked(false);
                day1_remind.setChecked(true);
                intent.putExtra("remind", "提前一天提醒");
                setResult(1, intent);
                finish();
                break;
        }
    }
    public void isChecked(boolean is){
        day1_remind.setChecked(is);
        no_remind.setChecked(is);
        min10_remind.setChecked(is);
        hour1_remind.setChecked(is);
    }
}
