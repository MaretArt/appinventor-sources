package com.marchtech.OneSignalScheduler.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.common.Default;

public enum Segment implements OptionList<String> {
    @Default
    Subscribed("Subscribed Users"),
    Active("Active Users"),
    Inactive("Inactive Users");

    private String segments;

    Segment(String segment) {
        this.segments = segment;
    }

    public String toUnderlyingValue() {
        return segments;
    }

    private static final Map<String, Segment> lookup = new HashMap<>();

    static {
        for (Segment segment : Segment.values()) {
            lookup.put(segment.toUnderlyingValue(), segment);
        }
    }

    public static Segment fromUnderlyingValue(String segment) {
        return lookup.get(segment);
    }
}