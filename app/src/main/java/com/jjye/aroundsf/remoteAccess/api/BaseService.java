package com.jjye.aroundsf.remoteAccess.api;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jjye.aroundsf.R;
import com.jjye.aroundsf.app.AroundSFApp;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jjye on 5/30/17.
 */

abstract public class BaseService {
    private Retrofit retrofit;
    BaseService(String restURL) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        retrofit = new Retrofit.Builder()
                .baseUrl(restURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();
    }

    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request oldRequest = chain.request();
                        HttpUrl url = oldRequest.url()
                                .newBuilder()
                                .addQueryParameter("key", AroundSFApp.getInstance().getString(R.string.google_places_api_key))
                                .build();
                        Request newRequest = oldRequest.newBuilder().url(url).build();
                        Log.e("JJJ req", newRequest.url().toString());
                        return chain.proceed(newRequest);
                    }
                }).build();
    }

    Retrofit getRetrofit() {
        return retrofit;
    }
}
