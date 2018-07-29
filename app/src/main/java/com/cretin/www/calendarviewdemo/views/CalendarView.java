package com.cretin.www.calendarviewdemo.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cretin.www.calendarviewdemo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cretin on 16/9/21.
 */
public class CalendarView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private String[] weeks = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private int firstDay = 1;
    private int firstDayCopy = 1;
    private int currYear;
    private int currMonth;
    private int currDay;
    private int mWeekColor = Color.parseColor("#AEEC8B");
    private int mDaysColor = Color.parseColor("#1d1d26");

    private boolean isFirst = true;

    private OnItemClickListener listener;

    private int defaultWidth = 100;

    private float scaleSize;

    private LinearLayout[][] containers = new LinearLayout[6][7];

    //目标年份 目标月份 和 目标日期
    private int aimYear;
    private int aimMonth;
    private int aimDay;

    //今日年份 今日月份 和 今日日期
    private int tYear;
    private int tMonth;
    private int tDay;

    //创建一个二维数组来存放显示日期的控件
    private TextView[][] days = new TextView[6][7];

    private TextView[][] huikuan = new TextView[6][7];

    //记录存放日期控件的LinearLayout
    private LinearLayout[] layouts = new LinearLayout[6];

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CalendarView(Context context) {
        super(context);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //初始化
    private void init(Context context) {
        mContext = context;
        scaleSize = getScaleSize();
        defaultWidth = (int) (scaleSize * 26);

        setOrientation(VERTICAL);

        initHeadView(mContext);

        initBodyView(mContext);

        putData(dates);
    }

    //填充数据
    private void putData(List<String> dates) {
        int lastMonthLastDay = getLastDayOfMonth(currYear, currMonth);
        int firstDayOfMonth = getFisrtDayOfMonth(currYear, currMonth);
        int daysOfMonth = getDaysOfMonth(currYear, currMonth);
        int index = 1;
        int temp = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == 0) {
                    if (j >= firstDayOfMonth - 1) {
                        days[i][j].setText((firstDay++) + "");

                        //设置回款
                        setHuikuanFlag(dates, days[i][j], i, j);

                        //设置当前选中日期背景
                        if (currYear == aimYear && currMonth == aimMonth && Integer.toString(aimDay).equals(days[i][j].getText().toString())) {
                            days[i][j].setBackgroundResource(R.mipmap.day_bg);
                            days[i][j].setTextColor(Color.parseColor("#79B856"));
                        } else {
                            days[i][j].setTextColor(Color.parseColor("#FFFFFF"));

                            //设置今日时间颜色
                            if (currYear == tYear && currMonth == tMonth && Integer.toString(tDay).equals(days[i][j].getText().toString())) {
                                days[i][j].setTextColor(Color.parseColor("#E9F91F"));
                                if (isFirst) {
                                    days[i][j].setBackgroundResource(R.mipmap.day_bg);
                                    days[i][j].setTextColor(Color.parseColor("#79B856"));
                                } else {
                                    days[i][j].setBackgroundDrawable(null);
                                }
                            }
                        }

                        containers[i][j].setTag(currYear + "-" + currMonth + "-" + days[i][j].getText().toString());
                        temp++;
                    } else {
                        //上个月的日期
                        days[i][j].setText((lastMonthLastDay - firstDayOfMonth + 2 + j) + "");
                        days[i][j].setTextColor(Color.parseColor("#AEEC8B"));
                    }
                } else {
                    if (firstDay <= daysOfMonth) {
                        days[i][j].setText((firstDay++) + "");

                        //设置回款
                        setHuikuanFlag(dates, days[i][j], i, j);

                        //设置当前选中日期背景
                        if (currYear == aimYear && currMonth == aimMonth && Integer.toString(aimDay).equals(days[i][j].getText().toString())) {
                            days[i][j].setBackgroundResource(R.mipmap.day_bg);
                            days[i][j].setTextColor(Color.parseColor("#79B856"));
                        } else {
                            days[i][j].setTextColor(Color.parseColor("#FFFFFF"));

                            //设置今日时间颜色
                            if (currYear == tYear && currMonth == tMonth && Integer.toString(tDay).equals(days[i][j].getText().toString())) {
                                days[i][j].setTextColor(Color.parseColor("#E9F91F"));
                                if (isFirst) {
                                    days[i][j].setBackgroundResource(R.mipmap.day_bg);
                                    days[i][j].setTextColor(Color.parseColor("#79B856"));
                                } else {
                                    days[i][j].setBackgroundDrawable(null);
                                }
                            }
                        }

                        containers[i][j].setTag(currYear + "-" + currMonth + "-" + days[i][j].getText().toString());
                        if (i < 5) {
                            temp++;
                        }
                    } else {
                        //下个月的日期
                        days[i][j].setText((index++) + "");
                        days[i][j].setTextColor(Color.parseColor("#AEEC8B"));
                    }
                }
            }
        }
        if (temp < daysOfMonth) {
            //说明5行并没有显示完 需要显示第6行
            layouts[5].setVisibility(VISIBLE);
        } else {
            //说明5行显示完了 隐藏第6行
            layouts[5].setVisibility(GONE);
        }
    }

    //设置回款的flag
    private void setHuikuanFlag(List<String> dates, TextView textView, int i, int j) {
        //设置回款标识
        if (dates != null && !dates.isEmpty())
            for (String str :
                    dates) {
                int year = Integer.parseInt(str.split("-")[0]);
                int month = Integer.parseInt(str.split("-")[1]);
                int day = Integer.parseInt(str.split("-")[2]);
                if (currYear == year && currMonth == month && Integer.toString(day).equals(textView.getText().toString())) {
                    textView.setBackgroundResource(R.mipmap.quan);
                    if (huikuan[i][j] != null) {
                        huikuan[i][j].setText("回款");
                    }
                }
            }
    }

    //初始化整体布局
    private void initBodyView(Context context) {
        int margin = (int) (10 * scaleSize);
        setPadding(0, 20, 0, 50 - margin);
        //添加日期的数据 6行
        for (int i = 0; i < 6; i++) {
            layouts[i] = new LinearLayout(context);
            layouts[i].setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            layouts[i].setOrientation(HORIZONTAL);

            for (int j = 0; j < 7; j++) {
                containers[i][j] = new LinearLayout(context);
                containers[i][j].setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                containers[i][j].setOrientation(LinearLayout.VERTICAL);
                containers[i][j].setGravity(Gravity.CENTER);

                days[i][j] = new TextView(context);

                LayoutParams lpWeek = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lpWeek.weight = 1;
                lpWeek.setMargins(margin, margin, margin, margin);

                days[i][j].setLayoutParams(new LayoutParams(defaultWidth, defaultWidth));
                days[i][j].setTextColor(mDaysColor);
                days[i][j].setTextSize(14);
                days[i][j].setGravity(Gravity.CENTER);

                containers[i][j].addView(days[i][j]);

                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, (int) (2 * scaleSize), 0, 0);
                huikuan[i][j] = new TextView(context);
                huikuan[i][j].setLayoutParams(layoutParams);
                huikuan[i][j].setTextColor(Color.parseColor("#F8E71C"));
                huikuan[i][j].setTextSize(10);
                huikuan[i][j].setGravity(Gravity.CENTER);

                containers[i][j].addView(huikuan[i][j]);

                containers[i][j].setOnClickListener(this);
                layouts[i].addView(containers[i][j]);
            }
            addView(layouts[i]);
        }
    }

    //初始化头部布局
    private void initHeadView(Context context) {
        //获取当前时间 记录当前年月日保存下来
        Date dt = new Date();
        SimpleDateFormat matter = new SimpleDateFormat("yyyy MM dd");
        if (currYear == 0)
            tYear = currYear = Integer.parseInt(matter.format(dt).split(" ")[0]);
        if (currMonth == 0)
            tMonth = currMonth = Integer.parseInt(matter.format(dt).split(" ")[1]);
        tDay = currDay = Integer.parseInt(matter.format(dt).split(" ")[2]);

        //添加头部的显示星期的布局
        LinearLayout llWeek = new LinearLayout(context);
        llWeek.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llWeek.setOrientation(HORIZONTAL);
        for (int i = 0; i < 7; i++) {
            //把最终显示星期的TextView添加到LinearLayout里面去
            TextView week = new TextView(context);
            LayoutParams lpWeek = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpWeek.weight = 1;
            week.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            week.setText(weeks[i]);
            week.setTextColor(mWeekColor);
            week.setTextSize(14);
            week.setGravity(Gravity.CENTER);
            llWeek.addView(week, lpWeek);
        }
        //将星期添加到视图中
        addView(llWeek);
    }

    //清除数据
    private void clearData() {
        //清除所有状态
        firstDay = 1;
        firstDayCopy = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                days[i][j].setText("");
                containers[i][j].setTag(null);
                days[i][j].setBackgroundDrawable(null);
                days[i][j].setTextColor(mDaysColor);
                huikuan[i][j].setText("");
            }
        }
    }

    public void setData(int year, int month) {
        clearData();
        if (month <= 0 || month > 12)
            throw new RuntimeException("month 数据有误");
        if (year < 1970)
            throw new RuntimeException("year 数据有误");
        currMonth = month;
        currYear = year;
        putData(dates);
    }

    //获取指定年份指定月份的第一天的位置
    private int getFisrtDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    //获取指定年份指定月份的第一天的位置
    private int getDaysOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    //获取指定年份指定月份的最后一天
    public int getLastDayOfMonth(int year, int month) {
        month = month - 1;
        if (month == 0) {
            month = 12;
            year = year - 1;
        }
        month = month - 1;
        if (month == 0) {
            month = 12;
            year = year - 1;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return Integer.parseInt(new SimpleDateFormat("dd").format(cal.getTime()));
    }

    private float getScaleSize() {
        TextView textView = new TextView(mContext);
        textView.setTextSize(1);
        return textView.getTextSize();
    }

    @Override
    public void onClick(View view) {
        if (listener != null && view.getTag() != null) {
            isFirst = false;
            //将目标日期获取用于绘制背景
            String s = view.getTag().toString();
            aimYear = Integer.parseInt(s.split("-")[0]);
            aimMonth = Integer.parseInt(s.split("-")[1]);
            aimDay = Integer.parseInt(s.split("-")[2]);
            clearData();
            putData(dates);
            listener.onItemClick(view, s);
        }
    }

    //数据的格式是 yyyy-MM-dd 如 2016-06-21
    private List<String> dates;

    public void setHuiKuan(List<String> dates) {
        this.dates = dates;
        clearData();
        putData(dates);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String date);
    }
}
