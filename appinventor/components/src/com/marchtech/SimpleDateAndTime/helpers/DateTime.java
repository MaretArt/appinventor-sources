package com.marchtech.SimpleDateAndTime.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.common.Default;

public enum DateTime implements OptionList<String> {
    @Default
    Day("day"),
    Month("month"),
    Year("year"),
    Hour("hour"),
    Minute("minute"),
    Second("second");

    private String dateTimes;

    DateTime(String dateTime) {
        this.dateTimes = dateTime;
    }

    public String toUnderlyingValue() {
        return dateTimes;
    }

    private static final Map<String, DateTime> lookup = new HashMap<>();

    static {
        for (DateTime dateTime : DateTime.values()) {
            lookup.put(dateTime.toUnderlyingValue(), dateTime);
        }
    }

    public static DateTime fromUnderlyingValue(String dateTime) {
        return lookup.get(dateTime);
    }
}
