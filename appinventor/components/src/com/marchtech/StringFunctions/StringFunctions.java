package com.marchtech.StringFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.ElementsUtil;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.appinventor.components.runtime.util.YailList;

import com.marchtech.Icon;
import com.marchtech.StringFunctions.helpers.Case;
import com.marchtech.StringFunctions.helpers.Random;

@DesignerComponent( version = 1,
                    description = "Extension to help you work with strings..",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class StringFunctions extends AndroidNonvisibleComponent {
    private String LETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private String DIGIT = "0123456789";
    private String SYMBOL = "`~!@#$%^&*()-=_+[]{}\\|;:'\",./<>? ";
    
    public StringFunctions(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Return true or false if string contains number.")
    public boolean isContainsNumber(String string) {
        char[] chars = string.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                return true;
            }
        }

        return false;
    }

    @SimpleFunction(description = "Return true or false if string contains symbol.")
    public boolean isContainsSymbol(String string) {
        char[] chars = string.toCharArray();
        for (char c : chars) {
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
        }

        return false;
    }

    @SimpleFunction(description = "Return true or false if string contains specific character.")
    public boolean isContainsSpecificCharacter(String string, String character) {
        char[] chars = string.toCharArray();
        for (char c : chars) {
            if (c == character.charAt(0)) {
                return true;
            }
        }

        return false;
    }

    @SimpleFunction(description = "Return true or false if string is a letter.")
    public boolean isLetter(String string) {
        char[] chars = string.toCharArray();
        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    @SimpleFunction(description = "Return true or false if string is a digit.")
    public boolean isDigit(String string) {
        char[] chars = string.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    @SimpleFunction(description = "Return true or false if string is a letter or digit.")
    public boolean isLetterOrDigit(String string) {
        char[] chars = string.toCharArray();
        for (char c : chars) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }

        return true;
    }

    @SimpleFunction(description = "Return true or false if string is a name.")
    public boolean isName(String string) {
        char[] chars = string.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                return false;
            } else if (!Character.isLetter(c) && c != ' ' && c != '.') return false;
        }

        return true;
    }

    @SimpleFunction(description = "Return true or false if string ends with specific character.")
    public boolean isEndsWith(String string, String character, boolean ignoreCapitalization) {
        if (ignoreCapitalization) {
            character = character.toLowerCase();
            string = string.toLowerCase();
        }

        String[] c = character.split("");

        if (string.endsWith(c[0])) return true;
        else return false;
    }

    @SimpleFunction(description = "Return true or false if string start with specific character.")
    public boolean isStartsWith(String string, String character, boolean ignoreCapitalization) {
        if (ignoreCapitalization) {
            character = character.toLowerCase();
            string = string.toLowerCase();
        }

        String[] c = character.split("");

        if (string.startsWith(c[0])) return true;
        else return false;
    }

    @SimpleFunction(description = "Return true or false if string is a valid email address.")
    public boolean isValidEmailAddress(String string) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        if (string == null) return false;
        return pattern.matcher(string).matches();
    }

    @SimpleFunction(description = "Return index of first specific character. return -1 if null")
    public int IndexOfSpecificCharacter(String string, String character, boolean ignoreCapitalization) {
        if (string == null) return -1;
        if (character == null) return -1;

        char c = character.charAt(0);
        if (ignoreCapitalization) {
            c = Character.toLowerCase(c);
            string = string.toLowerCase();
        }

        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) return i + 1;
        }

        return -1;
    }

    @SimpleFunction(description = "Return all of index with specific character.")
    public YailList IndexAllOfSpecificCharacter(String string, String character, boolean ignoreCapitalization) {
        if (string == null) return null;
        if (character == null) return null;

        List indexAll = new ArrayList<>();
        YailList indexList;
        char c = character.charAt(0);
        if (ignoreCapitalization) {
            c = Character.toLowerCase(c);
            string = string.toLowerCase();
        }

        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) indexAll.add(i + 1);
        }
        
        indexList = fromList(indexAll);
        return indexList;
    }

    @SimpleFunction(description = "Return the amount of the specific character appear.")
    public int CountCharacter(String string, String character, boolean ignoreCapitalization) {
        if (string == null) return -1;
        if (character == null) return -1;

        int count = 0;
        char c = character.charAt(0);
        if (ignoreCapitalization) {
            c = Character.toLowerCase(c);
            string = string.toLowerCase();
        }

        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) count++;
        }

        return count;
    }

    @SimpleFunction(description = "Return the changed case with the given parameters.")
    public @Options(Case.class) String ChangeCase(String string, @Options(Case.class) String toCase) {
        if (string == null) return null;
        if (toCase == null) return null;

        if (toCase == "Sentence case") {
            return MarchFunctions.toSentenceCase(string);
        } else if (toCase == "lowercase") {
            return string.toLowerCase();
        } else if (toCase == "UPPERCASE") {
            return string.toUpperCase();
        } else if (toCase == "Capitalize Each Word") {
            return MarchFunctions.toCapitalize(string);
        } else if (toCase == "tOGGLE cASE") {
            return MarchFunctions.toToggleCase(string);
        }

        return null;
    }

    @SimpleFunction(description = "Return the index of string from the list.")
    public int IndexFromList(String string, YailList list) {
        String[] array = list.toStringArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == string) return i + 1;
        }

        return -1;
    }

    @SimpleFunction(description = "Return the index of string from the list (from string).")
    public int IndexFromListString(String string, String listString) {
        YailList list = ElementsUtil.elementsFromString(listString);
        String[] array = list.toStringArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == string) return i + 1;
        }

        return -1;
    }

    @SimpleFunction(description = "Return the string of index from the list.")
    public String StringFromIndexOfList(int index, YailList list) {
        String[] array = list.toStringArray();
        return array[index - 1];
    }

    @SimpleFunction(description = "Return the string of index from the list (from string).")
    public String StringFromIndexOfListString(int index, String listString) {
        YailList list = ElementsUtil.elementsFromString(listString);
        String[] array = list.toStringArray();
        return array[index - 1];
    }

    @SimpleFunction(description = "Return integer 1 digit to 2 digits. e.g \"1\" to \"01\".")
    public String TwoDigit(int value) {
        if (value < 10) return "0" + String.valueOf(value);
        else return String.valueOf(value);
    }

    @SimpleFunction(description = "Return value from firebase in GotValue function.")
    public String GetFirebaseValue(String value, String segment, String replacement) {
        value = value.replaceAll("\"", "");
        if (segment != "" || segment != null) {
            if (value.contains(segment)) value = value.replaceAll(segment, replacement);
        }

        return value;
    }

    @SimpleFunction(description = "Return list from firebase value in GotValue function.")
    public YailList FirebaseValueToList(String value) {
        /*
        value = value.substring(1, value.length() - 1);
        char[] chars = value.toCharArray();
        int max = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"') max++;
        }

        int[] index = new int[max];
        int len = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"') {
                index[len] = i;
                len++;
            }
        }

        int count = 0;
        Object[] data = new Object[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[count] = value.substring(index[i] + 1, index[i + 1]);
            count++;
        }
        */
        JSONArray array = new JSONArray(value);
        Object[] data = new Object[array.length()];
        for (int i = 0; i < array.length(); i++) {
            if (array.get(i).toString().startsWith(" ")) {
                String a = array.getString(i);
                a = a.substring(1, a.length());
                data[i] = a;
            } else data[i] = array.get(i);
        }

        YailList result = YailList.makeList(data);
        return result;
    }

    @SimpleFunction(description = "Return random string from specific mode.")
    public @Options(Random.class) String RandomString(@Options(Random.class) String mode, int length) {
        String string = LETTER;
        if (mode == "digit") string = DIGIT;
        else if (mode == "symbol") string = SYMBOL;
        else if (mode == "letter_digit") string = LETTER + DIGIT;
        else if (mode == "letter_symbol") string = LETTER + SYMBOL;
        else if (mode == "digit_symbol") string = DIGIT + SYMBOL;
        else if (mode == "all") string = LETTER + DIGIT + SYMBOL;

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int)(string.length() * Math.random());
            result.append(string.charAt(index));
        }

        return result.toString();
    }

    @SimpleFunction(description = "Return dictionary from string (json).")
    public YailDictionary StringToDictionary(String string) {
        string = string.substring(1, string.length() - 1);
        string = string.replaceAll("\"", "");
        YailDictionary dictionary = new YailDictionary();
        String[] splited = string.split(",");
        if (splited.length > 0) {
            for (String item : splited) {
                Object[] mSplited = item.split(":");
                Object key = mSplited[0];
                if (mSplited[1].toString().contains("[")) {
                    Object[] list = mSplited[1].toString().split(",");
                    dictionary.put(key, YailList.makeList(list));
                } else {
                    Object val = mSplited[1];
                    dictionary.put(key, val);
                }
            }
        } else {
            Object[] mSplited = string.split(":");
            Object key = mSplited[0];
            if (mSplited[1].toString().contains("[")) {
                Object[] list = mSplited[1].toString().split(",");
                dictionary.put(key, YailList.makeList(list));
            } else {
                Object val = mSplited[1];
                dictionary.put(key, val);
            }
        }

        return dictionary;
    }

/*
    @SimpleFunction(description = "Return a new text obtained by replacing all occurences of the segments with the replacements after Extracts the segment of the given length from the given text starting from the given text starting from the given position. Position 1 denotes the beginning of the text.")
    public String SegmentAndReplaceAllText(String string, int start, int length, String segment, String replacement) {
        String[] array = string.split("");
        String text = "";
        int len = segment.length();
        for (int i = start - 1; i < start + length; i++) {
            text += array[i];
        }

        array = text.split("");
        for (int i = 0; i < array.length; i++) {
            if ((array[i]))
        }
    }
*/
    private static YailList fromList(List item) {
        YailList items = new YailList();
        items = YailList.makeList(item);

        return items;
    }
}
