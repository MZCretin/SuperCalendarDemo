package com.cretin.www.calendarviewdemo.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cretin.www.calendarviewdemo.R;
import com.cretin.www.calendarviewdemo.SquareTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cretin on 16/9/21.
 */
public class CalendarView extends LinearLayout {
    private Context mContext;
    private String[] weeks = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private int firstDay = 1;
    private int firstDayCopy = 1;
    private int currYear;
    private int currMonth;
    private int currDay;
    private int mTitleColor = Color.parseColor("#071d41");
    private int mWeekColor = Color.parseColor("#AEEC8B");
    private int mDaysColor = Color.parseColor("#1d1d26");

    private int defaultWidth = 100;

    private LinearLayout[][] containers = new LinearLayout[6][7];

    //创建一个二维数组来存放显示日期的控件
    private TextView[][] days = new TextView[6][7];


    private TextView[][] huikuan = new TextView[6][7];

    //记录存放日期控件的LinearLayout
    private LinearLayout[] layouts = new LinearLayout[6];

    //是否初始化
    private boolean initFlag;
    //容器宽度
    private int mPanelWidth;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    //初始化
    private void init(Context context) {
        mContext = context;
        setOrientation(VERTICAL);
    }

    //填充数据
    private void putData() {
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
                        temp++;
                        days[i][j].setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        //上个月的日期
                        days[i][j].setText((lastMonthLastDay - firstDayOfMonth + 2 + j) + "");
                        days[i][j].setTextColor(Color.parseColor("#AEEC8B"));
                    }
                } else {
                    if (firstDay <= daysOfMonth) {
                        days[i][j].setText((firstDay++) + "");
                        days[i][j].setTextColor(Color.parseColor("#FFFFFF"));
                        if (i < 5) {
                            temp++;
                        }
                    } else {
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
        drawOval();
    }

    private void drawOval() {
        int firstDayOfMonth = getFisrtDayOfMonth(currYear, currMonth);
        int daysOfMonth = getDaysOfMonth(currYear, currMonth);

        Date dt = new Date();
        SimpleDateFormat matter = new SimpleDateFormat("yyyy MM dd");
        int year = Integer.parseInt(matter.format(dt).split(" ")[0]);
        int month = Integer.parseInt(matter.format(dt).split(" ")[1]);
        int day = Integer.parseInt(matter.format(dt).split(" ")[2]);

        if(currYear == year && month == currMonth) {
            HH:
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    if (i == 0) {
                        if (j >= firstDayOfMonth - 1) {
                            if (firstDayCopy == day && month == currMonth && year == currYear) {
                                days[i][j].setBackgroundResource(R.mipmap.day_bg);
                                days[i][j].setTextColor(Color.WHITE);
                                firstDayCopy = 1;
                                break HH;
                            }
                            firstDayCopy++;
                        }
                    } else {
                        if (firstDayCopy <= daysOfMonth) {
                            if (firstDayCopy == day && month == currMonth && year == currYear) {
                                days[i][j].setBackgroundResource(R.mipmap.day_bg);
                                days[i][j].setTextColor(Color.WHITE);
                                firstDayCopy = 1;
                                break HH;
                            }
                            firstDayCopy++;
                        }
                    }
                }
            }
        }
    }

    //初始化整体布局
    private void initBodyView(Context context) {
        int margin = (mPanelWidth - 7 * defaultWidth) / 14;
        setPadding(0, 20, 0, 50 - margin);
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

                LayoutParams lpWeek = new LayoutParams(defaultWidth, defaultWidth);
                lpWeek.weight = 1;
                lpWeek.setMargins(margin, margin, margin, margin);

                days[i][j].setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                days[i][j].setTextColor(mDaysColor);
                days[i][j].setTextSize(14);
                days[i][j].setGravity(Gravity.CENTER);
                days[i][j].setBackgroundResource(R.mipmap.day_bg);

                containers[i][j].addView(days[i][j]);

                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 0);
                huikuan[i][j] = new TextView(context);
                huikuan[i][j].setLayoutParams(layoutParams);
                huikuan[i][j].setTextColor(Color.parseColor("#F8E71C"));
                huikuan[i][j].setTextSize(10);
                huikuan[i][j].setGravity(Gravity.CENTER);
                huikuan[i][j].setText("回款");

                containers[i][j].addView(huikuan[i][j]);

                layouts[i].addView(containers[i][j]);
            }
            addView(layouts[i]);
        }
        initFlag = true;
    }

    //初始化头部布局
    private void initHeadView(Context context) {
        //获取当前时间
        Date dt = new Date();
        SimpleDateFormat matter = new SimpleDateFormat("yyyy MM dd");
        if (currYear == 0)
            currYear = Integer.parseInt(matter.format(dt).split(" ")[0]);
        if (currMonth == 0)
            currMonth = Integer.parseInt(matter.format(dt).split(" ")[1]);
        currDay = Integer.parseInt(matter.format(dt).split(" ")[2]);

//        int margin = (mPanelWidth - 7 * defaultWidth) / 14;
//        LinearLayout llHead = new LinearLayout(context);
//        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.gravity = Gravity.CENTER;
//        llHead.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        ivLeft = new ImageView(context);
//        ivLeft.setImageResource(R.mipmap.calendar_left);
//        ivLeft.setPadding(margin + 35, 10, margin + 35, 10);
//        llHead.addView(ivLeft, lp);
//        time = new TextView(context);
//        LayoutParams lpTv = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lpTv.weight = 1;
//        time.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        time.setTextColor(mTitleColor);
//        time.setTextSize(16);
//        time.setGravity(Gravity.CENTER);
//        llHead.addView(time, lpTv);
//        ivRight = new ImageView(context);
//        ivRight.setImageResource(R.mipmap.calendar_right);
//        ivRight.setPadding(margin + 35, 10, margin + 35, 10);
//        llHead.addView(ivRight, lp);
//        addView(llHead);

        LinearLayout llWeek = new LinearLayout(context);
        llWeek.setPadding(0, 0, 0, 0);
        llWeek.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llWeek.setOrientation(HORIZONTAL);
        for (int i = 0; i < 7; i++) {
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
        addView(llWeek);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPanelWidth == 0) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);

            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            int width = Math.min(widthSize, heightSize);
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                width = heightSize;
            } else if (heightMode == MeasureSpec.UNSPECIFIED) {
                width = widthSize;
            }
            mPanelWidth = width;

            initHeadView(mContext);

            initBodyView(mContext);

            putData();

//            ivLeft.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    backMonth();
//                }
//            });
//
//            ivRight.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    preMonth();
//                }
//            });
        }
    }


//    //开启手势
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
//                int x = (int) event.getX();
//                int y = (int) event.getY();
//                if (Math.abs(x - currX) > Math.abs(y - currY) + 50) {
//                    //在水平方向上
//                    if (x > currX) {
//                        //右滑动
//                        backMonth();
//                    } else {
//                        //左滑动
//                        preMonth();
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_DOWN:
//                currX = (int) event.getX();
//                currY = (int) event.getY();
//                break;
//        }
//        return true;
//    }

    //往前计算一个月
    private void backMonth() {
        clearData();
        if (currMonth != 1) {
            currMonth--;
        } else {
            currYear--;
            currMonth = 12;
        }
        putData();
    }

    //清除数据
    private void clearData() {
        firstDay = 1;
        firstDayCopy = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                days[i][j].setText("");
                days[i][j].setBackgroundDrawable(null);
                days[i][j].setTextColor(mDaysColor);
            }
        }
    }

    //往后计算一个月
    private void preMonth() {
        clearData();
        if (currMonth != 12) {
            currMonth++;
        } else {
            currYear++;
            currMonth = 1;
        }
        putData();
    }

    public void setInitData(int year, int month) {
        if (month <= 0 || month > 12)
            throw new RuntimeException("month 数据有误");
        if (year < 1970)
            throw new RuntimeException("year 数据有误");
        currMonth = month;
        currYear = year;
    }

    public void setData(int year, int month) {
        if (initFlag) {
            clearData();
            if (month <= 0 || month > 12)
                throw new RuntimeException("month 数据有误");
            if (year < 1970)
                throw new RuntimeException("year 数据有误");
            currMonth = month;
            currYear = year;
            putData();
        }
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
}
