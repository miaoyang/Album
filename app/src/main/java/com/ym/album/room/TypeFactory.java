package com.ym.album.room;

import androidx.room.TypeConverter;

import java.sql.Date;

/**
 * Author:yangmiao
 * Desc:
 * Time:2022/1/27 11:40
 */
public class TypeFactory {
    @TypeConverter
    public static Long date2Long(Date date){
        return date==null?null:date.getTime();
    }

    @TypeConverter
    public static Date long2Date(Long time){
        return time==null?null:new Date(time);
    }
}
