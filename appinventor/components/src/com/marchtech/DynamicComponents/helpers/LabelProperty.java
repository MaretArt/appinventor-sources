package com.marchtech.DynamicComponents.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum LabelProperty implements OptionList<String> {
    @Default
    BackgroundColor("BackgroundColor"),
    Clickable("Clickable"),
    FontSize("FontSize"),
    FontTypefaceImport("FontTypefaceImport"),
    HasMargins("HasMargins"),
    Height("Height"),
    HeightPercent("HeightPercent"),
    Marquee("Marquee"),
    MaxLines("MaxLines"),
    RotationAngle("RotationAngle"),
    Text("Text"),
    TextColor("TextColor"),
    Visible("Visible"),
    Width("Width"),
    WidthPercent("WidthPercent");

    private String property;

    LabelProperty(String prop) {
        this.property = prop;
    }

    public String toUnderlyingValue() {
        return property;
    }

    private static final Map<String, LabelProperty> lookup = new HashMap<>();

    static {
        for (LabelProperty prop : LabelProperty.values()) {
            lookup.put(prop.toUnderlyingValue(), prop);
        }
    }

    public static LabelProperty fromUnderlyingValue(String prop) {
        return lookup.get(prop);
    }
}
