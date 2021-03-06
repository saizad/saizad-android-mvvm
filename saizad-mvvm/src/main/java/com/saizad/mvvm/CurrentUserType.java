package com.saizad.mvvm;


import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.sa.easyandroidform.ObjectUtils;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class CurrentUserType<U> extends SaizadSharedPreferences {

    protected final U emptyUser;
    private final BehaviorSubject<U> currentUser = BehaviorSubject.create();
    private static final String KEY_USER_INFO = "user_info";
    private U user;

    protected CurrentUserType(U emptyUser, SharedPreferences sharedPreferences, Gson gson) {
        super(sharedPreferences, gson);
        this.emptyUser = emptyUser;
        this.currentUser
                .skip(1)
                .filter(ObjectUtils::isNotNull)
                .subscribe(userInfo -> {
                    user = userInfo;
                    if (ObjectUtils.isNull(verifyUserObject(userInfo))) {
                        removeValue(KEY_USER_INFO);
                    } else {
                        putSharedPrefObject(KEY_USER_INFO, userInfo);
                    }
                });

        currentUser.onNext(ObjectUtils.coalesce(getSharedPrefObject(KEY_USER_INFO, getClassType()), emptyUser));
    }

    protected abstract @NonNull Class<U> getClassType();

    public void login(@NonNull U newUser) {
        currentUser.onNext(newUser);
    }

    public final void logout() {
        sharedPreferences.edit().clear().apply();
        currentUser.onNext(emptyUser);
    }

    public void refresh(@NonNull U freshUser) {
        currentUser.onNext(freshUser);
    }


    public @NonNull
    Observable<U> observable() {
        return currentUser;
    }

    public final  @Nullable
    U getUser() {
        if (user == null) {
            user = getSharedPrefObject(KEY_USER_INFO, getClassType());
        }
        return user;
    }


    public boolean exists() {
        return getUser() != null;
    }


    public @NonNull
    Observable<Boolean> isLoggedIn() {
        return observable().map(user -> ObjectUtils.isNotNull(verifyUserObject(user)));
    }


    public Observable<U> loggedInUser() {
        return observable().filter(user -> ObjectUtils.isNotNull(verifyUserObject(user)));
    }


    public Observable<Object> loggedOutUser() {
        return observable().filter(user -> ObjectUtils.isNull(verifyUserObject(user)))
                .map(__ -> new Object());
    }

    @Nullable
    protected U verifyUserObject(U user) {
        return user == emptyUser ? null : user;
    }

    public abstract @Nullable
    String getToken();
}
