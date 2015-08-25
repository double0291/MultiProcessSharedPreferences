package com.multiprocesssp.sharedpreferences;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    private final static String MAIN_PROCESS = "com.multiprocesssp";

    public static String sProcessName;
    public static boolean sIsInMainProcess = true;

    public static void initProcessInfo(Context context) {
        if (context == null)
            return;

        // get the name of current process
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
            if (info.pid == pid) {
                sProcessName = info.processName;
                break;
            }
        }

        sIsInMainProcess = MAIN_PROCESS.equals(sProcessName);
    }

    public static SharedPreferences getSystemSP(Context context, String name, int mode) {
        return context.getSharedPreferences(name, mode);
    }
}
