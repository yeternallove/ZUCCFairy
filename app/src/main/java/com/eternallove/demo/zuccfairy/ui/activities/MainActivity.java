package com.eternallove.demo.zuccfairy.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.ui.activities.LoginActivity;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private InputMethodManager imm;
    private View[] mViews = new View[2];
    private int mCurrent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViews[0] = findViewById(R.id.ll_bot_dir);
        mViews[1] = findViewById(R.id.ll_bot_in);
        mCurrent = 0;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    public void shift(View view) {

        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        mViews[mCurrent].animate().translationY(mViews[mCurrent].getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mViews[1 - mCurrent].animate().translationY(0)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mCurrent = 1 - mCurrent;
                                    }
                                }).start();
                    }
                }).start();
    }
}
