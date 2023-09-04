package com.marchtech.StringFunctions;

public class MarchFunctions {
    public static String toSentenceCase(final String string) {
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0) chars[0] = Character.toUpperCase(chars[0]);
            else chars[i] = Character.toLowerCase(chars[i]);
        }

        return String.valueOf(chars);
    }

    public static String toCapitalize(String string) {
        string = string.toLowerCase();
        String[] arr = string.split(" ");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
        }

        return sb.toString().trim();
    }

    public static String toToggleCase(String string) {
        string = string.toUpperCase();
        String[] arr = string.split(" ");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toLowerCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
        }

        return sb.toString().trim();
    }
}
