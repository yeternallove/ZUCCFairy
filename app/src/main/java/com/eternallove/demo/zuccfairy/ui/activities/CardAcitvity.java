package com.eternallove.demo.zuccfairy.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eternallove.demo.zuccfairy.util.QRHelper;

import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.util.DateHelper;
import com.eternallove.demo.zuccfairy.util.RoundImageView;

import java.util.Date;

/**
 * Created by angelzouxin on 2016/12/20.
 */

public class CardAcitvity extends AppCompatActivity {
    private static String DATE_FORMAT = "yyyy年MM月dd日 HH:mm";
    private static final int TOP_Time = 5 * 60;
    private static final int BUTTON_Time = 10 * 60;
    public int QR_WIDTH;
    public int QR_HEIGHT;

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CardAcitvity.class);
        context.startActivity(intent);
    }

    //5:00~10:00打卡，在生成card前请先调用该函数判断时间
    public static int checkTime() {
        String datestr[] = DateHelper.dateToString(new Date(System.currentTimeMillis()), "HH:mm").split(":");
        int hour = Integer.valueOf(datestr[0]), min = Integer.valueOf(datestr[1]);
        if (hour * 60 + min < TOP_Time) return 0;
        else if (hour * 60 + min <= BUTTON_Time) return 1;
        else return 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_card);
        init();
    }

    void init() {
        RoundImageView card_pic_me = (RoundImageView) findViewById(R.id.card_pic_me); //我的头像
        TextView card_time = (TextView) findViewById(R.id.card_time); //HH:mm
        TextView card_ymd = (TextView) findViewById(R.id.card_ymd); //yyyy年MM月dd日
        TextView card_punch_time = (TextView) findViewById(R.id.card_punch_time); //打卡持续天数
        TextView card_text_me = (TextView) findViewById(R.id.card_text_me); //xxxx人正在参与，比xx%的人起的早
        TextView card_word = (TextView) findViewById(R.id.card_word); //行动比语言更有说服力
        final ImageView card_QR = (ImageView) findViewById(R.id.card_QR); //二维码

        String datestr = DateHelper.dateToString(new Date(System.currentTimeMillis()), "yyyy年MM月dd日 HH:mm");
        String timestr[] = datestr.split(" ");
        card_ymd.setText(timestr[0]);
        card_time.setText(timestr[1]);

        //获取QR_card的height和width
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        card_QR.measure(w, h);
        int height = card_QR.getMeasuredHeight();
        int width = card_QR.getMeasuredWidth();

        /*TODO:从user信息中获取头像和打卡持续天数
        * card_pic_me.setImageResource(); ||  card_pic_me.setImageBitmap();
        * card_punch_time.setText();
        */

        //获得当前打卡人数，再计算
        card_text_me.setText("57891人正在参与，比80%的人起的早");

        card_word.setText("行动比语言更有说服力");

        //TODO:设置QR图片，实际改为"id:"+userid
        card_QR.setImageBitmap(QRHelper.createQRImage("id:zhangjia", height * 2, width * 2));
    }
}
