package com.marchtech.QRGenerator.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum FileFormat implements OptionList<String> {
    JPEG,
    @Default
    PNG,
    WEBP,
    WEBP_LOSSLESS,
    WEBP_LOSSY;

    public String toUnderlyingValue() {
        return name();
    }

    private static final Map<String, FileFormat> lookup = new HashMap<>();

    static {
        for (FileFormat format : FileFormat.values()) {
            lookup.put(format.toUnderlyingValue(), format);
        }
    }

    public static FileFormat fromUnderlyingValue(String format) {
        return lookup.get(format);
    }
}
