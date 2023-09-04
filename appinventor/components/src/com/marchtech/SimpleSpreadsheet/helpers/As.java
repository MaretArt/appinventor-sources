package com.marchtech.SimpleSpreadsheet.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum As implements OptionList<String> {
    @Default
    Dictionary,
    List,
    Text,
    Automatic;

    public String toUnderlyingValue() {
        return name();
    }

    private static final Map<String, As> lookup = new HashMap<>();

    static {
        for (As as : As.values()) {
            lookup.put(as.toUnderlyingValue(), as);
        }
    }

    public static As fromUnderlyingValue(String as) {
        return lookup.get(as);
    }
}
