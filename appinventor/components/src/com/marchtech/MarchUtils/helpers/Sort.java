package com.marchtech.MarchUtils.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum Sort implements OptionList<String> {
    @Default
    Default,
    Today,
    BeforeToday,
    AfterToday;

    public String toUnderlyingValue() {
        return name();
    }

    private static final Map<String, Sort> lookup = new HashMap<>();

    static {
        for (Sort sort : Sort.values()) {
            lookup.put(sort.toUnderlyingValue(), sort);
        }
    }

    public static Sort fromUnderlyingValue(String sort) {
        return lookup.get(sort);
    }
}
