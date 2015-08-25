package com.multiprocesssp.sharedpreferences;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

public class ContentProviderOperator implements IKeyValueOperator {
    private static final String BASE_URI = "content://com.multiprocesssp.sharedpreferences.SPContentProvider";
    private static final String DEFAULT_NAME = "default";

    private WeakReference<Context> mContext;
    private Uri mUri;

    public ContentProviderOperator(WeakReference<Context> context, String name) {
        mContext = context;
        String uriStr = BASE_URI + "/params?" + Constants.KEY_FILE + "=" + (TextUtils.isEmpty(name) ? DEFAULT_NAME :
                name);
        mUri = Uri.parse(uriStr);
    }

    @Override
    public Object read(int valueType, String key, Object defaultValue) {
        Context context;

        if (mContext == null || (context = mContext.get()) == null) {
            return defaultValue;
        }

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(mUri, new String[]{
                    Constants.KEY_KEY, key,
                    Constants.KEY_VALUE_TYPE, valueType + "",
                    Constants.KEY_DEFAULT, defaultValue + ""
            }, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cursor == null) {
            return defaultValue;
        }

        try {
            if (!cursor.moveToFirst()) {
                return defaultValue;
            } else {
                Object result = getDataFromCursor(cursor, valueType);
                if (result != null) {
                    return result;
                } else {
                    return defaultValue;
                }
            }
        } catch (Throwable t) {
            return defaultValue;
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, ?> readALl() {
        return null;
    }

    @Override
    public void write(int valueType, String key, Object value) {
        Context context;

        if (mContext == null || (context = mContext.get()) == null) {
            return;
        }

        if (value == null) {
            delete(key);
        } else {
            try {
                context.getContentResolver().insert(mUri, castPair(key, value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(String key) {
        Context context;

        if (mContext == null || (context = mContext.get()) == null) {
            return;
        }

        try {
            context.getContentResolver().delete(mUri, key, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        delete(Constants.KEY_ALL);
    }

    private static Object getDataFromCursor(Cursor cursor, int valueType) {
        if (cursor == null) {
            return null;
        }

        try {
            switch (valueType) {
                case Constants.VALUE_TYPE_INTEGER:
                    return cursor.getInt(0);
                case Constants.VALUE_TYPE_BOOLEAN:
                    return cursor.getInt(0) != 0;
                case Constants.VALUE_TYPE_FLOAT:
                    return cursor.getFloat(0);
                case Constants.VALUE_TYPE_LONG:
                    return cursor.getLong(0);
                case Constants.VALUE_TYPE_STRING:
                    return cursor.getString(0);
                case Constants.VALUE_TYPE_ANY:
                    // "contain" case, if has any data, return true;
                    return cursor.getCount() != 0 ? true : false;
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ContentValues castPair(String key, Object value) {
        ContentValues result = new ContentValues(1);
        if (value instanceof Integer) {
            result.put(key, (Integer) value);
        } else if (value instanceof Long) {
            result.put(key, (Long) value);
        } else if (value instanceof Float) {
            result.put(key, (Float) value);
        } else if (value instanceof String) {
            result.put(key, (String) value);
        } else if (value instanceof Boolean) {
            result.put(key, (Boolean) value);
        } else {
            result.put(key, "");
        }

        return result;
    }
}
