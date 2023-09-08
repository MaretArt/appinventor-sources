package com.marchtech.SimpleMenu.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;

public enum Placement implements OptionList<String> {
    LEFT_TOP,
    LEFT_CENTER,
    LEFT_BOTTOM,
    CENTER_TOP,
    CENTER,
    CENTER_BOTTOM,
    RIGHT_TOP,
    RIGHT_CENTER,
    RIGHT_BOTTOM;

    public String toUnderlyingValue() {
        return name();
    }

    private static final Map<String, Placement> lookup = new HashMap<>();

    static {
        for (Placement placement : Placement.values()) {
            lookup.put(placement.toUnderlyingValue(), placement);
        }
    }

    public static Placement fromUnderlyingVslue(String placement) {
        return lookup.get(placement);
    }
}
