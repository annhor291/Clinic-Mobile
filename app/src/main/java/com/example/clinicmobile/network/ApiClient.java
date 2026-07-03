package com.example.clinicmobile.network;

import android.content.Context;

import com.example.clinicmobile.utils.TokenManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // ⚠️ Đổi thành IP máy tính của bạn khi test trên máy thật
    // Dùng 10.0.2.2 cho Android Emulator
    private static final String BASE_URL = "http://10.0.2.2:8080/api/v1/";

    private static Retrofit retrofit = null;
    private static Context appContext;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Logging interceptor (chỉ dùng khi debug)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Auth interceptor — tự động thêm Bearer token vào mọi request
            Interceptor authInterceptor = chain -> {
                Request original = chain.request();
                String token = TokenManager.getAccessToken(appContext);

                Request request;
                if (token != null) {
                    request = original.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .build();
                } else {
                    request = original;
                }
                return chain.proceed(request);
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }

    // Reset khi cần (ví dụ sau khi refresh token)
    public static void reset() {
        retrofit = null;
    }
}
