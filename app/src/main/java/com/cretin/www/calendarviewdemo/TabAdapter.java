package com.cretin.www.calendarviewdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cretin.www.calendarviewdemo.views.CalendarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cretin on 2018/3/17.
 */
public class TabAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

    public TabAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(CalendarFragment.newInstance(2018, 6));
        fragments.add(CalendarFragment.newInstance(2018, 7));
        fragments.add(CalendarFragment.newInstance(2018, 8));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
