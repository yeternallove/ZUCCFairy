package com.eternallove.demo.zuccfairy.ui.activities;

import android.app.Activity;
import android.os.Bundle;

import com.eternallove.demo.zuccfairy.R;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在oncreate（）方法中创建了adapter对象，并将adapter作为适配器传递给了listview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
}
