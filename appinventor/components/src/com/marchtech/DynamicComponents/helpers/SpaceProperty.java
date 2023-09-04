package com.marchtech.DynamicComponents.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum SpaceProperty implements OptionList<String> {
    @Default
    Height("Height"),
    HeightPercent("HeightPercent"),
    Visible("Visible"),
    Width("Width"),
    WidthPercent("WidthPercent");

    private String property;

    SpaceProperty(String prop) {
        this.property = prop;
    }

    public String toUnderlyingValue() {
        return property;
    }

    private static final Map<String, SpaceProperty> lookup = new HashMap<>();

    static {
        for (SpaceProperty prop : SpaceProperty.values()) {
            lookup.put(prop.toUnderlyingValue(), prop);
        }
    }

    public static SpaceProperty fromUnderlyingValue(String prop) {
        return lookup.get(prop);
    }
}
