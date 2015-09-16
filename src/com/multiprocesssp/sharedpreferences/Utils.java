package com.multiprocesssp.sharedpreferences;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        /*
        * 这边直接调context.getSharedPreferences(name, mode)的话，
        * 会调用BaseApplication里面的getSharedPreferences，然后一层一层调回来，
        * 引起无限循环
        */
        SharedPreferences sp = null;
        try {
            Class<?> clazz = Class.forName("com.multiprocesssp.base.BaseApplication");
            Method method = clazz.getDeclaredMethod("getSystemSharedPreferences", new Class[]{String.class, int.class});
            sp = (SharedPreferences) method.invoke(context, new Object[]{name, mode});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return sp;
    }
}
