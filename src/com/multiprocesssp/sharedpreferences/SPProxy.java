package com.multiprocesssp.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SPProxy implements SharedPreferences {
    private IKeyValueOperator mSPOperator;
    private EditorImpl mEditor;
    private Set<OnSharedPreferenceChangeListener> mListeners;

    public SPProxy(WeakReference<Context> context, String name, int mode) {
        if (Utils.sIsInMainProcess) {
            mSPOperator = new DefaultSPOperator(context.get(), name, mode);
        } else {
            mSPOperator = new ContentProviderOperator(context, name);
        }
        mEditor = new EditorImpl();
    }

    @Override
    public Map<String, ?> getAll() {
        return mSPOperator.readALl();
    }

    @Override
    public String getString(String key, String defaultValue) {
        return (String) mSPOperator.read(Constants.VALUE_TYPE_STRING, key, defaultValue);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        Object result = mSPOperator.read(Constants.VALUE_TYPE_STRING_SET, key, defaultValue);
        if (result != null && result instanceof Set<?>) {
            return (Set<String>) result;
        } else {
            return null;
        }
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return (Integer) mSPOperator.read(Constants.VALUE_TYPE_INTEGER, key, defaultValue);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return (Long) mSPOperator.read(Constants.VALUE_TYPE_LONG, key, defaultValue);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return (Float) mSPOperator.read(Constants.VALUE_TYPE_FLOAT, key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return (Boolean) mSPOperator.read(Constants.VALUE_TYPE_BOOLEAN, key, defaultValue);
    }

    @Override
    public boolean contains(String key) {
        return (Boolean) mSPOperator.read(Constants.VALUE_TYPE_ANY, key, false);
    }

    @Override
    public Editor edit() {
        return mEditor;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener
                                                                 onSharedPreferenceChangeListener) {
        if (mListeners == null) {
            mListeners = new HashSet<OnSharedPreferenceChangeListener>(2);
        }

        mListeners.add(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener
                                                                   onSharedPreferenceChangeListener) {
        if (mListeners != null) {
            mListeners.remove(onSharedPreferenceChangeListener);
        }
    }

    private class EditorImpl implements SharedPreferences.Editor {
        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            mSPOperator.write(Constants.VALUE_TYPE_STRING, key, value);
            notifyListeners(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String key, Set<String> value) {
            mSPOperator.write(Constants.VALUE_TYPE_STRING_SET, key, value);
            notifyListeners(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            mSPOperator.write(Constants.VALUE_TYPE_INTEGER, key, value);
            notifyListeners(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            mSPOperator.write(Constants.VALUE_TYPE_LONG, key, value);
            notifyListeners(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            mSPOperator.write(Constants.VALUE_TYPE_FLOAT, key, value);
            notifyListeners(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            mSPOperator.write(Constants.VALUE_TYPE_BOOLEAN, key, value);
            notifyListeners(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            mSPOperator.delete(key);
            notifyListeners(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor clear() {
            mSPOperator.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return true;
        }

        @Override
        public void apply() {

        }

        private void notifyListeners(String key) {
            synchronized (SPProxy.this) {
                if (mListeners != null) {
                    Iterator<OnSharedPreferenceChangeListener> iterator = mListeners.iterator();
                    while (iterator.hasNext()) {
                        OnSharedPreferenceChangeListener listener = iterator.next();
                        listener.onSharedPreferenceChanged(SPProxy.this, key);
                    }
                }
            }
        }
    }
}
