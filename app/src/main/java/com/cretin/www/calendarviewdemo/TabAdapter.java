package com.cretin.www.calendarviewdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.cretin.www.calendarviewdemo.views.CalendarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cretin on 2018/3/17.
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CalendarFragment.newInstance(2018, 6);
    }

    @Override
    public int getCount() {
        return 50;
    }
}
