package com.saizad.mvvmexample.di;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.saizad.mvvm.CurrentUserType;
import com.saizad.mvvmexample.MVVMExampleCurrentUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public class AppModule {


    static final int MAX_REQUEST = 3;

    @Singleton
    @Provides
    public CurrentUserType currentUser(SharedPreferences sharedPreferences, Gson gson) {
        return new MVVMExampleCurrentUser(sharedPreferences, gson);
    }

}
















