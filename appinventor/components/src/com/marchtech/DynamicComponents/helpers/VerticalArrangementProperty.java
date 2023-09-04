package com.marchtech.DynamicComponents.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum VerticalArrangementProperty implements OptionList<String> {
    @Default
    AlignHorizontal("AlignHorizontal"),
    AlignVertical("AlignVertical"),
    BackgroundColor("BackgroundColor"),
    Clickable("Clickable"),
    Height("Height"),
    HeightPercent("HeightPercent"),
    Image("Image"),
    Scrollbar("Scrollbar"),
    UseRoundCard("UseRoundCard"),
    Visible("Visible"),
    Width("Width"),
    WidthPercent("WidthPercent");

    private String property;

    VerticalArrangementProperty(String prop) {
        this.property = prop;
    }

    public String toUnderlyingValue() {
        return property;
    }

    private static final Map<String, VerticalArrangementProperty> lookup = new HashMap<>();

    static {
        for (VerticalArrangementProperty prop : VerticalArrangementProperty.values()) {
            lookup.put(prop.toUnderlyingValue(), prop);
        }
    }

    public static VerticalArrangementProperty fromUnderlyingValue(String prop) {
        return lookup.get(prop);
    }
}
