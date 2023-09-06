package com.marchtech.QRGenerator.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum Charset implements OptionList<String> {
    ASCII("US-ASCII"),
    ISO88591("ISO-8859-1"),
    @Default
    UTF8("UTF-8"),
    UTF16("UTF-16"),
    UTF16BE("UTF-16BE"),
    UTF16LE("UTF-16LE");

    private String charsets;

    Charset(String charset) {
        this.charsets = charset;
    }

    public String toUnderlyingValue() {
        return charsets;
    }

    private static final Map<String, Charset> lookup = new HashMap<>();

    static {
        for (Charset charset : Charset.values()) {
            lookup.put(charset.toUnderlyingValue(), charset);
        }
    }

    public static Charset fromUnderlyingValue(String charset) {
        return lookup.get(charset);
    }
}
