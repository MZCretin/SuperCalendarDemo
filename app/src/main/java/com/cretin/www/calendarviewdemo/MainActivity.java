package com.cretin.www.calendarviewdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;

    private RecyclerView recyclerview;

    private CalendarPagerAdapter adapter;

    private int currPosotion;

    private LinearLayoutManager linearLayoutManager;

    private TextView tvYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        tvYear = (TextView) findViewById(R.id.tv_year);

        final List<String> list = new ArrayList<>();
        //startYear, startMonth, afterMonth, allMonth, allMonth - cMonth
        final int[] months = calcuMonth();
        int m = months[1];
        for (int i = 0; i < months[3]; i++) {
            if ((m + i) % 12 == 1) {
                months[0]++;
                list.add(Integer.toString(months[0]).substring(2, 4) + "年" + ((m + i) % 12) + "月");
            } else if ((m + i) % 12 == 0) {
                list.add("12月");
            } else {
                list.add(((m + i) % 12) + "月");
            }
        }

        adapter = new CalendarPagerAdapter(months[3], this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(months[3] - months[2]);
        viewPager.setOffscreenPageLimit(5);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position1) {
                int top = 0;
                int pisition = linearLayoutManager.findFirstVisibleItemPosition();
                View viewByPosition = linearLayoutManager.findViewByPosition(pisition);
                if (viewByPosition != null)
                    top = viewByPosition.getLeft();
                float itemViewHeight = recyclerViewWidth / 5;
                int needScrollPostion = position1 - pisition - 3;
                int distance = (int) (needScrollPostion * itemViewHeight + (itemViewHeight - Math.abs(top)));
                recyclerview.smoothScrollBy(distance, 10);
                currPosotion = position1;
                adapterRecycler.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        ViewTreeObserver vto = recyclerview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onGlobalLayout() {
                recyclerview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                recyclerViewWidth = recyclerview.getMeasuredWidth();

                adapterRecycler = new RecyclerviewAdapter(MainActivity.this, list);
                linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerview.setLayoutManager(linearLayoutManager);

                recyclerview.setHasFixedSize(true);
                recyclerview.setNestedScrollingEnabled(false);

                currPosotion = months[3] - months[2];
                recyclerview.setAdapter(adapterRecycler);
            }
        });
    }

    private RecyclerviewAdapter adapterRecycler;

    private int recyclerViewWidth;

    public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
        private Context context;
        private List<String> data;

        public RecyclerviewAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.name.setText(data.get(position));
            if (position == currPosotion) {
                holder.name.setTextColor(Color.parseColor("#6CAB3D"));
            } else {
                holder.name.setTextColor(Color.parseColor("#BEBEBE"));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currPosotion = position;
                    viewPager.setCurrentItem(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setLayoutParams(new ViewGroup.LayoutParams(recyclerViewWidth / 5, ViewGroup.LayoutParams.MATCH_PARENT));
                name = (TextView) itemView.findViewById(R.id.month);
            }
        }
    }

    //计算月数总数
    private int[] calcuMonth() {
        //2014-5 到 本月往后36个月
        int startYear = 2014;
        int startMonth = 5;
        int afterMonth = 36;
        Date dt = new Date();
        SimpleDateFormat matter = new SimpleDateFormat("yyyy MM dd");
        int cYear = Integer.parseInt(matter.format(dt).split(" ")[0]);
        int cMonth = Integer.parseInt(matter.format(dt).split(" ")[1]);

        //计算需要展示的所有月数
        int allMonth = (cYear - startYear - 1) * 12 + (12 - startMonth) + cMonth + afterMonth;
        return new int[]{startYear, startMonth, afterMonth, allMonth, allMonth - cMonth};
    }
}
