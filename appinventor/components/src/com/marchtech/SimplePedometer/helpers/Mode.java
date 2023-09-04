package com.marchtech.SimplePedometer.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum Mode implements OptionList<String> {
    Simple("Simple"),
    @Default
    Walk("Walk");

    private String mode;

    Mode(String modes) {
        this.mode = modes;
    }

    public String toUnderlyingValue() {
        return mode;
    }

    private static final Map<String, Mode> lookup = new HashMap<>();

    static {
        for (Mode modes : Mode.values()) {
            lookup.put(modes.toUnderlyingValue(), modes);
        }
    }

    public static Mode fromUnderlyingValue(String modes) {
        return lookup.get(modes);
    }
}
