package com.cretin.www.calendarviewdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.www.calendarviewdemo.adapter.CalendarPagerAdapter;
import com.cretin.www.calendarviewdemo.views.CalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private RecyclerView recyclerview;

    //控制ViewPager的适配器
    private CalendarPagerAdapter adapter;

    //记录当前页面的position
    private int currPosotion;

    private LinearLayoutManager linearLayoutManager;

    //显示月份的RecyclerView的适配器
    private RecyclerviewAdapter recyclerAdapter;

    //记录当前显示月份的RecyclerView的宽度
    private int recyclerViewWidth;

    //展示年份的控件
    private TextView tvYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化视图
        initView();

        /**
         * 获取总月数
         */
        //startYear, startMonth, afterMonth, allMonth, allMonth - cMonth
        final int[] months = calcuMonth();

        initViewPager(months);

        initRecyclerView(months);
    }

    //初始化RecyclerView
    private void initRecyclerView(final int[] months) {
        /**
         * 获取到数据计算出对应的月份 我们的需求是从2014-5到现在往后的36个月 计算出所有的月份范围添加到list中
         *
         * 所以这一块要根据你自己的需求
         */
        final List<String> list = new ArrayList<>();
        //startYear, startMonth, afterMonth, allMonth, allMonth - cMonth
        int m = months[1];
        for (int i = 0; i < months[3]; i++) {
            //如果是12月份 直接设置成12月 其他直接设置余数即可
            if ((m + i) % 12 == 0) {
                list.add("12月");
            } else {
                list.add(((m + i) % 12) + "月");
            }
        }

        //计算Recycler的宽度 主要为了设置item的宽度，保证5个item正好撑满RecyclerView
        ViewTreeObserver vto = recyclerview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onGlobalLayout() {
                recyclerview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                recyclerViewWidth = recyclerview.getMeasuredWidth();

                //初始化适配器
                recyclerAdapter = new RecyclerviewAdapter(MainActivity.this, list);
                linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                //设置布局为横向
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerview.setLayoutManager(linearLayoutManager);

                recyclerview.setHasFixedSize(true);
                recyclerview.setNestedScrollingEnabled(false);

                //设置当前currPosotion为当前月份
                currPosotion = months[3] - months[2];
                recyclerview.setAdapter(recyclerAdapter);

                //通知handler更新RecyclerView的位置
                handler.sendEmptyMessage(0);
            }
        });
    }

    //初始化ViewPager的数据
    private void initViewPager(int[] months) {
        //实例化适配器
        adapter = new CalendarPagerAdapter(months[3], new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String date) {
                /**
                 * 这里暂时打印出来 实际效果应该是点击后会有对应请求数据的操作 好用来获取当天的相关数据
                 */
                Toast.makeText(MainActivity.this, date, Toast.LENGTH_SHORT).show();
            }
        });
        viewPager.setAdapter(adapter);
        //设置currentItem下标为当前月
        viewPager.setCurrentItem(months[3] - months[2]);

        //添加滑动监听 主要是为了在每次滑动完了之后主动滑动RecyclerView来实现联动
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int top = 0;
                //获取可视的第一个item下标
                int pFirst = linearLayoutManager.findFirstVisibleItemPosition();
                //获取可视的第一个View
                View viewByPosition = linearLayoutManager.findViewByPosition(pFirst);
                //获取可视的第一个View的left 作为滑动的依据
                if (viewByPosition != null)
                    top = viewByPosition.getLeft();
                //获取每一个Item的宽度 我们默认一屏显示5条 所以除以5
                float itemViewHeight = recyclerViewWidth / 5;
                //计算需要滑动的Item的数量 这个自己比划算一算
                int needScrollPostion = position - pFirst - 3;
                //计算最终需要滑动的距离 为负数就是向相反方向滑动
                int distance = (int) (needScrollPostion * itemViewHeight + (itemViewHeight - Math.abs(top)));
                //开始滑动
                recyclerview.smoothScrollBy(distance, 10);
                //更新当前下标的数据
                currPosotion = position;
                //刷新适配器 改变被选择的item的文字的颜色
                recyclerAdapter.notifyDataSetChanged();

                refreshYeardata();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //初始化控件
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        tvYear = (TextView) findViewById(R.id.tv_year);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //刚开始计算完高度的时候 要滑动到当前月的日历
            linearLayoutManager.scrollToPosition(currPosotion + 2);
            //更新年份数据
            refreshYeardata();
        }
    };

    //更新年份数据
    private void refreshYeardata() {
        int[] yearAndMonth = adapter.getYearAndMonth(currPosotion);
        tvYear.setText(yearAndMonth[0] + "");

        setHuikuanData();
    }

    //设置回款数据
    private void setHuikuanData() {
        //要设置回款的数据就调这个方法 模拟的数据
        List<String> datas = new ArrayList<>();
        datas.add("2018-07-05");
        datas.add("2018-07-08");
        datas.add("2018-07-12");
        datas.add("2018-07-17");
        adapter.setHuiKuan(datas);
    }

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
                //设置itemView的宽度为RecyclerView的宽度的五分之一
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
