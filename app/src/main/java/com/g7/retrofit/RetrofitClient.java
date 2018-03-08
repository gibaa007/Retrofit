package com.g7.retrofit;

import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by newagesmb on 26/2/18.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    public static Retrofit getClient(String baseUrl) {


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(logging).addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder();
//                builder.header("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfaWQiOiJkNTk4NWY5MTJiNTJiMTJjMGNmYjUyOThkOWMzNzM5MyIsInVzZXJfdHlwZSI6MSwiZmlyc3RfbmFtZSI6IkdpbWJydSIsImxhc3RfbmFtZSI6IlRob21hcyIsImVtYWlsIjoiZ2liaW5AbmV3YWdlc21iLmNvbSIsImhvbWVfb2ZmaWNlX2lkIjoxLCJpbWFnZSI6Imh0dHA6Ly9uZXdhZ2VzbWUuY29tOjMwNDAvdXBsb2Fkcy9tc2xzX3Bob3Rvcy9xZ0tSWVBsTTZmYVVrTEg3T2t0MkZEYWlpaVBKejhaTTA4MmhyaUoyWENRUXhvRkhjTzJMaHBlMWVkcUNUckhILmpwZyIsInBob25lIjoiMTIzNDU2Nzg5MCIsImFjY2VwdF90ZXJtcyI6IlkiLCJpZCI6MiwiaWF0IjoxNTE5ODI0ODY1LCJleHAiOjE1MTk4Mjg0NjV9.DO3c5N4GbbxcC0lToMVvPZSpMNXUvk4uotWcqUKXafM");
                Request newRequest = builder.build();
                Response response = chain.proceed(originalRequest);
                Print.e("Server Request URL : " + newRequest.url().toString());
                //okHttpClient.addInterceptor(logging);
                Print.e("Server Response : " + response.body().string());
                Headers headers = response.headers();
                String accessToken = "", refreshToken = "";
                for (int i = 0; i < headers.size(); i++) {


                    //Login
//                    if (headers.name(i).equalsIgnoreCase("x-access-token")) {
//                        accessToken = headers.get("x-access-token");
//                    }
//                    if (headers.name(i).equalsIgnoreCase("refresh-token")) {
//                        refreshToken = headers.get("x-refresh-token");
//                    }

                    //Other APIS
//                    if (headers.name(i).equalsIgnoreCase("AuthToken")) {
//                        accessToken = headers.get("AuthToken");
//                    }
                    if (headers.name(i).equalsIgnoreCase("authorization")) {
                        if (headers.get("authorization").equalsIgnoreCase("false")) {
                            SingletonHolder.getInstance().setAuthenticated(false);
                            Print.e("authorization : false");
                        } else {
                            SingletonHolder.getInstance().setAuthenticated(true);
                            Print.e("authorization : true");
                            if (headers.get("new-token").equalsIgnoreCase("true")) {
                                Print.e("new token : true");
                                accessToken = headers.get("x-access-token");
                                Print.e("accessToken : " + accessToken);
                                refreshToken = headers.get("refresh-token");
                                Print.e("refreshToken : " + refreshToken);
                                SingletonHolder.getInstance().getEditor().putString("accessToken", accessToken);
                                SingletonHolder.getInstance().getEditor().putString("refreshToken", refreshToken);
                                SingletonHolder.getInstance().getEditor().commit();
                            }
                        }

                    }

                }
                return chain.proceed(newRequest);
            }
        }).build();

//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new Logger() {
//            @Override public void log(String message) {
//                Log.e("Response",message);
//            }
//        });

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
