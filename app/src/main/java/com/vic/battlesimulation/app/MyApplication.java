package com.vic.battlesimulation.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class MyApplication extends Application {
    private static Context mContext;
    public static final String EXTRA_SETTING = "extra_settings";
    public static final String LAST_SETTING = "last_setting";
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
