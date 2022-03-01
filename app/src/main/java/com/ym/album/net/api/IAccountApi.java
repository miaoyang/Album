package com.ym.album.net.api;

import androidx.annotation.NonNull;

import com.ym.album.net.bean.BaseBean;
import com.ym.album.room.model.UserInfo;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/1 10:43
 */
public interface IAccountApi {

    @POST("/account/login")
    Observable<BaseBean<UserInfo>> login(@Query("username")String username,
                                         @Query("password")String password);
}
