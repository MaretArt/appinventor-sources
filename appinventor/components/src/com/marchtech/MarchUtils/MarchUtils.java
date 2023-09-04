package com.marchtech.MarchUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.Dates;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.appinventor.components.runtime.util.YailList;
import com.marchtech.Icon;
import com.marchtech.MarchUtils.helpers.Sort;

@DesignerComponent(version = 1, description = "Extension to help you.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
public class MarchUtils extends AndroidNonvisibleComponent {
    public MarchUtils(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "For using map function.")
    public int MapInteger(int value, int fromMin, int fromMax, int toMin, int toMax) {
        if (fromMin == fromMax)
            return value;
        return (((value - fromMin) * (toMax - toMin)) / (fromMax - fromMin)) + toMin;
    }

    @SimpleFunction(description = "For using map function.")
    public float MapFloat(float value, float fromMin, float fromMax, float toMin, float toMax) {
        if (fromMin == fromMax)
            return value;
        return (((value - fromMin) * (toMax - toMin)) / (fromMax - fromMin)) + toMin;
    }

    @SimpleFunction(description = "For calculate glucose.")
    public Object Glucose(double contrast, double correlation, double energy, double homogeneity) {
        double mean = (contrast + correlation + energy + homogeneity) / 4;
        double result = (129780.0 * Math.pow(mean, 2)) - (145799.0 * mean) + 41033.0;
        String data = String.format("%f", result);
        int length = data.length();
        StringBuilder mResult = new StringBuilder(length);
        if (data.charAt(0) == '1' || data.charAt(0) == '2') {
            for (int i = 0; i < length; i++) {
                if (i == 3)
                    mResult.append(".");
                if (data.charAt(i) != '.')
                    mResult.append(data.charAt(i));
            }
        } else {
            for (int i = 0; i < length; i++) {
                if (i == 2)
                    mResult.append(".");
                if (data.charAt(i) != '.')
                    mResult.append(data.charAt(i));
            }
        }

        for (int i = 0; i < mResult.length(); i++) {
            if (mResult.charAt(i) == ',') {
                mResult.replace(i, mResult.length(), "");
                break;
            }
        }

        return Double.valueOf(mResult.toString());
    }

    @SimpleFunction(description = "For format decimal places.")
    public Object FormatDecimal(String format, Object value) {
        double mValue = Double.valueOf(String.valueOf(value));
        return String.format(format, mValue).replace(',', '.');
    }

    @SimpleFunction(description = "Get height of component from percent.")
    public int GetHeight(int percent, int componentHeight) {
        return (percent / 100) * componentHeight;
    }

    @SimpleFunction(description = "To convert list with value list to dictionary.")
    public YailDictionary ListToDictionary(YailList list, YailList tags) {
        List<Object> mList = YailListToList(list);
        List<Object> mTags = YailListToList(tags);

        Map<Object, Object> result = new HashMap<>();
        for (int i = 0; i < mList.size(); i++) {
            YailDictionary values = new YailDictionary();
            List<Object> listValues = YailListToList((YailList) mList.get(i));
            if (result.containsKey(listValues.get(0))) {
                values = (YailDictionary) result.get(listValues.get(0));
                boolean isList = true;
                for (int j = 1; j < mTags.size(); j++) {
                    if (!isYailList(values.get(mTags.get(j)))) {
                        isList = false;
                        break;
                    }
                }

                for (int j = 1; j < mTags.size(); j++) {
                    List<Object> newValues = new ArrayList<>();
                    if (isList) {
                        newValues = convertToList(values.get(mTags.get(j)));
                    } else {
                        newValues.add(values.get(mTags.get(j)));
                    }

                    newValues.add(listValues.get(j));
                    values.replace(mTags.get(j), YailList.makeList(newValues));
                }
            } else {
                for (int j = 1; j < mTags.size(); j++) {
                    values.put(mTags.get(j), listValues.get(j));
                }
            }

            result.put(listValues.get(0), values);
        }

        return YailDictionary.makeDictionary(result);
    }

    @SimpleFunction(description = "Returns a list after sort the given list with dictionary value by date.")
    public YailList SortByDate(YailList list, String keyPath, String pattern, Sort sortType) {
        List<Object> result = YailListToList(list);
        List<Object> dateList = new ArrayList<>();
        List<Object> mKeyPath = new ArrayList<>();
        if (keyPath.contains("/"))
            mKeyPath = stringToList(keyPath, "/");
        else
            mKeyPath.add(keyPath);

        for (Object date : list.toArray()) {
            YailDictionary data = (YailDictionary) date;
            dateList.add(data.getObjectAtKeyPath(mKeyPath));
        }

        if (sortType != Sort.BeforeToday || sortType != Sort.AfterToday) {
            int len = result.size();
            for (int counter = 0; counter < result.size() - 1; counter++) {
                len--;
                for (int i = 0; i < len; i++) {
                    int day1 = getPattern(dateList.get(i).toString(), pattern, 'd');
                    int month1 = getPattern(dateList.get(i).toString(), pattern, 'M');
                    int year1 = getPattern(dateList.get(i).toString(), pattern, 'y');
                    Calendar date1 = Dates.DateInstant(year1, month1, day1);

                    int day2 = getPattern(dateList.get(i + 1).toString(), pattern, 'd');
                    int month2 = getPattern(dateList.get(i + 1).toString(), pattern, 'M');
                    int year2 = getPattern(dateList.get(i + 1).toString(), pattern, 'y');
                    Calendar date2 = Dates.DateInstant(year2, month2, day2);

                    if (compareDate(date1, date2, sortType) < 0) {
                        Object lastDate = dateList.get(i);
                        Object lastResult = result.get(i);
                        dateList.set(i, dateList.get(i + 1));
                        dateList.set(i + 1, lastDate);
                        result.set(i, result.get(i + 1));
                        result.set(i + 1, lastResult);
                    }
                }
            }
        } else if (sortType == Sort.BeforeToday) {
            int numAfter = 0;
            for (int i = 0; i < result.size() - 1; i++) {
                int day1 = getPattern(dateList.get(i).toString(), pattern, 'd');
                int month1 = getPattern(dateList.get(i).toString(), pattern, 'M');
                int year1 = getPattern(dateList.get(i).toString(), pattern, 'y');
                Calendar date1 = Dates.DateInstant(year1, month1, day1);

                int day2 = getPattern(dateList.get(i + 1).toString(), pattern, 'd');
                int month2 = getPattern(dateList.get(i + 1).toString(), pattern, 'M');
                int year2 = getPattern(dateList.get(i + 1).toString(), pattern, 'y');
                Calendar date2 = Dates.DateInstant(year2, month2, day2);

                if (compareBeforeToday(date1, date2) < 0) {
                    Object lastDate = dateList.get(i);
                    Object lastResult = result.get(i);
                    dateList.set(i, dateList.get(i + 1));
                    dateList.set(i + 1, lastDate);
                    result.set(i, result.get(i + 1));
                    result.set(i + 1, lastResult);
                    numAfter++;
                }
            }

            int len = result.size() - numAfter;
            for (int counter = 0; counter < len - 1; counter++) {
                len--;
                for (int i = 0; i < len; i++) {
                    int day1 = getPattern(dateList.get(i).toString(), pattern, 'd');
                    int month1 = getPattern(dateList.get(i).toString(), pattern, 'M');
                    int year1 = getPattern(dateList.get(i).toString(), pattern, 'y');
                    Calendar date1 = Dates.DateInstant(year1, month1, day1);

                    int day2 = getPattern(dateList.get(i + 1).toString(), pattern, 'd');
                    int month2 = getPattern(dateList.get(i + 1).toString(), pattern, 'M');
                    int year2 = getPattern(dateList.get(i + 1).toString(), pattern, 'y');
                    Calendar date2 = Dates.DateInstant(year2, month2, day2);

                    if (compareDate(date1, date2, sortType) < 0) {
                        Object lastDate = dateList.get(i);
                        Object lastResult = result.get(i);
                        dateList.set(i, dateList.get(i + 1));
                        dateList.set(i + 1, lastDate);
                        result.set(i, result.get(i + 1));
                        result.set(i + 1, lastResult);
                    }
                }
            }

            int lenAfter = result.size();
            for (int counter = 0; counter < numAfter - 1; counter++) {
                lenAfter--;
                for (int i = result.size() - numAfter - 1; i < lenAfter; i++) {
                    int day1 = getPattern(dateList.get(i).toString(), pattern, 'd');
                    int month1 = getPattern(dateList.get(i).toString(), pattern, 'M');
                    int year1 = getPattern(dateList.get(i).toString(), pattern, 'y');
                    Calendar date1 = Dates.DateInstant(year1, month1, day1);

                    int day2 = getPattern(dateList.get(i + 1).toString(), pattern, 'd');
                    int month2 = getPattern(dateList.get(i + 1).toString(), pattern, 'M');
                    int year2 = getPattern(dateList.get(i + 1).toString(), pattern, 'y');
                    Calendar date2 = Dates.DateInstant(year2, month2, day2);

                    if (compareDate(date1, date2, sortType) < 0) {
                        Object lastDate = dateList.get(i);
                        Object lastResult = result.get(i);
                        dateList.set(i, dateList.get(i + 1));
                        dateList.set(i + 1, lastDate);
                        result.set(i, result.get(i + 1));
                        result.set(i + 1, lastResult);
                    }
                }
            }
        }

        return YailList.makeList(result);
    }

    @SimpleFunction(description = "Returns a list after sort the given list with dictionary value by letter.")
    public YailList SortByLetter(YailList list, String keyPath) {
        List<Object> result = YailListToList(list);
        List<Object> dataList = new ArrayList<>();
        List<Object> mKeyPath = new ArrayList<>();
        if (keyPath.contains("/"))
            mKeyPath = stringToList(keyPath, "/");
        else
            mKeyPath.add(keyPath);

        for (Object object : list.toArray()) {
            YailDictionary data = (YailDictionary) object;
            dataList.add(data.getObjectAtKeyPath(mKeyPath));
        }

        int len = result.size();
        for (int i = 0; i < result.size() - 1; i++) {
            len--;
            for (int index = 0; index < len; index++) {
                String data1 = String.valueOf(dataList.get(index));
                String data2 = String.valueOf(dataList.get(index + 1));

                if (data1.compareTo(data2) > 0) {
                    Object lastData = dataList.get(index);
                    Object lastResult = result.get(index);
                    dataList.set(index, dataList.get(index + 1));
                    dataList.set(index + 1, lastData);
                    result.set(index, result.get(index + 1));
                    result.set(index + 1, lastResult);
                }
            }
        }

        return YailList.makeList(result);
    }

    @SimpleFunction(description = "Returns a list after sort the given list with dictionary value by digit.")
    public YailList SortByDigit(YailList list, String keyPath) {
        List<Object> result = YailListToList(list);
        List<Object> dataList = new ArrayList<>();
        List<Object> mKeyPath = new ArrayList<>();
        if (keyPath.contains("/"))
            mKeyPath = stringToList(keyPath, "/");
        else
            mKeyPath.add(keyPath);

        for (Object object : list.toArray()) {
            YailDictionary data = (YailDictionary) object;
            dataList.add(data.getObjectAtKeyPath(mKeyPath));
        }

        int len = result.size();
        for (int i = 0; i < result.size() - 1; i++) {
            len--;
            for (int index = 0; index < len; index++) {
                int data1 = (int) dataList.get(index);
                int data2 = (int) dataList.get(index + 1);

                if ((data1 - data2) < 0) {
                    Object lastData = dataList.get(index);
                    Object lastResult = result.get(index);
                    dataList.set(index, dataList.get(index + 1));
                    dataList.set(index + 1, lastData);
                    result.set(index, result.get(index + 1));
                    result.set(index + 1, lastResult);
                }
            }
        }

        return YailList.makeList(result);
    }

    @SimpleFunction(description = "Returns a list after remove all contains value in the given list with dictionary value.")
    public YailList RemoveAllContainsValue(YailList list, String keyPath, Object value) {
        List<Object> result = YailListToList(list);
        List<Object> dataList = new ArrayList<>();
        List<Object> mKeyPath = new ArrayList<>();
        if (keyPath.contains("/"))
            mKeyPath = stringToList(keyPath, "/");
        else
            mKeyPath.add(keyPath);

        for (Object object : list.toArray()) {
            YailDictionary data = (YailDictionary) object;
            dataList.add(data.getObjectAtKeyPath(mKeyPath));
        }

        for (int i = 0; i < result.size(); i++) {
            if (i >= result.size())
                break;
            else if (dataList.get(i) == value) {
                dataList.remove(i);
                result.remove(i);
            }
        }

        return YailList.makeList(result);
    }

    @SimpleFunction(description = "Returns a dictionary after converting data of onesignal.")
    public Object ConvertJSONString(Object json) throws JSONException {
        if (json instanceof JSONObject) {
            JSONObject object = new JSONObject(json);
            return YailDictionary.makeDictionary(toMap(object));
        } else if (json instanceof JSONArray) {
            JSONArray array = new JSONArray(json);
            return YailList.makeList(toList(array));
        }

        return "It's not JSON";
    }

    private Map<Object, Object> toMap(JSONObject json) throws JSONException {
        Map<Object, Object> result = new HashMap<>();
        Iterator<Object> keys = json.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            Object value = json.get(String.valueOf(key));
            if (value instanceof JSONArray)
                value = YailList.makeList(toList((JSONArray) value));
            else if (value instanceof JSONObject)
                value = YailDictionary.makeDictionary(toMap((JSONObject) value));

            result.put(key, value);
        }

        return result;
    }

    private List<Object> toList(JSONArray json) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            Object value = json.get(i);
            if (value instanceof JSONArray)
                value = YailList.makeList(toList((JSONArray) value));
            else if (value instanceof JSONObject)
                value = YailDictionary.makeDictionary(toMap((JSONObject) value));

            list.add(value);
        }

        return list;
    }

    private List<Object> stringToList(String string, String regex) {
        List<Object> list = new ArrayList<>();
        for (Object obj : string.split(regex)) {
            list.add(obj);
        }

        return list;
    }

    private boolean isYailList(Object object) {
        if (object instanceof YailList)
            return true;
        else
            return false;
    }

    private List<Object> YailListToList(YailList list) {
        List<Object> result = new ArrayList<>();
        for (Object object : list.toArray()) {
            result.add(object);
        }

        return result;
    }

    private List<Object> convertToList(Object object) {
        List<Object> list = new ArrayList<>();
        if (object.getClass().isArray())
            list = Arrays.asList((Object[]) object);
        else if (object instanceof Collection)
            list = new ArrayList<>((Collection<?>) object);

        return list;
    }

    private int getPattern(String date, String pattern, char regex) {
        int index = pattern.indexOf(regex);
        int len = 0;
        char[] chars = pattern.toCharArray();
        for (char c : chars) {
            if (c == regex)
                len++;
        }

        return Integer.valueOf(date.substring(index, index + len));
    }

    private int compareDate(Calendar date1, Calendar date2, Sort type) {
        Calendar today = Dates.Now();
        try {
            today.set(Calendar.HOUR_OF_DAY, 12);
            date1.set(Calendar.HOUR_OF_DAY, 12);
            date2.set(Calendar.HOUR_OF_DAY, 12);
            today.set(Calendar.MINUTE, 0);
            date1.set(Calendar.MINUTE, 0);
            date2.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            date1.set(Calendar.SECOND, 0);
            date2.set(Calendar.SECOND, 0);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "SortByDate", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        int day = Dates.Day(Dates.Now());
        int day1 = Dates.Day(date1);
        int day2 = Dates.Day(date2);
        int month = Dates.Month(Dates.Now()) - 1;
        int month1 = Dates.Month(date1);
        int month2 = Dates.Month(date2);
        int year = Dates.Month(Dates.Now());
        int year1 = Dates.Month(date1);
        int year2 = Dates.Month(date2);

        if (type == Sort.Default)
            return date1.compareTo(date2);
        else if (type == Sort.Today) {
            if (Math.abs(year1 - year) < Math.abs(year2 - year))
                return 1;
            else if (Math.abs(year1 - year) == Math.abs(year2 - year)) {
                if (Math.abs(month1 - month) < Math.abs(month2 - month))
                    return 1;
                else if (Math.abs(month1 - month) == Math.abs(month2 - month)) {
                    if (Math.abs(day1 - day) < Math.abs(day2 - day))
                        return 1;
                    else if (Math.abs(day1 - day) == Math.abs(day2 - day))
                        return 0;
                }
            }

            return -1;
        }

        return 0;
    }

    private int compareBeforeToday(Calendar date1, Calendar date2) {
        Calendar today = Dates.Now();
        try {
            today.set(Calendar.HOUR_OF_DAY, 12);
            date1.set(Calendar.HOUR_OF_DAY, 12);
            date2.set(Calendar.HOUR_OF_DAY, 12);
            today.set(Calendar.MINUTE, 0);
            date1.set(Calendar.MINUTE, 0);
            date2.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            date1.set(Calendar.SECOND, 0);
            date2.set(Calendar.SECOND, 0);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "SortByDate", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        int day = Dates.Day(Dates.Now());
        int day1 = Dates.Day(date1);
        int day2 = Dates.Day(date2);
        int month = Dates.Month(Dates.Now()) - 1;
        int month1 = Dates.Month(date1);
        int month2 = Dates.Month(date2);
        int year = Dates.Month(Dates.Now());
        int year1 = Dates.Month(date1);
        int year2 = Dates.Month(date2);

        if ((year1 - year) >= 0 && (year2 - year) < 0)
            return -1;
        else if ((year1 - year) < 0 && (year2 - year) < 0) {
            if ((month1 - month) >= 0 && (month2 - month) < 0)
                return -1;
            else if ((month1 - month) < 0 && (month2 - month) < 0) {
                if ((day1 - day) >= 0 && (day2 - day) < 0)
                    return -1;
                else if ((day1 - day) < 0 && (day2 - day) < 0)
                    return 0;
            }
        }

        return 1;
    }
}
