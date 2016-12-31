package com.eternallove.demo.zuccfairy.ui.fragment;

import android.support.design.widget.NavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.modle.CalendarEvent;
import com.eternallove.demo.zuccfairy.ui.activities.CalendarActivity;
import com.eternallove.demo.zuccfairy.ui.pager.BasePager;
import com.eternallove.demo.zuccfairy.ui.pager.DayPager;
import com.eternallove.demo.zuccfairy.ui.pager.HomePager;
import com.eternallove.demo.zuccfairy.ui.pager.WeekPager;
import com.eternallove.demo.zuccfairy.util.BusProvider;
import com.eternallove.demo.zuccfairy.util.CalendarManager;
import com.eternallove.demo.zuccfairy.util.Events;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by acer-pc on 2016/3/11.
 */
public class ContentFragment extends BaseFragment {

    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private List<BasePager> mPageList;


    private List<CalendarEvent> eventList;
    private HomePager homePager;
    private DayPager dayPager;
    private WeekPager weekPager;

    Unbinder unbind;


    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.fragment_content, null);

        unbind = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @Override
    public void initDate() {

        homePager = new HomePager(mActivity);
        dayPager = new DayPager(mActivity);
        weekPager = new WeekPager(mActivity);

        //主界面添加数据
        mPageList = new ArrayList<>();

        mPageList.add(homePager);
        mPageList.add(dayPager);
        mPageList.add(weekPager);

        vpContent.setAdapter(new VpContentAdapter());

        buildHomePager();


    }

    public void changFrame(int ncase) {
        switch (ncase) {
            case 1:
                vpContent.setCurrentItem(0, false);//设置当前的页面，取消平滑滑动
                buildHomePager();
                break;
            case 2:
                vpContent.setCurrentItem(1, false);
                dayPager.initData();
                break;
            case 3:
                vpContent.setCurrentItem(2, false);
                weekPager.initData();
                break;
            default:
        }
    }

    /**
     * 主界面设置
     */
    private void buildHomePager() {
        homePager.initData();
        BusProvider.getInstance().toObserverable().subscribe(event -> {
            if (event instanceof Events.GoBackToDay) {
                homePager.agenda_view.getAgendaListView().scrollToCurrentDate(CalendarManager.getInstance().getToday());
            }
        });
    }


    /**
     * viewPager数据适配器
     */
    class VpContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPageList.get(position);
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mPageList.get(position).mRootView);
        }
    }

}
