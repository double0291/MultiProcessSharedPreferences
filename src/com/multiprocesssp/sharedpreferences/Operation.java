package com.multiprocesssp.sharedpreferences;

public class Operation {
    public String key;
    public Object value;

    public int operationCode;
    public int valueType;

    public Operation(String key, Object value, int operationCode, int valueType) {
        this.key = key;
        this.value = value;
        this.operationCode = operationCode;
        this.valueType = valueType;
    }
}
