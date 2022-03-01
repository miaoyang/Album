package com.ym.common_util.utils;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    private static final String TAG = TimeUtil.class.getSimpleName();
    public static String dateToStr(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date strToDate(String dateStr,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.e(TAG,"strToDate error ",e);
        }
        return date;
    }
}
