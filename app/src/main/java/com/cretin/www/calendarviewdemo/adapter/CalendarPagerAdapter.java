package com.cretin.www.calendarviewdemo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.cretin.www.calendarviewdemo.views.CalendarView;
import java.util.LinkedList;
import java.util.List;

public class CalendarPagerAdapter extends PagerAdapter {

    //缓存上一次回收的MonthView
    private LinkedList<CalendarView> cache = new LinkedList<>();

    //记录当前展示的View
    private CalendarView currView;

    //记录需要展示的数量
    private int count;

    //初始化事件监听
    private CalendarView.OnItemClickListener listener;

    public CalendarPagerAdapter(int count, CalendarView.OnItemClickListener listener) {
        this.count = count;
        this.listener = listener;
    }

    public void setHuiKuan(List<String> dates) {
        if (currView != null)
            currView.setHuiKuan(dates);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currView = (CalendarView) object;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //尽可能复用View
        final CalendarView view;
        if (!cache.isEmpty()) {
            view = cache.removeFirst();
            //根据position获取当前页面需要展示的年份和月份
            int[] yearAndMonth = getYearAndMonth(position);
            view.setData(yearAndMonth[0], yearAndMonth[1]);
        } else {
            view = new CalendarView(container.getContext());
            //根据position获取当前页面需要展示的年份和月份
            int[] yearAndMonth = getYearAndMonth(position);
            view.setData(yearAndMonth[0], yearAndMonth[1]);
        }
        if (listener != null)
            view.setListener(listener);
        container.addView(view);
        return view;
    }

    //记录开始的年份 我们这里是从2014-5 到 这个月后面的36个月
    int startYear = 2014;
    int startMonth = 5;
    int afterMonth = 36;

    //根据position获取到此页面需要展示年月份的数据
    public int[] getYearAndMonth(int position) {
        int cMonth = startMonth + position;
        int cYear = startYear + cMonth / 12;

        if (cMonth % 12 == 1) {
            //增加一年
            cMonth = 1;
        } else if (cMonth % 12 == 0) {
            //正好12月
            cMonth = 12;
            cYear--;
        } else {
            cMonth = cMonth % 12;
        }
        return new int[]{cYear, cMonth};
    }

    //根据年月反推position
    public int getPosition(int[] yearAndMonth) {
        int year = yearAndMonth[0];
        int month = yearAndMonth[1];
        //计算需要展示的所有月数
        if (year == startYear) {
            if (month > startMonth) {
                return month - startMonth;
            } else {
                return 0;
            }
        }
        return (year - startYear - 1) * 12 + (12 - startMonth) + month;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CalendarView) object);
        cache.addLast((CalendarView) object);
    }
}
