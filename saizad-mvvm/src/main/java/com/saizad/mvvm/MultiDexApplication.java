package com.saizad.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.multidex.MultiDex;

public abstract class MultiDexApplication extends Application {

    @Override
    @CallSuper
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

}

