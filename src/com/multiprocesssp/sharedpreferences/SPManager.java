package com.multiprocesssp.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SPManager {
    private static SPManager sInstance = new SPManager();

    private WeakReference<Context> mContext = null;

    private Map<String, SharedPreferences> mLocalSPs = new ConcurrentHashMap<String, SharedPreferences>(5);
    private Map<String, SharedPreferences> mRemoteSPs = new ConcurrentHashMap<String, SharedPreferences>(5);

    public static SPManager getsInstance() {
        return sInstance;
    }

    public synchronized void init(Context context) {
        mContext = new WeakReference<Context>(context);
        Utils.initProcessInfo(context);
    }

    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (TextUtils.isEmpty(name)) {
            name = "null";
        }
        SharedPreferences sp = null;
        Map<String, SharedPreferences> spMap = null;

        if (!Utils.sIsInMainProcess) {
            spMap = mRemoteSPs;
        } else {
            spMap = mLocalSPs;
        }

        sp = spMap.get(name);
        if (sp == null) {
            synchronized (spMap) {
                sp = spMap.get(name);
                if (sp == null) {
                    sp = new SPProxy(mContext, name, mode);
                    spMap.put(name, sp);
                }
            }
        }

        return sp;
    }
}
