package com.cretin.www.calendarviewdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.cretin.www.calendarviewdemo.views.CalendarView;

import java.util.LinkedList;

public class CalendarPagerAdapter extends PagerAdapter {

    //缓存上一次回收的MonthView
    private LinkedList<CalendarView> cache = new LinkedList<>();
    private SparseArray<CalendarView> mViews = new SparseArray<>();

    private Context context;

    private int count;

    public CalendarPagerAdapter(int count,Context context) {
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
            view.setData(2018,position+1);
        } else {
            view = new CalendarView(container.getContext());
            view.setBackgroundResource(R.drawable.bg);
            view.setInitData(2018,position+1);
        }
        mViews.put(position, view);
        container.addView(view);
        return view;
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
