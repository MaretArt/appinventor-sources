package com.marchtech.DeviceInfo.helpers;

import java.util.HashMap;
import java.util.Map;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.common.Default;

public enum Data implements OptionList<String> {
    @Default
    Country("country"),
    CountryCode("countryCode"),
    Region("region"),
    RegionName("regionName"),
    City("city"),
    Zip("zip"),
    Latitude("lat"),
    Longitude("lon"),
    Timezone("timezone"),
    ISP("isp"),
    ORG("org"),
    AS("as"),
    IP("query");

    private String datas;

    Data(String data) {
        this.datas = data;
    }

    public String toUnderlyingValue() {
        return datas;
    }

    private static final Map<String, Data> lookup = new HashMap<>();

    static {
        for (Data data : Data.values()) {
            lookup.put(data.toUnderlyingValue(), data);
        }
    }

    public static Data fromUnderlyingValue(String data) {
        return lookup.get(data);
    }
}