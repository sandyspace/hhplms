package com.haihua.hhplms.sys.model;

public class DictEntry {
    private String key;
    private String value;

    public DictEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
