package com.saizad.mvvm.delegation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.saizad.mvvm.ActivityResult;
import com.saizad.mvvm.delegation.BaseLifecycleDelegate;

public interface FragmentAppLifecycleDelegate extends BaseLifecycleDelegate {

     void onViewStateRestored(@Nullable Bundle savedInstanceState);

     void onAttach(Context context);

     View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

     void onActivityCreated(@Nullable Bundle savedInstanceState);

     void onViewCreated(View view, @Nullable Bundle savedInstanceState);

    void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater);

    void onDestroyView();

     void onDetach();

    <T> void finishWithResult(ActivityResult<T> activityResult);

    void finish();
}
