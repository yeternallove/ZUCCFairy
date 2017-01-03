package com.eternallove.demo.zuccfairy.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.modle.Information;
import com.eternallove.demo.zuccfairy.ui.adapters.InformationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/25.
 */
public class InformationActivity extends Activity {
    private List<Information> informationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在oncreate（）方法中创建了adapter对象，并将adapter作为适配器传递给了listview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalinformation);
        initinformationList();
        InformationAdapter informationAdapter = new InformationAdapter(InformationActivity.this, R.layout.information_item, informationList);
        ListView informationListView = (ListView) findViewById(R.id.informationListView);
        informationListView.setAdapter(informationAdapter);

        informationListView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(i == 5){
                PreferenceManager.getDefaultSharedPreferences(InformationActivity.this).edit().putBoolean("Login", false);
                LoginActivity.actionStart(InformationActivity.this);
            }
        });
    }

    private void initinformationList() {
        Information head = new Information("头像", R.drawable.ic_user_1,null);
        informationList.add(head);
        Information nickname = new Information("昵称",-1, "西瓜");
        informationList.add(nickname);
        Information sex = new Information("性别",R.drawable.female_pink,null);
        informationList.add(sex);
        Information birthday = new Information("生日",-1,"1995.02.18");
        informationList.add(birthday);
        Information mobilePhoneNumber = new Information("手机号", -1,"17764526762");
        informationList.add(mobilePhoneNumber);
        Information member = new Information("退出账号哦",-1,null);
        informationList.add(member);
    }
}
