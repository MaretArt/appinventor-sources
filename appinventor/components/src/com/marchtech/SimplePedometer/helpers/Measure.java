package com.marchtech.SimplePedometer.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;

public enum Measure implements OptionList<String> {
    Kilometer("km"),
    Meter("m"),
    Mile("mile");

    private String measures;

    Measure(String measure) {
        this.measures = measure;
    }

    public String toUnderlyingValue() {
        return measures;
    }

    private static final Map<String, Measure> lookup = new HashMap<>();

    static {
        for (Measure measure : Measure.values()) {
            lookup.put(measure.toUnderlyingValue(), measure);
        }
    }

    public static Measure fromUnderlyingValue(String measure) {
        return lookup.get(measure);
    }
}
