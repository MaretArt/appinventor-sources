package com.marchtech.SimplePedometer;

import java.text.DecimalFormat;

import static com.marchtech.SimplePedometer.SimplePedometer.INTERVAL_VARIATION;
import static com.marchtech.SimplePedometer.SimplePedometer.WIN_SIZE;

public class Functions {
    public boolean areStepsEquallySpaced(long[] stepInterval) {
        float avg = 0;
        int num = 0;
        for (long interval : stepInterval) {
            if (interval > 0) {
                num++;
                avg += interval;
            }
        }

        avg = avg / num;
        for (long interval : stepInterval) {
            if (Math.abs(interval - avg) > INTERVAL_VARIATION) return false;
        }

        return true;
    }

    public boolean isPeak(int winPos, float[] lastValues) {
        int mid = (winPos + WIN_SIZE / 2) % WIN_SIZE;
        for (int i = 0; i < WIN_SIZE; i++) {
            if (i != mid && lastValues[i] > lastValues[mid]) return false;
        }

        return true;
    }

    public boolean isValley(int winPos, float[] lastValues) {
        int mid = (winPos + WIN_SIZE / 2) % WIN_SIZE;
        for (int i = 0; i < WIN_SIZE; i++) {
            if (i != mid && lastValues[i] < lastValues[mid]) return false;
        }

        return true;
    }

    public float formatFloat(float data, DecimalFormat decimalFormat) {
        String newData = decimalFormat.format(data);
        char[] chars = newData.toCharArray();
        for (int i = 0; i < chars.length; i++) if (chars[i] == ',') chars[i] = '.';
        return Float.parseFloat(String.valueOf(chars));
    }
}
