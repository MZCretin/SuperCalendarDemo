package com.cretin.www.calendarviewdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private CalendarView calendarView;

    private ViewPager viewPager;

    private TabAdapter adapter;

    private int currentItem;

    private int count = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(new CalendarFragment());
        }
        adapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                currentItem = viewPager.getCurrentItem();
                switch (state) {
                    case 0://No operation
                        if (currentItem == 0) {
                            viewPager.setCurrentItem(count - 1, false);
                        } else if (currentItem == count - 1) {
                            viewPager.setCurrentItem(1, false);
                        }
                        break;
                    case 1://start Sliding
                        if (currentItem == count - 1) {
                            viewPager.setCurrentItem(1, false);
                        } else if (currentItem == 0) {
                            viewPager.setCurrentItem(count - 1, false);
                        }
                        break;
                    case 2://end Sliding
                        break;
                }
            }
        });
    }

}
