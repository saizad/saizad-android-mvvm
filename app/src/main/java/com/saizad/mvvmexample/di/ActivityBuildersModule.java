package com.saizad.mvvmexample.di;

import com.saizad.mvvm.di.AuthScope;
import com.saizad.mvvm.di.MainScope;
import com.saizad.mvvmexample.components.auth.AuthActivity;
import com.saizad.mvvmexample.components.main.DrawerMainActivity;
import com.saizad.mvvmexample.di.auth.AuthFragmentBuildersModule;
import com.saizad.mvvmexample.di.auth.AuthModule;
import com.saizad.mvvmexample.di.auth.AuthViewModelsModule;
import com.saizad.mvvmexample.di.main.MainFragmentBuildersModule;
import com.saizad.mvvmexample.di.main.MainModule;
import com.saizad.mvvmexample.di.main.MainViewModelsModule;
import com.saizad.mvvmexample.service.FCMService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
            modules = {AuthFragmentBuildersModule.class, AuthViewModelsModule.class, AuthModule.class})
    abstract AuthActivity authActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {MainFragmentBuildersModule.class, MainViewModelsModule.class, MainModule.class}
    )
    abstract DrawerMainActivity mainActivity();

    @ContributesAndroidInjector
    public abstract FCMService fcmService();
}
