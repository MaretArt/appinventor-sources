package com.marchtech.GLCM.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum Mode implements OptionList<String> {
    @Default
    Contrast,
    Dissimilarity,
    Homogeneity,
    ASM,
    Energy,
    Correlation;

    public String toUnderlyingValue() {
        return name();
    }

    private static final Map<String, Mode> lookup = new HashMap<>();

    static {
        for (Mode mode : Mode.values()) {
            lookup.put(mode.toUnderlyingValue(), mode);
        }
    }

    public static Mode fromUnderlyingValue(String mode) {
        return lookup.get(mode);
    }
}
