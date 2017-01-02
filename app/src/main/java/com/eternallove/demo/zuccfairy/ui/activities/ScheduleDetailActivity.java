package com.eternallove.demo.zuccfairy.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.db.FairyDB;
import com.eternallove.demo.zuccfairy.modle.AlarmBean;
import com.eternallove.demo.zuccfairy.util.ColorUtils;
import com.eternallove.demo.zuccfairy.service.SendAlarmBroadcast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by angelzouxin on 2016/12/31.
 */

public class ScheduleDetailActivity extends AppCompatActivity {

    //detail
    @BindView(R.id.detail_alarm_title)
    TextView detail_alarm_title;
    @BindView(R.id.detail_alarm_date)
    TextView detail_alarm_date;
    @BindView(R.id.detail_alarm_start_end_time)
    TextView detail_alarm_start_end_time;
    @BindView(R.id.detail_alarm_remind)
    TextView detail_alarm_remind;
    @BindView(R.id.detail_alarm_local)
    TextView detail_alarm_local;
    @BindView(R.id.detail_alarm_description)
    TextView detail_alarm_description;
    @BindView(R.id.detail_layout)
    RelativeLayout detail_layout;

    //删除
    @OnClick(R.id.tv_delete)
    void detele() {
        dialog();
    }

    @BindView(R.id.update_fab)
    FloatingActionButton update_fab;

    //更新修改
    @OnClick(R.id.update_fab)
    void updateData() {

        Intent intent = new Intent(this, AddScheduleActivity.class);
        intent.putExtra("AlarmBean", bean);
        intent.putExtra("type", "DetailToAdd");
        startActivity(intent);
        finish();

    }

    @OnClick(R.id.left_alarm_back)
    void back() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private int id;
    private AlarmBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);

        ButterKnife.bind(this);

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        System.out.println("---id=" + id);


        bean = getDataById(id);
        Calendar calendar = Calendar.getInstance();
        calendar.set(bean.getYear(), bean.getMonth(), bean.getDay());
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日  EE");
        //匹配数据
        detail_alarm_title.setText(bean.getTitle());
        detail_alarm_date.setText(df.format(calendar.getTime()) + "  本月第" + calendar.get(Calendar.WEEK_OF_MONTH) + "周");
        buildStartEndTime(bean.getStartTimeHour(), bean.getStartTimeMinute(), bean.getEndTimeHour(), bean.getEndTimeMinute());
        detail_alarm_remind.setText(bean.getAlarmTime());
        detail_alarm_local.setText(bean.getLocal());
        detail_alarm_description.setText(bean.getDescription());

        int colorId = ColorUtils.getColorFromStr(bean.getAlarmColor());
        detail_layout.setBackgroundColor(getResources().getColor(colorId));
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(colorId));
        }

    }


    /**
     * 根据ID查询一条数据
     */
    private AlarmBean getDataById(int id) {
        AlarmBean alarmBean = FairyDB.getInstance(this).getDataById(id);
        return alarmBean;
    }

    /**
     * 构建开始结束时间
     */
    private void buildStartEndTime(int startHour, int startMinute, int endHour, int endMinute) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, startHour);
        startTime.set(Calendar.MINUTE, startMinute);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, endHour);
        endTime.set(Calendar.MINUTE, endMinute);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String strStartTime = df.format(startTime.getTime());
        String strEndTime = df.format(endTime.getTime());

        detail_alarm_start_end_time.setText(strStartTime + "-" + strEndTime);
    }

    /**
     * 根据ID删除数据
     */
    private void deteleDataById(int id) {
        FairyDB.getInstance(this).deleteDataById(id);

        SendAlarmBroadcast.startAlarmService(this);

    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("确定要删除吗？"); //设置标题
        builder.setIcon(R.drawable.icon);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deteleDataById(id);
                startActivity(new Intent(ScheduleDetailActivity.this, MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
