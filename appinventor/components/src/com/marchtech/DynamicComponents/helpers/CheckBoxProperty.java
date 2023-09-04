package com.marchtech.DynamicComponents.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.Default;
import com.google.appinventor.components.common.OptionList;

public enum CheckBoxProperty implements OptionList<String> {
    @Default
    BackgroundColor("BackgroundColor"),
    CheckboxColor("CheckboxColor"),
    Checked("Checked"),
    Enabled("Enabled"),
    FontSize("FontSize"),
    FontTypefaceImport("FontTypefaceImport"),
    Height("Height"),
    HeightPercent("HeightPercent"),
    Text("Text"),
    TextColor("TextColor"),
    Visible("Visible"),
    Width("Width"),
    WidthPercent("WidthPercent");

    private String property;

    CheckBoxProperty(String prop) {
        this.property = prop;
    }

    public String toUnderlyingValue() {
        return property;
    }

    private static final Map<String, CheckBoxProperty> lookup = new HashMap<>();

    static {
        for (CheckBoxProperty prop : CheckBoxProperty.values()) {
            lookup.put(prop.toUnderlyingValue(), prop);
        }
    }

    public static CheckBoxProperty fromUnderlyingValue(String prop) {
        return lookup.get(prop);
    }
}
