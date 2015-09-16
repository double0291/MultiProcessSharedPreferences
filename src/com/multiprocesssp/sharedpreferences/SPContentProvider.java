package com.multiprocesssp.sharedpreferences;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.Map;

public class SPContentProvider extends ContentProvider {
    private static final String[] MOCK_COLUMNS = {"value"};
    private static final String NO_SUCH_KEY = "no_such_key";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String name = getParam(uri, Constants.KEY_FILE);
        String key = NO_SUCH_KEY;
        int type = -1;
        String defaultValue = null;

        for (int i = 0, count = projection.length; i < count; i++) {
            if (Constants.KEY_KEY.equals(projection[i])) {
                if (++i < count)
                    key = projection[i];
            } else if (Constants.KEY_VALUE_TYPE.equals(projection[i])) {
                if (++i < count)
                    type = Integer.parseInt(projection[i]);
            } else if (Constants.KEY_DEFAULT.equals(projection[i])) {
                if (++i < count)
                    defaultValue = projection[i];
            }
        }

        if (NO_SUCH_KEY.equals(key) || type == -1) {
            return null;
        }

        MatrixCursor cursor = new MatrixCursor(MOCK_COLUMNS);
        Object value = null;
        SharedPreferences sp = SPManager.getsInstance().getSharedPreferences(name, Context.MODE_PRIVATE);

        try {
            switch (type) {
                case Constants.VALUE_TYPE_INTEGER:
                    value = sp.getInt(key, Integer.parseInt(defaultValue));
                    break;
                case Constants.VALUE_TYPE_LONG:
                    value = sp.getLong(key, Long.parseLong(defaultValue));
                    break;
                case Constants.VALUE_TYPE_FLOAT:
                    value = sp.getFloat(key, Float.parseFloat(defaultValue));
                    break;
                case Constants.VALUE_TYPE_BOOLEAN:
                    // boolean has to be converted to an integer cursor
                    value = sp.getBoolean(key, Boolean.parseBoolean(defaultValue)) ? 1 : 0;
                    break;
                case Constants.VALUE_TYPE_STRING:
                    value = sp.getString(key, defaultValue);
                    break;
                case Constants.VALUE_TYPE_ANY:
                    // return something to hint that key exists
                    value = sp.contains(key) ? 0 : null;
                    break;
            }

            if (value != null) {
                cursor.addRow(new Object[]{
                        value
                });
            }
        } catch (Exception e) {

        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return "N/A";
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String name = getParam(uri, Constants.KEY_FILE);

        Map.Entry<String, Object> entry = contentValues.valueSet().iterator().next();
        if (entry == null) {
            return null;
        }

        String key = entry.getKey();
        Object value = entry.getValue();

        SharedPreferences.Editor editor = SPManager.getsInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit();

        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value).commit();
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value).commit();
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value).commit();
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof String) {
            editor.putString(key, (String) value).commit();
        } else {
            return null;
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String name = getParam(uri, Constants.KEY_FILE);
        String key = selection;

        if (Constants.KEY_ALL.equals(key)) {
            SPManager.getsInstance().getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
        } else {
            SPManager.getsInstance().getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).commit();
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        if (uri.getLastPathSegment() == null)
            return -1;
        else
            return 0;
    }

    private String getParam(Uri uri, String key) {
        try {
            return uri.getQueryParameter(key);
        } catch (UnsupportedOperationException e) {
        } catch (NullPointerException e) {
        }
        return null;
    }
}
