package com.marchtech.QRGenerator.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum BarFormat implements OptionList<String> {
    AZTEC,
    CODABAR,
    CODE_39,
    CODE_93,
    CODE_128,
    DATA_MATRIX,
    EAN_8,
    EAN_13,
    ITF,
    MAXICODE,
    PDF_417,
    @Default
    QR_CODE,
    RSS_14,
    RSS_EXPANDED,
    UPC_A,
    UPC_E,
    UPC_EAN_EXTENSION;

    public String toUnderlyingValue() {
        return name();
    }

    private static final Map<String, BarFormat> lookup = new HashMap<>();

    static {
        for (BarFormat format : BarFormat.values()) {
            lookup.put(format.toUnderlyingValue(), format);
        }
    }

    public static BarFormat fromUnderlyingValue(String format) {
        return lookup.get(format);
    }
}
