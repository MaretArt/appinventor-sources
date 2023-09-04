package com.marchtech.DynamicComponents.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.common.Default;

public enum Components implements OptionList<Object> {
    @Default
    Button("Button"),
    Checkbox("CheckBox"),
    DatePicker("DatePicker"),
    Image("Image"),
    Label("Label"),
    Space("SpaceView"),
    HorizontalArrangement("HorizontalArrangement"),
    VerticalArrangement("VerticalArrangement");

    private Object components;

    Components(Object component) {
        this.components = component;
    }

    public Object toUnderlyingValue() {
        return components;
    }

    private static final Map<Object, Components> lookup = new HashMap<>();

    static {
        for (Components component : Components.values()) {
            lookup.put(component.toUnderlyingValue(), component);
        }
    }

    public static Components fromUnderlyingValue(Object component) {
        return lookup.get(component);
    }
}
