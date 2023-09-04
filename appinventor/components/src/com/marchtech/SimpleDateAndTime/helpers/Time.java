package com.marchtech.SimpleDateAndTime.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.common.Default;

public enum Time implements OptionList<String> {
    @Default
    Hour("hour"),
    Minute("minute"),
    Second("second");

    private String times;

    Time(String time) {
        this.times = time;
    }

    public String toUnderlyingValue() {
        return times;
    }

    private static final Map<String, Time> lookup = new HashMap<>();

    static {
        for (Time time : Time.values()) {
            lookup.put(time.toUnderlyingValue(), time);
        }
    }

    public static Time fromUnderlyingValue(String time) {
        return lookup.get(time);
    }
}