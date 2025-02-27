package com.dt170g.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private final ApiService api;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // Ensure this matches your backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    // ✅ Fix: Properly return the API instance
    public static ApiService getApiService() {
        return getInstance().api;
    }

    public ApiService getApi() {
        return api;
    }
}
