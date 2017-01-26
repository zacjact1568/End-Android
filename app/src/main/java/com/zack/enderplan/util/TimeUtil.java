package com.zack.enderplan.util;

import android.text.format.DateFormat;

import com.zack.enderplan.App;
import com.zack.enderplan.R;
import com.zack.enderplan.common.Constant;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static boolean isValidDateTimePickerTime(long timeInMillis) {
        return timeInMillis == Constant.UNDEFINED_TIME || timeInMillis > System.currentTimeMillis();
    }

    public static long getDefaultDateTimePickerTime(long timeInMillis) {
        if (timeInMillis == Constant.UNDEFINED_TIME) {
            Calendar calendar = Calendar.getInstance();
            //TODO 延后的时间可以自定义
            calendar.add(Calendar.MINUTE, 1);
            timeInMillis = calendar.getTimeInMillis();
        }
        return timeInMillis;
    }

    public static boolean is24HourFormat() {
        return DateFormat.is24HourFormat(App.getContext());
    }

    public static String formatTime(long timeInMillis) {
        if (timeInMillis == Constant.UNDEFINED_TIME) {
            return null;
        }
        Date date = new Date(timeInMillis);
        String dateStr = DateFormat.getMediumDateFormat(App.getContext()).format(date);
        String timeStr = DateFormat.getTimeFormat(App.getContext()).format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Calendar standardCal = Calendar.getInstance();
        if (calendar.after(standardCal)) {
            //标准时间设为1小时后
            standardCal.add(Calendar.HOUR_OF_DAY, 1);
            if (calendar.before(standardCal)) {
                //1小时以内
                int minuteCount = (int) (calendar.getTimeInMillis() - System.currentTimeMillis()) / (1000 * 60);
                if (minuteCount == 0) {
                    return ResourceUtil.getString(R.string.dscpt_time_within_1_minute);
                } else if (minuteCount == 1) {
                    return ResourceUtil.getString(R.string.dscpt_time_1_minute);
                } else {
                    return String.format(ResourceUtil.getString(R.string.dscpt_time_multi_minutes), minuteCount);
                }
            }
            //不在1小时之内，还原时间
            standardCal.add(Calendar.HOUR_OF_DAY, -1);
        }
        //标准时间设为昨天零点
        standardCal.add(Calendar.DAY_OF_MONTH, -1);
        standardCal.set(Calendar.HOUR_OF_DAY, 0);
        standardCal.set(Calendar.MINUTE, 0);
        standardCal.set(Calendar.SECOND, 0);
        standardCal.set(Calendar.MILLISECOND, 0);
        //比较
        if (calendar.after(standardCal)) {
            //标准时间设为今天零点
            standardCal.add(Calendar.DAY_OF_MONTH, 1);
            //比较
            if (calendar.before(standardCal)) {
                //在昨天
                return String.format(ResourceUtil.getString(R.string.dscpt_time_yesterday), timeStr);
            }
            //标准时间设为明天零点
            standardCal.add(Calendar.DAY_OF_MONTH, 1);
            //比较
            if (calendar.before(standardCal)) {
                //在今天
                return String.format(ResourceUtil.getString(R.string.dscpt_time_today), timeStr);
            }
            //标准时间设为后天零点
            standardCal.add(Calendar.DAY_OF_MONTH, 1);
            //比较
            if (calendar.before(standardCal)) {
                //在明天
                return String.format(ResourceUtil.getString(R.string.dscpt_time_tomorrow), timeStr);
            }
            //在后天以及以后，不需要时间描述
        }
        //在前天以及以前，不需要时间描述
        //TODO 添加星期显示
        return String.format(ResourceUtil.getString(R.string.dscpt_time_general), dateStr, timeStr);
    }
}