package com.marchtech.DictionaryUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.Dates;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.appinventor.components.runtime.util.YailList;
import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Extension to help you using dictionary.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class DictionaryUtils extends AndroidNonvisibleComponent {
    public DictionaryUtils(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Sort list of string in dictionary value by key.")
    public YailDictionary SortLetter(YailDictionary dictionary, String key) {
        Map<Object, Object> result = dictionary;
        Map<Object, List<Object>> newDictionary = new HashMap<>();
        for (Object k : dictionary.keySet()) {
            newDictionary.put(k, convert(dictionary.get(k)));
        }

        int len = newDictionary.get(key).size();
        for (int counter = 0; counter < newDictionary.get(key).size() - 1; counter++) {
            len--;
            for (int index = 0; index < len; index++) {
                Map<Object, Object> lastValue = new HashMap<>();
                for (Object k : dictionary.keySet()) {
                    lastValue.put(k, newDictionary.get(k).get(index));
                }

                Object newValue = newDictionary.get(key).get(index + 1);
                int a = compareTo(lastValue.get(key).toString().toLowerCase(), newValue.toString().toLowerCase());
                if (a > 0) {
                    for (Object k : dictionary.keySet()) {
                        newDictionary.replace(k, moveList(index, newDictionary.get(k), lastValue.get(k)));
                    }
                }
            }
        }

        for (Object k : dictionary.keySet()) {
            result.replace(k, newDictionary.get(k));
        }

        return YailDictionary.makeDictionary(result);
    }

    @SimpleFunction(description = "Sort list of integer in dictionary value by key.")
    public YailDictionary SortDigit(YailDictionary dictionary, String key) {
        Map<Object, Object> result = dictionary;
        Map<Object, List<Object>> newDictionary = new HashMap<>();
        for (Object k : dictionary.keySet()) {
            newDictionary.put(k, convert(dictionary.get(k)));
        }

        int len = newDictionary.get(key).size();
        for (int counter = 0; counter < newDictionary.get(key).size() - 1; counter++) {
            len--;
            for (int index = 0; index < len; index++) {
                Map<Object, Object> lastValue = new HashMap<>();
                for (Object k : dictionary.keySet()) {
                    lastValue.put(k, newDictionary.get(k).get(index));
                }

                Object newValue = newDictionary.get(key).get(index + 1);
                int a = Integer.valueOf(lastValue.get(key).toString());
                int b = Integer.valueOf(newValue.toString());
                if (a > b) {
                    for (Object k : dictionary.keySet()) {
                        newDictionary.replace(k, moveList(index, newDictionary.get(k), lastValue.get(k)));
                    }
                }
            }
        }

        for (Object k : dictionary.keySet()) {
            result.replace(k, newDictionary.get(k));
        }

        return YailDictionary.makeDictionary(result);
    }

    @SimpleFunction(description = "Sort list by index of given list in dictionary value by key.")
    public YailDictionary SortByListIndex(YailDictionary dictionary, String key, YailList list) {
        Map<Object, Object> result = dictionary;
        Map<Object, List<Object>> newDictionary = new HashMap<>();
        for (Object k : dictionary.keySet()) {
            newDictionary.put(k, convert(dictionary.get(k)));
        }

        int len = newDictionary.get(key).size();
        for (int counter = 0; counter < newDictionary.get(key).size() - 1; counter++) {
            len--;
            for (int index = 0; index < len; index++) {
                Map<Object, Object> lastValue = new HashMap<>();
                for (Object k : dictionary.keySet()) {
                    lastValue.put(k, newDictionary.get(k).get(index));
                }

                Object newValue = newDictionary.get(key).get(index + 1);
                int a = getIndex(list, lastValue.get(key));
                int b = getIndex(list, newValue);
                if (a > b) {
                    for (Object k : dictionary.keySet()) {
                        newDictionary.replace(k, moveList(index, newDictionary.get(k), lastValue.get(k)));
                    }
                }
            }
        }

        for (Object k : dictionary.keySet()) {
            result.replace(k, newDictionary.get(k));
        }

        return YailDictionary.makeDictionary(result);
    }

    @SimpleFunction(description = "Sort list of date in dictionary value by key.")
    public YailDictionary SortDate(YailDictionary dictionary, String key, String pattern) {
        Map<Object, Object> result = dictionary;
        Map<Object, List<Object>> newDictionary = new HashMap<>();
        for (Object k : dictionary.keySet()) {
            newDictionary.put(k, convert(dictionary.get(k)));
        }

        int len = newDictionary.get(key).size();
        for (int counter = 0; counter < newDictionary.get(key).size() - 1; counter++) {
            len--;
            for (int index = 0; index < len; index++) {
                Map<Object, Object> lastValue = new HashMap<>();
                for (Object k : dictionary.keySet()) {
                    lastValue.put(k, newDictionary.get(k).get(index));
                }

                Object newValue = newDictionary.get(key).get(index + 1);
                int newDay = getPattern(newValue.toString(), pattern, 'd');
                int newMonth = getPattern(newValue.toString(), pattern, 'M');
                int newYear = getPattern(newValue.toString(), pattern, 'y');

                Object lastVal = lastValue.get(key).toString();
                int lastDay = getPattern(lastVal.toString(), pattern, 'd');
                int lastMonth = getPattern(lastVal.toString(), pattern, 'M');
                int lastYear = getPattern(lastVal.toString(), pattern, 'y');

                int day = Dates.Day(Dates.Now());
                int month = Dates.Month(Dates.Now()) - 1;
                int year = Dates.Year(Dates.Now());

                if ((lastYear - year) < (newYear - year)) {
                    for (Object k : dictionary.keySet()) {
                        newDictionary.replace(k, moveList(index, newDictionary.get(k), lastValue.get(k)));
                    }
                } else if ((lastYear - year) == (newYear - year)) {
                    if ((lastMonth - month) < (newMonth - month)) {
                        for (Object k : dictionary.keySet()) {
                            newDictionary.replace(k, moveList(index, newDictionary.get(k), lastValue.get(k)));
                        }
                    } else if ((lastMonth - month) == (newMonth - month)) {
                        if ((lastDay - day) < (newDay - day)) {
                            for (Object k : dictionary.keySet()) {
                                newDictionary.replace(k, moveList(index, newDictionary.get(k), lastValue.get(k)));
                            }
                        }
                    }
                }
            }
        }

        for (Object k : dictionary.keySet()) {
            result.replace(k, newDictionary.get(k));
        }

        return YailDictionary.makeDictionary(result);
    }

    @SimpleFunction(description = "To make dictionary from string.")
    public YailDictionary StringToDictionary(String string, String separator, YailList keys) {
        Object[] key = keys.toArray();
        Object[] value = string.split(separator);
        Map<Object, Object> dictionary = new HashMap<Object, Object>();
        for (int i = 0; i < key.length; i++) {
            dictionary.put(key[i], value[i]);
        }

        return YailDictionary.makeDictionary(dictionary);
    }

    @SimpleFunction(description = "To make dictionary with list value from list.")
    public YailDictionary ListToDictionary(YailList list, String separator, YailList keys) {
        String[] string = list.toStringArray();
        Object[] key = keys.toArray();
        Object[][] data = new Object[string.length][string[0].split(separator).length];
        Map<Object, Object> dictionary = new HashMap<>();

        for (int i = 0; i < string.length; i++) {
            Object[] value = string[i].split(separator);
            for (int x = 0; x < value.length; x++) {
                data[i][x] = value[x];
            }
        }

        for (int i = 0; i < key.length; i++) {
            List<Object> value = new ArrayList<>();
            for (int x = 0; x < data.length; x++) {
                value.add(data[x][i]);
            }
            dictionary.put(key[i], YailList.makeList(value));
        }

        return YailDictionary.makeDictionary(dictionary);
    }

    @SimpleFunction(description = "To get list of keys.")
    public YailList ListKeys(YailDictionary dictionary) {
        Object[] keys = dictionary.keySet().toArray();
        return YailList.makeList(keys);
    }

    private int getIndex(YailList list, Object target) {
        Object[] mList = list.toArray();
        for (int i = 0; i < mList.length; i++) {
            if (mList[i] == target) return i;
        }

        return 0;
    }

    private int getPattern(String date, String pattern, char regex) {
        int index = pattern.indexOf(regex);
        int len = 0;
        char[] chars = pattern.toCharArray();
        for (char c : chars) {
            if (c == regex) len++;
        }

        return Integer.valueOf(date.substring(index, index + len));
    }

    private int compareTo(String string1, String string2) {
        return string1.compareTo(string2);
    }

    private List<Object> moveList(int index, List<Object> list, Object replacement) {
        List<Object> result = list;
        result.set(index, result.get(index + 1));
        result.set(index + 1, replacement);
        return result;
    }

    private List<Object> convert(Object object) {
        List<Object> list = new ArrayList<>();
        if (object.getClass().isArray()) list = Arrays.asList((Object[]) object);
        else if (object instanceof Collection) list = new ArrayList<>((Collection<?>) object);

        return list;
    }
}
