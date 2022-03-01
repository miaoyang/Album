package com.ym.album.net;

import com.ym.album.net.api.IAccountApi;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/1 11:12
 */
public class ApiModule {
    private static volatile ApiModule sInstance;
    private ApiModule(){}

    private IAccountApi mAccountApi;

    public static ApiModule getInstance(){
        if (sInstance == null){
            synchronized (ApiModule.class){
                if (sInstance == null){
                    sInstance = new ApiModule();
                }
            }
        }
        return sInstance;
    }

    /**
     * 防止多次初始化，节约资源
     * @return
     */
    public IAccountApi providerAccountService(){
        if (mAccountApi == null){
            synchronized (IAccountApi.class){
                if (mAccountApi == null){
                    mAccountApi = HttpClient.getInstance().getRetrofit().create(IAccountApi.class);
                }
            }
        }
        return mAccountApi;
    }


}
