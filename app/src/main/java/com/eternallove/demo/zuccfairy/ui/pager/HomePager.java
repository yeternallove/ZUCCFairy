package com.eternallove.demo.zuccfairy.ui.pager;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;


import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.db.FairyDB;
import com.eternallove.demo.zuccfairy.modle.AlarmBean;
import com.eternallove.demo.zuccfairy.modle.BaseCalendarEvent;
import com.eternallove.demo.zuccfairy.modle.CalendarEvent;
import com.eternallove.demo.zuccfairy.ui.activities.CalendarActivity;
import com.eternallove.demo.zuccfairy.ui.activities.MainActivity;
import com.eternallove.demo.zuccfairy.ui.activities.ScheduleDetailActivity;
import com.eternallove.demo.zuccfairy.ui.customview.agenda.AgendaAdapter;
import com.eternallove.demo.zuccfairy.ui.customview.agenda.AgendaView;
import com.eternallove.demo.zuccfairy.ui.customview.calendar.CalendarView;
import com.eternallove.demo.zuccfairy.ui.customview.render.DefaultEventRenderer;
import com.eternallove.demo.zuccfairy.ui.customview.render.EventRenderer;
import com.eternallove.demo.zuccfairy.util.CalendarManager;
import com.eternallove.demo.zuccfairy.util.ColorUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * 用于显示界面的 Pager ，通过设置不同的 adapter 实现布局复用
 */
public class HomePager extends BasePager implements StickyListHeadersListView.OnStickyHeaderChangedListener {

    private static final String LOG_TAG = HomePager.class.getSimpleName();

    @BindView(R.id.agenda_view)
    public AgendaView agenda_view;
    public AgendaAdapter agendaAdapter;

    private int mAgendaCurrentDayTextColor = mActivity.getResources().getColor(R.color.calendar_agendaHead_current_color);

    public HomePager(Activity mActivity) {
        super(mActivity);

    }

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.pager_home, null);

        ButterKnife.bind(this, view);

        return view;
    }


    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        //初始化数据
        List eventList = new ArrayList<>();
        List<AlarmBean> alllist = FairyDB.getInstance(mActivity).getAll();
        mockList(eventList, alllist);

        // 初始化AgendaView的数据适配器
        agendaAdapter = new AgendaAdapter(mAgendaCurrentDayTextColor);
        agenda_view.getAgendaListView().setAdapter(agendaAdapter);

        //注册AgendaListView的OnStickyHeaderChanged监听事件
        agenda_view.getAgendaListView().setOnStickyHeaderChangedListener(this);
        agenda_view.getAgendaListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView alarm_id = (TextView) view.findViewById(R.id.Alarm_id);
                Toast.makeText(mActivity, position + "-" + alarm_id.getText().toString(), Toast.LENGTH_SHORT).show();
                if (!alarm_id.getText().toString().equals("0")) {
                    Intent intent = new Intent(mActivity, ScheduleDetailActivity.class);
                    intent.putExtra("id", alarm_id.getText().toString());
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }

            }
        });

        //将日期与event进行匹配
        CalendarManager.getInstance().loadEvents(eventList);

        // 添加默认的 Renderer 渲染布局
        addEventRenderer(new DefaultEventRenderer());

    }

    //为event添加布局渲染
    public void addEventRenderer(@NonNull final EventRenderer<?> renderer) {
        AgendaAdapter adapter = (AgendaAdapter) agenda_view.getAgendaListView().getAdapter();
        adapter.addEventRenderer(renderer);
    }

    /**
     * 当ListView的头部改变时,同时改变CalendarView的日期
     *
     * @param l            ListView
     * @param header       头部 header View
     * @param itemPosition 位置
     * @param headerId     头部 header ID
     */
    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        Log.d(LOG_TAG, String.format("onStickyHeaderChanged, position = %d, headerId = %d", itemPosition, headerId));

        if (CalendarManager.getInstance().getEvents().size() > 0) {
            CalendarEvent event = CalendarManager.getInstance().getEvents().get(itemPosition);
            if (event != null) {
                getCalendarView().scrollToDate(event);
            }
        }
    }

    /**
     * 得到 CalendarView
     */
    private CalendarView getCalendarView() {
        CalendarActivity mainUi = (CalendarActivity) mActivity;
        return mainUi.calendar_view;
    }

    /**
     * 构建数据集合
     *
     * @param eventList list显示的集合
     * @param beanList  从数据库中读到的全部数据集合
     */
    private void mockList(List<CalendarEvent> eventList, List<AlarmBean> beanList) {

        for (AlarmBean bean : beanList) {
            Calendar startTime1 = Calendar.getInstance();
            startTime1.set(bean.getYear(), bean.getMonth(), bean.getDay());
            Calendar endTime1 = Calendar.getInstance();
            endTime1.set(bean.getYear(), bean.getMonth(), bean.getDay());

            boolean isAllday;
            if (bean.getIsAllday() == 1) {
                isAllday = true;
            } else {
                isAllday = false;
            }

            int colorId = ColorUtils.getColorFromStr(bean.getAlarmColor());

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(Calendar.HOUR_OF_DAY, bean.getStartTimeHour());
            startCalendar.set(Calendar.MINUTE, bean.getStartTimeMinute());
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(Calendar.HOUR_OF_DAY, bean.getEndTimeHour());
            endCalendar.set(Calendar.MINUTE, bean.getEndTimeMinute());

            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            String startTime = df.format(startCalendar.getTime());
            String endTime = df.format(endCalendar.getTime());
            String startAndEndTime = startTime + "-" + endTime;

            BaseCalendarEvent event1 = new BaseCalendarEvent(bean.getId(), bean.getTitle(), bean.getDescription(), bean.getLocal(),
                    ContextCompat.getColor(mActivity, colorId), startTime1, endTime1, isAllday, startAndEndTime);
            System.out.println("---" + event1.toString());
            eventList.add(event1);
        }

    }
}
