package com.marchtech.StringFunctions.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.common.Default;

public enum Case implements OptionList<String> {
    @Default
    Sentencecase("Sentence case"),
    Lowercase("lowercase"),
    Uppercase("UPPERCASE"),
    CapitalizeEachWord("Capitalize Each Word"),
    Togglecase("tOGGLE cASE");

    private String cases;

    Case(String cas) {
        this.cases = cas;
    }

    public String toUnderlyingValue() {
        return cases;
    }

    private static final Map<String, Case> lookup = new HashMap<>();

    static {
        for (Case cas : Case.values()) {
            lookup.put(cas.toUnderlyingValue(), cas);
        }
    }

    public static Case fromUnderlyingValue(String cas) {
        return lookup.get(cas);
    }
}