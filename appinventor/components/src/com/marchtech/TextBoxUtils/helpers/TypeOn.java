package com.marchtech.TextBoxUtils.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.common.Default;

public enum TypeOn implements OptionList<Integer> {
    @Default
    Normal(1),
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

    TypeOn(int type) {
        this.types = type;
    }

    public Integer toUnderlyingValue() {
        return types;
    }

    private static final Map<Integer, TypeOn> lookup = new HashMap<>();

    static {
        for (TypeOn type : TypeOn.values()) {
            lookup.put(type.toUnderlyingValue(), type);
        }
    }

    public static TypeOn fromUnderlyingValue(Integer type) {
        return lookup.get(type);
    }
}