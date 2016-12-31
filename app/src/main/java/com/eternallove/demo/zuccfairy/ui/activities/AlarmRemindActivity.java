package com.eternallove.demo.zuccfairy.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.eternallove.demo.zuccfairy.modle.AlarmBean;

/**
 * Created by angelzouxin on 2016/12/31.
 */

public class AlarmRemindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        AlarmBean alarmBean = (AlarmBean) bundle.getSerializable("alarm");
        System.out.println(alarmBean.getTitle());
        Toast.makeText(this, "闹钟响了！" + alarmBean.getTitle(), Toast.LENGTH_SHORT).show();
    }

}
