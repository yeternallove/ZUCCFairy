package com.eternallove.demo.zuccfairy.ui.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.service.SendAlarmBroadcast;
import com.eternallove.demo.zuccfairy.db.FairyDB;
import com.eternallove.demo.zuccfairy.util.PrefUtils;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/24 16:11
 */

public class CalendarActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    private static FairyDB db;
    @BindView(R.id.activity_main_rfal)
    RapidFloatingActionLayout rfaLayout;
    @BindView(R.id.activity_main_rfab)
    RapidFloatingActionButton rfaBtn;

    RapidFloatingActionHelper rfabHelper;

    public static FairyDB getFairyDB() {
        return db;
    }

    public static void actionStart(Context context){
        Intent intent=new Intent();
        intent.setClass(context,CalendarActivity.class);
        context.startActivity(intent);
    }

    private boolean isAllowAlert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ButterKnife.bind(this);
        db = FairyDB.getInstance(this);
        initFab();

        //弹窗权限验证
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isAllowAlert = PrefUtils.getBoolean(this,"isAllowAlert",false);
            Log.d("isAllowAlert:" + isAllowAlert, "Calendar");
            if(!isAllowAlert){
                showPermissionDialog();
            }
        }else {
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
    private void showPermissionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
        builder.setTitle("弹窗需要开启权限哦~")
                .setPositiveButton("开启", (dialog, which) -> {
                    requestAlertWindowPermission();
                })
                .setNegativeButton("取消", (dialog, which) -> {

                });
        builder.create().show();
    }


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
            intent.putExtra("type","mainToAdd");
            startActivity(intent);
            finish();
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
}