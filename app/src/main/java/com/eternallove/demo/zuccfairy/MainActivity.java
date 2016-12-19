package com.eternallove.demo.zuccfairy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eternallove.demo.zuccfairy.ui.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginActivity.actionStart(this);
    }
}
