package com.vnetoo.test.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/29.
 */

public class RetrofitClient {


    private static final int DEFAULT_TIMEOUT = 5;

    private ApiService apiService;

    private OkHttpClient okHttpClient;

    public static String baseUrl = ApiService.Base_URL;

    private static Context mContext;

    private static RetrofitClient sNewInstance;
    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(
                mContext);
    }

    public static RetrofitClient getInstance(Context context) {
        if (context != null) {
            Log.v("RetrofitClient",  "getInstance");
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }


    public static RetrofitClient getInstance(Context context, String url) {
        if (context != null) {
            mContext = context;
        }
        sNewInstance = new RetrofitClient(context, url);
        return sNewInstance;
    }

    private RetrofitClient(Context context) {

        this(context, null);
    }

    private RetrofitClient(Context context, String url) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
//                .cookieJar(new NovateCookieManger(context))
//                .addInterceptor(new BaseInterceptor(mContext))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public void getData(Subscriber<ResponseBody> subscriber, String ip) {
        apiService.getData(ip)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void get(String url, Map headers, Map parameters, Subscriber<ResponseBody> subscriber) {
        apiService.executeGet(url, headers, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void post(String url, Map headers, Map parameters, Subscriber<ResponseBody> subscriber) {
        apiService.executePost(url, headers, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //大家发现上面的指定生产线程和消费线程的步骤有点麻烦，每个api都得进行指定线程，那么可以利用rxJava的转换器写一个
    // Transformer
    Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {


            @Override
            public Object call(Object observable) {
                return ((Observable)  observable).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    //那么api可以这样优化了：
    public Subscription getData2(Subscriber<ResponseBody> subscriber, String ip) {
        return apiService.getData(ip)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }
}
