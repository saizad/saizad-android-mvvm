package com.saizad.mvvmexample.components;

import androidx.annotation.NonNull;

import com.saizad.mvvm.CurrentUserType;
import com.saizad.mvvm.Environment;
import com.saizad.mvvm.SaizadLocation;
import com.saizad.mvvm.pager.BasePage;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

abstract public class MVVMExamplePageFragment<VM extends MVVMExampleViewModel> extends BasePage<VM> {

    @Inject
    public Environment mainEnvironment;

//    @Inject
//    public ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    SaizadLocation gpsLocation;

    @NonNull
    @Override
    public SaizadLocation appLocation() {
        return gpsLocation;
    }

//    @NonNull
//    @Override
//    public ViewModelProviderFactory viewModelProviderFactory() {
//        return viewModelProviderFactory;
//    }

    @NotNull
    @Override
    public Environment environment() {
        return mainEnvironment;
    }

    protected CurrentUserType currentUser() {
        return mainEnvironment.currentUser();
    }
}
