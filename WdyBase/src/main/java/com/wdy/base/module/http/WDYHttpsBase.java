package com.wdy.base.module.http;

import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者：王东一
 * 创建时间：2019-05-24.
 */
public class WDYHttpsBase {
    private OkHttpClient okHttpClient;
    private Gson gson;
    private volatile static WDYHttpsBase wdyHttpBase;

    public WDYHttpsBase() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(WDYHttpConfig.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(WDYHttpConfig.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(WDYHttpConfig.writeTimeout, TimeUnit.SECONDS)
                .sslSocketFactory(HttpsTrustManager.createSSLSocketFactory())
                .hostnameVerifier(new HttpsTrustManager.TrustAllHostnameVerifier())
                .build();
        gson = new Gson();
    }

    public static WDYHttpsBase getInstance() {
        if (wdyHttpBase == null) {
            synchronized (WDYHttpsBase.class) {
                if (wdyHttpBase == null) {
                    wdyHttpBase = new WDYHttpsBase();
                }
            }
        }
        return wdyHttpBase;
    }

    public void getEasyAsyncHttp(String url, final WDYHttpsBase.OnWDYEasyHttpCallback onWDYEasyHttpCallback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onWDYEasyHttpCallback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull final Call call, @NonNull final Response response) throws IOException {
                assert response.body() != null;
                final String json = response.body().string();
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onWDYEasyHttpCallback.onResponse(gson.fromJson(json, onWDYEasyHttpCallback.mType), json);
                    }
                });
            }
        });
    }

    public void postEasyAsyncHttp(Request request, final WDYHttpsBase.OnWDYEasyHttpCallback onWDYEasyHttpCallback) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onWDYEasyHttpCallback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull final Call call, @NonNull final Response response) throws IOException {
                assert response.body() != null;
                final String json = response.body().string();
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onWDYEasyHttpCallback.onResponse(gson.fromJson(json, onWDYEasyHttpCallback.mType), json);
                    }
                });
            }
        });
    }

    public void AsyncHttp(Request request, final WDYHttpsBase.OnWDYHttpCallback onWDYHttpCallback) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onWDYHttpCallback.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull final Call call, @NonNull final Response response) throws IOException {
                assert response.body() != null;
                final String json = response.body().string();
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onWDYHttpCallback.onResponse(call, gson.fromJson(json, onWDYHttpCallback.mType), json);
                    }
                });
            }
        });
    }

    public static abstract class OnWDYHttpCallback<T> {
        Type mType;


        public OnWDYHttpCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }


        static Type getSuperclassTypeParameter(Class<?> subclass) {
            //通过反射得到泛型参数
            //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            //ParameterizedType参数化类型，即泛型
            ParameterizedType parameterized = (ParameterizedType) superclass;
            //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
            //将Java 中的Type实现,转化为自己内部的数据实现,得到gson解析需要的泛型
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onFailure(Call call, IOException e);

        public abstract void onResponse(Call call, T response, String json);

    }

    public static abstract class OnWDYEasyHttpCallback<T> {
        Type mType;


        public OnWDYEasyHttpCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }


        static Type getSuperclassTypeParameter(Class<?> subclass) {
            //通过反射得到泛型参数
            //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            //ParameterizedType参数化类型，即泛型
            ParameterizedType parameterized = (ParameterizedType) superclass;
            //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
            //将Java 中的Type实现,转化为自己内部的数据实现,得到gson解析需要的泛型
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onFailure(IOException e);

        public abstract void onResponse(T response, String json);

    }
}
