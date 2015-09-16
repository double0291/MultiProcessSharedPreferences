package com.multiprocesssp.sharedpreferences;

import java.util.Map;

public interface IKeyValueOperator {
    Object read(int valueType, String key, Object defaultValue);

    Map<String, ?> readALl();

    void write(int valueType, String key, Object value);

    void delete(String key);

    void clear();
}
