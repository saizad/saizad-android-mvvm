package com.saizad.mvvm.di;

public abstract class BaseDiAppModule {

    /*@Singleton
    @Provides
    public static SaizadLocation providesAppLocation(Application application) {
        return new SaizadLocation(application);
    }

    @Singleton
    @Provides
    public static FCMToken providesFCMToken(SharedPreferences sharedPreferences, Gson gson) {
        return new FCMToken(sharedPreferences, gson);
    }

    public SaizadEasyRetrofit retrofit(Application application, CurrentUserType currentUser, Gson gson){
        return new SaizadEasyRetrofit(application, currentUser, gson, domainURL());
    }

    @Singleton
    @Provides
    public SaizadEasyRetrofit providesRetrofit(Application application, CurrentUserType currentUser, Gson gson) {
        return retrofit(application, currentUser, gson);
    }

    public abstract String domainURL();

    @Singleton
    @Provides
    public static SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    abstract public CurrentUserType currentUser(SharedPreferences sharedPreferences, Gson gson);

    @Singleton
    @Provides
    public static Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Singleton
    @Provides
    public static BehaviorSubject<ActivityResult<?>> provideFragmentNavigationResult() {
        return BehaviorSubject.create();
    }

    @Singleton
    @Provides
    @Named("notification")
    public static BehaviorSubject<NotifyOnce<?>> notificationResultBehaviorSubject() {
        return BehaviorSubject.create();
    }

    @Singleton
    @Provides
    public CurrentUserType providesCurrentUserType(SharedPreferences sharedPreferences, Gson gson) {
        return currentUser(sharedPreferences, gson);
    }

    @Singleton
    @Provides
    public static Environment providesEnvironment(FCMToken fcmToken, CurrentUserType currentUser, BehaviorSubject<ActivityResult<?>> navigationFragmentResult, @Named("notification") BehaviorSubject<NotifyOnce<?>> notifyOnceBehaviorSubject, PermissionManager permissionManager) {
        return new Environment(fcmToken, navigationFragmentResult, currentUser, notifyOnceBehaviorSubject, permissionManager);
    }*/

}