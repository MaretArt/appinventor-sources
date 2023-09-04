package com.marchtech.FileUtils.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum Size implements OptionList<String> {
    @Default
    Byte,
    KiloByte,
    MegaByte,
    GigaByte;

    public String toUnderlyingValue() {
        return name();
    }

    private static final Map<String, Size> lookup = new HashMap<>();

    static {
        for (Size size : Size.values()) {
            lookup.put(size.toUnderlyingValue(), size);
        }
    }

    public static Size fromUnderlyingValue(String size) {
        return lookup.get(size);
    }
}
