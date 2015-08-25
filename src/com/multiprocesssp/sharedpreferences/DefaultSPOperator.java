package com.multiprocesssp.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Map;
import java.util.Set;

public class DefaultSPOperator implements IKeyValueOperator {
    SharedPreferences mDefaultSP;

    public DefaultSPOperator(Context context, String name, int mode) {
        if (context != null) {
            mDefaultSP = Utils.getSystemSP(context, name, mode);
        }
    }

    @Override
    public Object read(int valueType, String key, Object defaultValue) {
        if (mDefaultSP == null) {
            return defaultValue;
        }

        try {
            switch (valueType) {
                case Constants.VALUE_TYPE_INTEGER:
                    return mDefaultSP.getInt(key, defaultValue != null ? (Integer) defaultValue : 0);
                case Constants.VALUE_TYPE_FLOAT:
                    return mDefaultSP.getFloat(key, defaultValue != null ? (Float) defaultValue : 0);
                case Constants.VALUE_TYPE_LONG:
                    return mDefaultSP.getLong(key, defaultValue != null ? (Long) defaultValue : 0);
                case Constants.VALUE_TYPE_BOOLEAN:
                    return mDefaultSP.getBoolean(key, defaultValue != null ? (Boolean) defaultValue : false);
                case Constants.VALUE_TYPE_STRING:
                    return mDefaultSP.getString(key, defaultValue != null ? (String) defaultValue : null);
                case Constants.VALUE_TYPE_STRING_SET:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        return mDefaultSP.getStringSet(key, defaultValue != null ? (Set<String>) defaultValue : null);
                    } else {
                        return defaultValue;
                    }
                case Constants.VALUE_TYPE_ANY:
                    if (mDefaultSP.contains(key)) {
                        return true;
                    } else {
                        return defaultValue;
                    }
                default:
                    return defaultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public Map<String, ?> readALl() {
        if (mDefaultSP == null)
            return null;
        else
            return mDefaultSP.getAll();
    }

    @Override
    public void write(int valueType, String key, Object value) {
        if (mDefaultSP == null)
            return;

        try {
            SharedPreferences.Editor editor = mDefaultSP.edit();
            switch (valueType) {
                case Constants.VALUE_TYPE_INTEGER:
                    editor.putInt(key, (Integer) value);
                    break;
                case Constants.VALUE_TYPE_BOOLEAN:
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case Constants.VALUE_TYPE_FLOAT:
                    editor.putFloat(key, (Float) value);
                    break;
                case Constants.VALUE_TYPE_LONG:
                    editor.putLong(key, (Long) value);
                    break;
                case Constants.VALUE_TYPE_STRING:
                    editor.putString(key, (String) value);
                    break;
                case Constants.VALUE_TYPE_STRING_SET:
                    editor.putStringSet(key, (Set<String>) value);
                    break;
            }
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
        if (mDefaultSP == null)
            return;

        SharedPreferences.Editor editor = mDefaultSP.edit();
        editor.remove(key).commit();
    }

    @Override
    public void clear() {
        if (mDefaultSP == null)
            return;

        SharedPreferences.Editor editor = mDefaultSP.edit();
        editor.clear().commit();
    }
}
