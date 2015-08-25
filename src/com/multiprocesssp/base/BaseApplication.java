package com.multiprocesssp.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.multiprocesssp.sharedpreferences.SPManager;

public class BaseApplication extends Application {
    private static BaseApplication instance = new BaseApplication();

    public static BaseApplication getInstance() {
        return instance;
    }

    public SharedPreferences getSP() {
        return SPManager.getsInstance().getSharedPreferences("abc", Context.MODE_MULTI_PROCESS);
    }

    @Override
    public void onCreate() {
        SPManager.getsInstance().init(this);
    }
}
