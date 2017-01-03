package com.eternallove.demo.zuccfairy.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.ui.adapters.HomePageAdapter;
import com.eternallove.demo.zuccfairy.modle.HomePage;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {

    private List<HomePage> myList = new ArrayList<>();
    private List<HomePage> rightsList = new ArrayList<>();

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在oncreate（）方法中创建了adapter对象，并将adapter作为适配器传递给了listview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initHomepages();
        ImageView headImg = (ImageView) findViewById(R.id.headImg);
        if (headImg != null) {
            headImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, InformationActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void initHomepages() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.homePageViewPager);
        final List<View> viewList = new ArrayList<>();
        View myView = View.inflate(this, R.layout.activity_home_my, null);
        viewList.add(myView);
        initMy();//初始化数据
        HomePageAdapter myAdapter = new HomePageAdapter(HomeActivity.this, R.layout.homepage_item, myList);
        ListView myListView = (ListView) myView.findViewById(R.id.myListView);
        myListView.setAdapter(myAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                HomePage homePage = myList.get(position);
//                Toast.makeText(HomeActivity.this, homePage.getName(), Toast.LENGTH_SHORT).show();
                if(position==0){
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, SettingActivirty.class);
                    startActivity(intent);
                }else if(position==2){
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, HelpActivity.class);
                    startActivity(intent);
                }
            }
        });
        View rightsView = View.inflate(this, R.layout.rights, null);
        initRights();
        HomePageAdapter rightsAdapter = new HomePageAdapter(HomeActivity.this, R.layout.homepage_item, rightsList);
        ListView rightsListView = (ListView) rightsView.findViewById(R.id.rightsListView);
        rightsListView.setAdapter(rightsAdapter);
        rightsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                HomePage homePage = rightsList.get(position);
                Toast.makeText(HomeActivity.this, homePage.getName(), Toast.LENGTH_SHORT).show();
            }
            //使用了setOnItemClickListener（）方法来为ListView注册了一个监听器，
            //当用户点击了ListView中的任何一个子项就会回调onIntemClick方法
            //在这个方法中可以通过position参数判断出用户点击的是哪一个子项，然后获取到相应的水果
            //并通过Toast将水果的名字显示出来
        });
        viewList.add(rightsView);
        PagerAdapter viewPagerAdapter = new PagerAdapter() {
            //官方建议这么写
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            //返回一共有多少个界面
            @Override
            public int getCount() {
                return viewList.size();
            }

            //实例化一个item
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            //销毁一个item
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

        };

        final Button myButton = (Button) findViewById(R.id.myButton);
        final Button rightsButton = (Button) findViewById(R.id.rightsButton);

        if (myButton != null && rightsButton != null) {
            rightsButton.setAlpha(1);
            myButton.setAlpha((float) 0.3);

            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewPager != null) {
                        viewPager.setCurrentItem(0);
                        rightsButton.setAlpha(1);
                        myButton.setAlpha((float) 0.3);
                    }
                }
            });

            rightsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewPager != null) {
                        viewPager.setCurrentItem(1);
                        myButton.setAlpha(1);
                        rightsButton.setAlpha((float) 0.3);
                    }
                }
            });
        }

        if (viewPager != null) {
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int arg0) {
                    if(arg0==0){
                        if (myButton != null) {
                            myButton.callOnClick();
                        }
                    }else if(arg0==1){
                        if (rightsButton != null) {
                            rightsButton.callOnClick();
                        }
                    }
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
    }

    private void initMy() {
        HomePage information = new HomePage("编辑信息", R.drawable.pen);
        myList.add(information);
        HomePage schedule = new HomePage("日程", R.drawable.schedule);
        myList.add(schedule);
        HomePage help = new HomePage("帮助", R.drawable.help);
        myList.add(help);
    }

    private void initRights() {
        //构造器函数中将名字和对应图片id传入，然后把创建好的对象添加到水果列表中。
        HomePage remind = new HomePage("日程提醒", R.drawable.remind);
        rightsList.add(remind);
        HomePage punch = new HomePage("打卡", R.drawable.punch);
        rightsList.add(punch);
    }
}
