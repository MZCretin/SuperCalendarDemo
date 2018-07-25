package com.cretin.www.calendarviewdemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cretin.www.calendarviewdemo.views.CalendarView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
    private CalendarView calendarView;

    public static CalendarFragment newInstance(int year, int month) {
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        if (getArguments() != null) {
            int year = getArguments().getInt("year", 0);
            int month = getArguments().getInt("month", 0);
            calendarView.setInitData(year, month);
        }
        return view;
    }

    //设置数据
    public void setData(int year, int month) {
        calendarView.setData(year, month);
    }
}
