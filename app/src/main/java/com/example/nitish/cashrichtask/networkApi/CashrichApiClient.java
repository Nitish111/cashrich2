package com.example.nitish.cashrichtask.networkApi;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CashrichApiClient {


    public static final String BASE_URL = "http://demo0312305.mockable.io/";//testCashRich

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {

        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static ApiCallMethods getAPICallMethods(Context context) {

        return getClient(context).create(ApiCallMethods.class);

    }


}