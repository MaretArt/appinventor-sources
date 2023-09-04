package com.marchtech.TextBoxUtils.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.common.Default;

public enum TypeOff implements OptionList<Integer> {
    Normal(1),
    @Default
    Password(2),
    Name(3),
    Email(4),
    Number(5),
    NumberPassword(6),
    DateTime(7),
    DecimalNumber(8),
    PhoneNumber(9),
    PostalAddress(10);

    private int types;

    TypeOff(int type) {
        this.types = type;
    }

    public Integer toUnderlyingValue() {
        return types;
    }

    private static final Map<Integer, TypeOff> lookup = new HashMap<>();

    static {
        for (TypeOff type : TypeOff.values()) {
            lookup.put(type.toUnderlyingValue(), type);
        }
    }

    public static TypeOff fromUnderlyingValue(Integer type) {
        return lookup.get(type);
    }
}