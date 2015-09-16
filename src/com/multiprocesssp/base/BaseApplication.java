package com.multiprocesssp.base;

import android.app.Application;
import android.content.SharedPreferences;
import com.multiprocesssp.sharedpreferences.SPManager;

public class BaseApplication extends Application {
    private static BaseApplication instance = new BaseApplication();

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return SPManager.getsInstance().getSharedPreferences(name, mode);
    }

    public SharedPreferences getSystemSharedPreferences(String name, int mode) {
        return super.getSharedPreferences(name, mode);
    }

    @Override
    public void onCreate() {
        SPManager.getsInstance().init(this);
    }
}
