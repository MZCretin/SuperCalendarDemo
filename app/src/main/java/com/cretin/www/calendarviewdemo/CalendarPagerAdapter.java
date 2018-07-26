package com.cretin.www.calendarviewdemo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.cretin.www.calendarviewdemo.views.CalendarView;

import java.util.LinkedList;

public class CalendarPagerAdapter extends PagerAdapter {

    //缓存上一次回收的MonthView
    private LinkedList<CalendarView> cache = new LinkedList<>();
    private SparseArray<CalendarView> mViews = new SparseArray<>();

    private Context context;

    private int count;

    public CalendarPagerAdapter(int count, Context context) {
        this.count = count;
        this.context = context;
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
        final CalendarView view;
        if (!cache.isEmpty()) {
            view = cache.removeFirst();
            int[] yearAndMonth = getYearAndMonth(position);
            view.setData(yearAndMonth[0], yearAndMonth[1]);
            Log.e("HHHHHHH", "gangal");
        } else {
            view = new CalendarView(container.getContext());
            view.setBackgroundResource(R.drawable.bg);
            int[] yearAndMonth = getYearAndMonth(position);
            view.setInitData(yearAndMonth[0], yearAndMonth[1]);
            Log.e("HHHHHHH", "gangal1");
        }
        mViews.put(position, view);
        container.addView(view);
        return view;
    }

    int startYear = 2014;
    int startMonth = 5;
    int afterMonth = 36;

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
        mViews.remove(position);
    }

    /**
     * 获得ViewPager缓存的View
     *
     * @return
     */
    public SparseArray<CalendarView> getViews() {
        return mViews;
    }
}
