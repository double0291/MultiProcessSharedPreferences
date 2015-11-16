package com.multiprocesssp.base;

import android.app.Application;
import android.content.SharedPreferences;
import com.multiprocesssp.sharedpreferences.SPManager;

public class BaseApplication extends Application {
    private static BaseApplication instance;

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
		super.onCreate();
		instance = this;
		
        SPManager.getsInstance().init(this);
    }
}
