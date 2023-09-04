package com.marchtech.StringFunctions.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.common.Default;

public enum Random implements OptionList<String> {
    @Default
    Letter("letter"),
    Digit("digit"),
    Symbol("symbol"),
    LetterDigit("letter_digit"),
    LetterSymbol("letter_symbol"),
    DigitSymbol("digit_symbol"),
    LetterDigitSymbol("all");

    private String modes;

    Random(String mode) {
        this.modes = mode;
    }

    public String toUnderlyingValue() {
        return modes;
    }

    private static final Map<String, Random> lookup = new HashMap<>();

    static {
        for (Random mode : Random.values()) {
            lookup.put(mode.toUnderlyingValue(), mode);
        }
    }

    public static Random fromUnderlyingValue(String mode) {
        return lookup.get(mode);
    }
}
