package com.ym.album.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ym.album.AlbumApp;
import com.ym.album.room.dao.UserDao;
import com.ym.album.room.model.UserInfo;

@Database(entities = {UserInfo.class}, version = 1,exportSchema = false)
@TypeConverters({TypeFactory.class})
public abstract class AppDataBase extends RoomDatabase {
    private static final String TAG = "AppDataBase";
    private static final String ALBUM_DATABASE_NAME = "album_data.db";
    private static volatile AppDataBase sInstance;

    public abstract UserDao userDao();

    public static AppDataBase getInstance(){
        if (sInstance == null){
            synchronized (AppDataBase.class){
                if (sInstance == null){
                    sInstance = Room.databaseBuilder(AlbumApp.getApp().getApplicationContext(),
                            AppDataBase.class,ALBUM_DATABASE_NAME)
                            .build();
                }
            }
        }
        return sInstance;
    }
}
