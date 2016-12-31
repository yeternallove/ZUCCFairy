package com.eternallove.demo.zuccfairy.ui.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.Service.SendAlarmBroadcast;
import com.eternallove.demo.zuccfairy.db.FairyDB;
import com.eternallove.demo.zuccfairy.ui.customview.calendar.CalendarView;
import com.eternallove.demo.zuccfairy.ui.fragment.ContentFragment;
import com.eternallove.demo.zuccfairy.util.BusProvider;
import com.eternallove.demo.zuccfairy.util.CalendarManager;
import com.eternallove.demo.zuccfairy.util.Events;
import com.eternallove.demo.zuccfairy.util.PrefUtils;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/24 16:11
 */

public class CalendarActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    @BindView(R.id.activity_main_rfal)
    RapidFloatingActionLayout rfaLayout;
    @BindView(R.id.activity_main_rfab)
    RapidFloatingActionButton rfaBtn;

    RapidFloatingActionHelper rfabHelper;


    //日历View
    @BindView(R.id.calendar_view)
    public CalendarView calendar_view;
    //CalendarView自定义控件的属性
    private int mCalendarHeaderColor, mCalendarDayTextColor, mCalendarPastDayTextColor, mCalendarCurrentDayColor;
    //月份
    @BindView(R.id.title_day)
    TextView title_day;
    //日程 ListView
    @BindView(R.id.main_frame)
    FrameLayout main_frame;

    //旋转箭头
    @BindView(R.id.title_arrow)
    ImageView title_arrow;
    //打开关闭CalendarView
    private boolean isOpen = true;//日历视图是否显示

    @OnClick(R.id.calendar_open_close)
    void openAndCloseCalendarView() {
        if (!isOpen) {
            calendar_view.setVisibility(View.VISIBLE);
            arrowRotateAnim(isOpen);
            isOpen = true;
        } else {
            calendar_view.setVisibility(View.GONE);
            arrowRotateAnim(isOpen);
            isOpen = false;
        }
    }

    //回到今天
    @OnClick(R.id.go_today)
    void goToday() {
//        homePager.agenda_view.getAgendaListView().scrollToCurrentDate(CalendarManager.getInstance().getToday());
        BusProvider.getInstance().send(new Events.GoBackToDay());
        calendar_view.scrollToDate(CalendarManager.getInstance().getToday(), CalendarManager.getInstance().getWeeks());
    }

    //议程列表
    private ContentFragment fragment;

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CalendarActivity.class);
        context.startActivity(intent);
    }

    private boolean isAllowAlert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ButterKnife.bind(this);

        initCalendarInfo();
        initLayoutView();
        setMonthTitle();
        initFab();

        //弹窗权限验证
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isAllowAlert = PrefUtils.getBoolean(this, "isAllowAlert", false);
            Log.d("isAllowAlert:" + isAllowAlert, "Calendar");
            if (!isAllowAlert) {
                showPermissionDialog();
            }
        } else {
            SendAlarmBroadcast.startAlarmService(this);
        }
    }

    //权限申请相关方法
    private static final int REQUEST_CODE = 1;

    @TargetApi(Build.VERSION_CODES.M)
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 权限申请弹窗
     */
    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
        builder.setTitle("弹窗需要开启权限哦~")
                .setPositiveButton("开启", (dialog, which) -> {
                    requestAlertWindowPermission();
                })
                .setNegativeButton("取消", (dialog, which) -> {

                });
        builder.create().show();
    }

    /**
     * 初始化布局
     */
    private void initLayoutView() {
        //初始化 CalendarView,设置颜色
        mCalendarHeaderColor = R.color.calendar_header_day_background;
        mCalendarDayTextColor = R.color.calendar_text_day;
        mCalendarCurrentDayColor = R.color.calendar_text_current_day;
        mCalendarPastDayTextColor = R.color.calendar_text_past_day;

        calendar_view.findViewById(R.id.cal_day_names).setBackgroundColor(getResources().getColor(mCalendarHeaderColor));

        // CalendarView 初始化,完成布局的初始化，数据的绑定,日期的显示
        calendar_view.init(CalendarManager.getInstance(this), getResources().getColor(mCalendarDayTextColor), getResources().getColor(mCalendarCurrentDayColor), getResources().getColor(mCalendarPastDayTextColor));

        //填充 Fragment
        FragmentManager manager = getFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new ContentFragment();
        transaction.replace(R.id.main_frame, fragment, "CONTENT_FRAGMENT");


        transaction.commit();

    }

    /**
     * title_arrow 旋转动画
     */
    private void arrowRotateAnim(Boolean isOpen) {
        RotateAnimation anim;
        if (isOpen) {
            anim = new RotateAnimation(0, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            anim = new RotateAnimation(180f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        anim.setDuration(150);
        anim.setFillAfter(true);
        title_arrow.startAnimation(anim);
    }

    /**
     * 设置月份
     */
    private void setMonthTitle() {
        BusProvider.getInstance().toObserverable().subscribe(event -> {
            if (event instanceof Events.CalendarMonthEvent) {
                Events.CalendarMonthEvent monthEvent = (Events.CalendarMonthEvent) event;
                title_day.setText(monthEvent.getMonth());
            }
        });
    }

    /**
     * 初始化 Fab
     */

    private void initFab() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("活动")
                .setResId(R.drawable.ic_access_alarms_white_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("所有行程")
                .setResId(R.drawable.ic_schedule_grey_900_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("日程")
                .setResId(R.drawable.ic_today_grey_900_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("周程")
                .setResId(R.drawable.ic_receipt_grey_900_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();

    }

    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {
        Toast.makeText(this, "Click Label !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        if (i == 0) {
            Intent intent = new Intent(CalendarActivity.this, AddScheduleActivity.class);
            intent.putExtra("type", "mainToAdd");
            startActivity(intent);
            finish();
        } else if (i <= 3) {
            fragment.changFrame(i);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("---onRestart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        support.deactivate();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "弹窗权限开启！", Toast.LENGTH_SHORT).show();
                PrefUtils.setBoolean(CalendarActivity.this, "isAllowAlert", true);
            } else {
                PrefUtils.setBoolean(CalendarActivity.this, "isAllowAlert", false);
            }
        }
    }


    /**
     * 初始化日历信息
     */
    private void initCalendarInfo() {
        // 设置日历显示的时间，最大为当前时间+1年，最小为当前时间-2月
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -10);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);
        //根据你传入的开始结束值，构建生成Calendar数据（各种Item，JavaBean）
        CalendarManager.getInstance(this).buildCal(minDate, maxDate, Locale.getDefault());
    }

}