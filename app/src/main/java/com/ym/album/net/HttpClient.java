package com.ym.album.net;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ym.album.AlbumApp;
import com.ym.album.net.api.IAccountApi;
import com.ym.common_util.config.DebugConfig;
import com.ym.common_util.utils.FileUtil;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 20:49
 */
public class HttpClient<T extends Object> {
    private static final String TAG = "HttpClient";
    private static final int DEFAULT_TIME_OUT = 20;
    private static final int CACHE_SIZE = 10*1024*1024;
    private static final String BASE_URL = "https://localhost:8080";

    private static volatile HttpClient sInstance;

    private T mApi;

    private HttpClient(){
//        throw new IllegalArgumentException("Don't instantiation me!");
    }

    public static HttpClient getInstance(){
        if (sInstance == null){
            synchronized (HttpClient.class){
                if (sInstance == null){
                    sInstance = new HttpClient();
                }
            }
        }
        return sInstance;
    }

    private final Gson gson = new GsonBuilder().serializeNulls().setLenient().create();

    private Cache getCache(){
        String path = FileUtil.getSDCardPath(AlbumApp.getApp(),"HttpCache", DebugConfig.appTag);
        LogUtil.d(TAG,"getCache(): path "+path);
        File httpCachePath = new File(path);
        if (!httpCachePath.exists()){
            httpCachePath.mkdirs();
        }
        return new Cache(httpCachePath,CACHE_SIZE);
    }

    private static final Interceptor CACHE_REWRITE_INTERCEPTOR = new Interceptor() {
        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if (NetWorkUtil.isNetworkAvailable(AlbumApp.getApp())){
                int maxAge=0;
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control","public, max-age="+maxAge)
                        .header("content-type","application/json; Charset=UTF-8")
                        .build();
            }else {
                int maxStale = 20*24*60*60;
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control","public, only-if-cached, max-stale="+maxStale)
                        .header("content-type","application/json; Charset=UTF-8")
                        .build();
            }
        }
    };

    private static final Interceptor getLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //显示日志
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private OkHttpClient getOkHttpClient(){
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS)
                .addNetworkInterceptor(CACHE_REWRITE_INTERCEPTOR)
                .addInterceptor(getLogInterceptor())
                .cache(getCache())
                .build();
    }

    public Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

}
