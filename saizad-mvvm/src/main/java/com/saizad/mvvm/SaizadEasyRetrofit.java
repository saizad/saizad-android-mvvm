package com.saizad.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sa.zad.easyretrofit.EasyRetrofit;
import sa.zad.easyretrofit.EasyRetrofitClient;

public class SaizadEasyRetrofit extends EasyRetrofit {

    private final CurrentUser currentUser;
    private final Gson gson;
    private final String domainUrl;

    public SaizadEasyRetrofit(Application application, CurrentUser currentUser, Gson gson, String domainUrl) {
        super(application);
        this.currentUser = currentUser;
        this.gson = gson;
        this.domainUrl = domainUrl;
    }

    @NonNull
    @Override
    public Retrofit.Builder retrofitBuilderReady(@NonNull Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder
                .baseUrl(domainUrl);
    }

    @NonNull
    @Override
    protected Converter.Factory addConverterFactory() {
        return GsonConverterFactory.create(gson);
    }

    @NonNull
    @Override
    protected EasyRetrofitClient easyRetrofitClient() {
        return new SaizadEasyRetrofitClient(provideApplication(), currentUser);
    }

}
