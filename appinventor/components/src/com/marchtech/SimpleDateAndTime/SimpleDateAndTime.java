package com.marchtech.SimpleDateAndTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.Dates;
import com.google.appinventor.components.runtime.util.ErrorMessages;

import com.marchtech.Icon;
import com.marchtech.SimpleDateAndTime.helpers.DateTime;
import com.marchtech.SimpleDateAndTime.helpers.Time;

@DesignerComponent(version = 1, description = "Extension to help using the date and time.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
public class SimpleDateAndTime extends AndroidNonvisibleComponent {
    public SimpleDateAndTime(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleEvent
    public void ErrorOccurred(String message) {
        EventDispatcher.dispatchEvent(this, "ErrorOccured", message);
    }

    @SimpleFunction(description = "To get date of today.")
    public String GetToday(String pattern) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    @SimpleFunction(description = "To get date of yesterday.")
    public String GetYesterday(String pattern) {
        int day = Calendar.getInstance().get(Calendar.DATE) - 1;
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    @SimpleFunction(description = "To get date of tomorrow.")
    public String GetTomorrow(String pattern) {
        int day = Calendar.getInstance().get(Calendar.DATE) + 1;
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    @SimpleFunction(description = "To get instant of today.")
    public Calendar Today() {
        int day = Dates.Day(Dates.Now());
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        try {
            GregorianCalendar cal = new GregorianCalendar(year, month, day);
            cal.setLenient(false);
            cal.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "Today", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        Calendar calendar = Dates.DateInstant(year, month, day);
        return calendar;
    }

    @SimpleFunction(description = "To get instant of yesterday.")
    public Calendar Yesterday() {
        int day = Dates.Day(Dates.Now()) - 1;
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        try {
            GregorianCalendar cal = new GregorianCalendar(year, month, day);
            cal.setLenient(false);
            cal.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "Yesterday", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        Calendar calendar = Dates.DateInstant(year, month, day);
        return calendar;
    }

    @SimpleFunction(description = "To get instant of tomorrow.")
    public Calendar Tomorrow() {
        int day = Dates.Day(Dates.Now()) + 1;
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        try {
            GregorianCalendar cal = new GregorianCalendar(year, month, day);
            cal.setLenient(false);
            cal.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "Tomorrow", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        Calendar calendar = Dates.DateInstant(year, month, day);
        return calendar;
    }

    @SimpleFunction(description = "To get instant of today.")
    public Calendar TodayWithTime(int hour, int minute, int second) {
        int day = Dates.Day(Dates.Now());
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        Calendar calendar = null;
        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "TodayWithTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "TodayWithTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction(description = "To get instant of yesterday.")
    public Calendar YesterdayWithTime(int hour, int minute, int second) {
        int day = Dates.Day(Dates.Now()) - 1;
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        Calendar calendar = null;
        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "YesterdayWithTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "YesterdayWithTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction(description = "To get instant of tomorrow.")
    public Calendar TomorrowWithTime(int hour, int minute, int second) {
        int day = Dates.Day(Dates.Now()) + 1;
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        Calendar calendar = null;
        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "TomorrowWithTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "TomorrowWithTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction(description = "To get instant of today.")
    public Calendar TodayWithStringTime(String time, String separator) {
        String[] separated = time.split(separator);
        int hour = Integer.parseInt(separated[0]);
        int minute = Integer.parseInt(separated[1]);
        int second = 0;
        if (separated.length > 2)
            second = Integer.parseInt(separated[2]);

        int day = Dates.Day(Dates.Now());
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        Calendar calendar = null;
        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "TodayWithStringTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "TodayWithStringTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction(description = "To get instant of yesterday.")
    public Calendar YesterdayWithStringTime(String time, String separator) {
        String[] separated = time.split(separator);
        int hour = Integer.parseInt(separated[0]);
        int minute = Integer.parseInt(separated[1]);
        int second = 0;
        if (separated.length > 2)
            second = Integer.parseInt(separated[2]);

        int day = Dates.Day(Dates.Now()) - 1;
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        Calendar calendar = null;
        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "YesterdayWithStringTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "YesterdayWithStringTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction(description = "To get instant of tomorrow.")
    public Calendar TomorrowWithStringTime(String time, String separator) {
        String[] separated = time.split(separator);
        int hour = Integer.parseInt(separated[0]);
        int minute = Integer.parseInt(separated[1]);
        int second = 0;
        if (separated.length > 2)
            second = Integer.parseInt(separated[2]);

        int day = Dates.Day(Dates.Now()) + 1;
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        Calendar calendar = null;
        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "TomorrowWithStringTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "TomorrowWithStringTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction
    public Calendar MakeInstantFromTime(int hour, int minute, int second, boolean checkToday) {
        int day;
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        if (checkToday) {
            if (Dates.Hour(Dates.Now()) < hour)
                day = Dates.Day(Dates.Now());
            else {
                if (Dates.Minute(Dates.Now()) < minute)
                    day = Dates.Day(Dates.Now());
                else {
                    if (Dates.Second(Dates.Now()) < second)
                        day = Dates.Day(Dates.Now());
                    else {
                        day = Dates.Day(Dates.Now()) + 1;
                    }
                }
            }
        } else
            day = Dates.Day(Dates.Now());

        Calendar calendar = null;
        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "MakeInstantFromTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "MakeInstantFromTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction
    public Calendar MakeInstantFromStringTime(String time, String separator, boolean checkToday) {
        String[] separated = time.split(separator);
        int hour = Integer.parseInt(separated[0]);
        int minute = Integer.parseInt(separated[1]);
        int second = 0;
        if (separated.length > 2)
            second = Integer.parseInt(separated[2]);

        int day;
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        if (checkToday) {
            if (Dates.Hour(Dates.Now()) < hour)
                day = Dates.Day(Dates.Now());
            else {
                if (Dates.Minute(Dates.Now()) < minute)
                    day = Dates.Day(Dates.Now());
                else {
                    if (Dates.Second(Dates.Now()) < second)
                        day = Dates.Day(Dates.Now());
                    else {
                        day = Dates.Day(Dates.Now()) + 1;
                    }
                }
            }
        } else
            day = Dates.Day(Dates.Now());

        Calendar calendar = null;
        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "MakeInstantFromStringTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "MakeInstantFromStringTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction
    public Calendar MakeInstantFromDate(Calendar instant, Calendar timeIfToday, Calendar timeIfTomorrow) {
        int day = Dates.Day(Dates.Now());
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (day == Dates.Day(instant) && month == (Dates.Month(instant) + 1) && year == Dates.Year(instant)) {
            hour = Dates.Hour(timeIfToday);
            minute = Dates.Minute(timeIfToday);
            second = Dates.Second(timeIfToday);
        } else {
            hour = Dates.Hour(timeIfTomorrow);
            minute = Dates.Minute(timeIfTomorrow);
            second = Dates.Second(timeIfTomorrow);
        }

        Calendar calendar = null;
        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "MakeInstantFromDate", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "MakeInstantFromDate", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction(description = "Return name of time \"MORNING\", \"DAY\", \"AFTERNOON\", \"EVENING\" or \"NIGHT\".")
    public String TimeName(Calendar instant) {
        String[] name = new String[] { "MORNING", "DAY", "AFTERNOON", "EVENING", "NIGHT" };
        int hour = Dates.Hour(instant);
        if (hour >= 5 && hour < 11)
            return name[0];
        else if (hour >= 11 && hour < 15)
            return name[1];
        else if (hour >= 15 && hour < 18)
            return name[2];
        else if (hour >= 18 && hour < 0)
            return name[3];
        else
            return name[4];
    }

    @SimpleFunction(description = "Return splitted time from string.")
    public @Options(Time.class) int SplitTime(@Options(Time.class) String time, String string, String separator) {
        String[] array = string.split(separator);
        if (time == null)
            return -1;
        if (array.length <= 1)
            return 1;

        if (time == "hour")
            return Integer.parseInt(array[0]);
        else if (time == "minute")
            return Integer.parseInt(array[1]);
        else {
            if (array.length < 3)
                return -1;
            else
                return Integer.parseInt(array[2]);
        }
    }

    @SimpleFunction(description = "Returns text representing the date and time of an instant in the specified pattern.")
    public String DateAndTime(Calendar instant, String pattern) {
        try {
            return Dates.FormatDateTime(instant, pattern);
        } catch (IllegalArgumentException e) {
            throw new YailRuntimeError(
                    "Illegal argument for pattern in SimpleDateAndTime.FormatDateTime. Acceptable values are empty string, MM/dd/YYYY hh:mm:ss a, MMM d, yyyy HH:mm "
                            + "For all possible patterns, see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html",
                    "Sorry to be so picky.");
        }
    }

    @SimpleFunction(description = "Text representing the date of an instant in the specified pattern.")
    public String Date(Calendar instant, String pattern) {
        try {
            return Dates.FormatDate(instant, pattern);
        } catch (IllegalArgumentException e) {
            throw new YailRuntimeError(
                    "Illegal argument for pattern in SimpleDateAndTime.FormatDate. Acceptable values are empty string, MM/dd/YYYY, or MMM d, yyyy. "
                            + "For all possible patterns, see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html",
                    "Sorry to be so picky.");
        }
    }

    @SimpleFunction(description = "Return true or false if is today.")
    public boolean isToday(Calendar instant) {
        if (Dates.Day(instant) == Dates.Day(Dates.Now()) && Dates.Month(instant) == (Dates.Month(Dates.Now()))
                && Dates.Year(instant) == Dates.Year(Dates.Now()))
            return true;
        else
            return false;
    }

    @SimpleFunction(description = "Return true or false if is yesterday.")
    public boolean isYesterday(Calendar instant) {
        int day = Dates.Day(Dates.Now());
        int day1 = Dates.Day(instant);
        int month = Dates.Month(Dates.Now());
        int month1 = Dates.Month(instant);
        int year = Dates.Year(Dates.Now());
        int year1 = Dates.Year(instant);

        if (year1 < year)
            return true;
        else if (year1 == year) {
            if (month1 < month)
                return true;
            else if (month1 == month) {
                if (day1 < day)
                    return true;
            }
        }

        return false;
    }

    @SimpleFunction(description = "Return hour, minute or second.")
    public @Options(DateTime.class) int GetNow(@Options(DateTime.class) String dateTime) {
        if (dateTime == "day")
            return Dates.Day(Dates.Now());
        else if (dateTime == "month")
            return Dates.Month(Dates.Now()) + 1;
        else if (dateTime == "year")
            return Dates.Year(Dates.Now());
        else if (dateTime == "hour")
            return Dates.Hour(Dates.Now());
        else if (dateTime == "minute")
            return Dates.Minute(Dates.Now());
        else
            return Dates.Second(Dates.Now());
    }

    @SimpleFunction(description = "Return the most recent of two dates.")
    public Calendar RecentDate(String date1, String date2, String pattern) {
        int day1 = getPattern(date1, pattern, 'd');
        int month1 = getPattern(date1, pattern, 'M');
        int year1 = getPattern(date1, pattern, 'y');
        Calendar calendar1 = Dates.DateInstant(year1, month1, day1);

        int day2 = getPattern(date2, pattern, 'd');
        int month2 = getPattern(date2, pattern, 'M');
        int year2 = getPattern(date2, pattern, 'y');
        Calendar calendar2 = Dates.DateInstant(year2, month2, day2);

        if (year1 > year2)
            return calendar1;
        else if (year1 < year2)
            return calendar2;
        else {
            if (month1 > month2)
                return calendar1;
            else if (month1 < month2)
                return calendar2;
            else {
                if (day1 > day2)
                    return calendar1;
                else if (day1 < day2)
                    return calendar2;
                else {
                    return calendar1;
                }
            }
        }
    }

    @SimpleFunction(description = "To get date and time in string with specific pattern.")
    public Calendar GetDateAndTime(String string, String pattern) {
        int day = Dates.Day(Dates.Now());
        int month = Dates.Month(Dates.Now()) + 1;
        int year = Dates.Year(Dates.Now());
        int hour = Dates.Hour(Dates.Now());
        int minute = Dates.Minute(Dates.Now());
        int second = Dates.Second(Dates.Now());
        Calendar calendar = null;

        if (pattern.contains("d"))
            day = getPattern(string, pattern, 'd');
        if (pattern.contains("M"))
            month = getPattern(string, pattern, 'M');
        if (pattern.contains("y"))
            year = getPattern(string, pattern, 'y');
        if (pattern.contains("h"))
            hour = getPattern(string, pattern, 'h');
        if (pattern.contains("m"))
            minute = getPattern(string, pattern, 'm');
        if (pattern.contains("s"))
            second = getPattern(string, pattern, 's');

        try {
            calendar = new GregorianCalendar(year, month, day);
            calendar.setLenient(false);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "GetDateAndTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        calendar = Dates.DateInstant(year, month, day);

        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "GetDateAndTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return calendar;
    }

    @SimpleFunction(description = "Returns an instant in time some seconds before the given instant.")
    public Calendar ReduceSeconds(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        int second = Dates.Second(newInstant);
        int minute = Dates.Minute(newInstant);
        int hour = Dates.Hour(newInstant);

        if (quantity > Dates.Second(newInstant)) {
            second -= quantity;
            while (second < 0) {
                minute--;
                if (minute < 0) {
                    minute = 59;
                    hour--;
                    if (hour < 0)
                        hour = 23;
                }

                second = 60 + second;
            }
        } else
            second -= quantity;

        try {
            newInstant.set(Calendar.HOUR_OF_DAY, hour);
            newInstant.set(Calendar.MINUTE, minute);
            newInstant.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "ReduceSeconds", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return newInstant;
    }

    @SimpleFunction(description = "Returns an instant in time some minutes before the given instant.")
    public Calendar ReduceMinutes(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        int minute = Dates.Minute(newInstant);
        int hour = Dates.Hour(newInstant);

        if (quantity > Dates.Minute(newInstant)) {
            minute -= quantity;
            while (minute < 0) {
                hour--;
                if (hour < 0)
                    hour = 23;

                minute = 60 + minute;
            }
        } else
            minute -= quantity;

        try {
            newInstant.set(Calendar.HOUR_OF_DAY, hour);
            newInstant.set(Calendar.MINUTE, minute);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "ReduceMinutes", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return newInstant;
    }

    @SimpleFunction(description = "Returns an instant in time some hour before the given instant.")
    public Calendar ReduceHours(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        int hour = Dates.Hour(newInstant);

        if (quantity > Dates.Hour(newInstant)) {
            hour -= quantity;
            while (hour < 0) {
                hour = 24 + hour;
            }
        } else
            hour -= quantity;

        try {
            newInstant.set(Calendar.HOUR_OF_DAY, hour);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "ReduceHours", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return newInstant;
    }

    @SimpleFunction(description = "Returns an instant with specific time in the given instant.")
    public Calendar SetTime(Calendar instant, int hour, int minute, int second) {
        Calendar newInstant = (Calendar) instant.clone();

        if (second > 59) {
            while (second > 59) {
                minute++;
                if (minute > 59) {
                    minute = 0;
                    hour++;
                    if (hour > 23)
                        hour = 0;
                }

                second = Math.abs(60 - second);
            }
        } else if (minute > 59) {
            while (minute > 59) {
                hour++;
                if (hour > 23)
                    hour = 0;

                minute = Math.abs(60 - minute);
            }
        } else if (hour > 23) {
            while (hour > 23) {
                hour = Math.abs(24 - hour);
            }
        }

        try {
            newInstant.set(Calendar.HOUR_OF_DAY, hour);
            newInstant.set(Calendar.MINUTE, minute);
            newInstant.set(Calendar.SECOND, second);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, "SetTime", ErrorMessages.ERROR_ILLEGAL_DATE);
        }

        return newInstant;
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
}
