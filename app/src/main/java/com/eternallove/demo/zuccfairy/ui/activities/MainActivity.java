package com.eternallove.demo.zuccfairy.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eternallove.demo.zuccfairy.R;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int NUM_BOT_DIR_1=1;//子目录中item的个数
    private static final int NUM_BOT_DIR_3=1;

    private InputMethodManager imm;
    private View[] mViews = new View[2];
    private int mCurrent;
    private int mpopupHeight;
    private View mPopupView1;
    private View mPopupView3;
    private PopupWindow mPopupWindow;
    private TextView mtvDailyPunch;
    private TextView mtvHomepage;

    @BindView(R.id.recyclerView)    RecyclerView mRecyclerView;
    @BindView(R.id.edt_bot_send)    EditText     mEditText;
    @BindView(R.id.btn_bot_send)    Button       mBtnSend;
    @BindView(R.id.bot_dir_1)       Button       mBtnBotDir1;
    @BindView(R.id.bot_dir_2)       Button       mBtnBotDir2;
    @BindView(R.id.bot_dir_3)       Button       mBtnBotDir3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViews[0] = findViewById(R.id.ll_bot_dir);
        mViews[1] = findViewById(R.id.ll_bot_in);
        mCurrent = 0;
        mpopupHeight = (int ) this.getResources().getDimension(R.dimen.height_popupWindow);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mPopupView1 = getLayoutInflater().inflate(R.layout.popup_window_dir_1,null);
        mPopupView3 = getLayoutInflater().inflate(R.layout.popup_window_dir_3,null);

        mtvDailyPunch = (TextView) mPopupView1.findViewById(R.id.tv_daily_punch);
        mtvHomepage = (TextView) mPopupView3.findViewById(R.id.tv_homepage);

        mPopupWindow = new PopupWindow(mPopupView1,WRAP_CONTENT, WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        //start Login
        int mId= PreferenceManager.getDefaultSharedPreferences(this).getInt("mId",-1);
        if(mId == -1){
            LoginActivity.actionStart(this);
        }

        mBtnSend.setOnClickListener(this);
        mBtnBotDir1.setOnClickListener(this);
        mBtnBotDir2.setOnClickListener(this);
        mBtnBotDir3.setOnClickListener(this);
        mtvDailyPunch.setOnClickListener(this);
        mtvHomepage.setOnClickListener(this);
    }

    /**
     * 如其名
     * @param view
     */
    public void shift(View view) {

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
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 如果点击的还是输入框就返回FALSE
     * @param v
     * @param event
     * @return
     */
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bot_dir_1:
                mPopupWindow.setContentView(mPopupView1);
                mPopupWindow.showAsDropDown(mBtnBotDir1,0,-1*(mViews[0].getHeight()+NUM_BOT_DIR_1*mpopupHeight));
                break;
            case R.id.bot_dir_2:
                CalendarActivity.actionStart(this);
                break;
            case R.id.bot_dir_3:
                mPopupWindow.setContentView(mPopupView3);
                mPopupWindow.showAsDropDown(mBtnBotDir3,0,-1*(mViews[0].getHeight()+NUM_BOT_DIR_3*mpopupHeight));
                break;
            case R.id.tv_daily_punch:
                Toast.makeText(this, "你点击了“”按键！", Toast.LENGTH_SHORT).show();
                mPopupWindow.dismiss();
                break;
            case R.id.tv_homepage:
                Toast.makeText(this, "你点击了“home”按键！", Toast.LENGTH_SHORT).show();
                mPopupWindow.dismiss();
                break;
            case R.id.btn_bot_send:
                mEditText.clearFocus();
                break;
            default:break;
        }
    }
}
