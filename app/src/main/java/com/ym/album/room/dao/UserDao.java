package com.ym.album.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ym.album.room.model.UserInfo;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserInfo(UserInfo userInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListUserInfo(List<UserInfo> userInfoList);

    @Query("delete from t_user where user_name=:userName")
    void deleteByUserName(String userName);

    @Delete
    void deleteByUserInfo(UserInfo userInfo);

    @Delete
    void deleteListUserInfo(List<UserInfo> userInfoList);

    @Update
    void updateUserInfo(UserInfo userInfo);

    @Query("select * from t_user")
    List<UserInfo> selectAllUserInfo();

    @Query("select * from t_user where user_name=:userName")
    UserInfo selectByUsername(String userName);

    @Query("select * from t_user where telephone=:tel")
    UserInfo selectByTelephone(String tel);

}
