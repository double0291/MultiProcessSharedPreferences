package com.multiprocesssp.sharedpreferences;

import java.util.Map;

public interface IKeyValueOperator {
    public Object read(int valueType, String key, Object defaultValue);

    public Map<String, ?> readALl();

    public void write(int valueType, String key, Object value);

    public void delete(String key);

    public void clear();
}
