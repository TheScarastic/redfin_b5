package com.google.android.material.datepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/* loaded from: classes.dex */
public class DateStrings {
    public static String getDateString(long j) {
        return getDateString(j, null);
    }

    public static String getMonthDay(long j, Locale locale) {
        return UtcDates.getAndroidFormat("MMMd", locale).format(new Date(j));
    }

    public static String getYearMonthDay(long j, Locale locale) {
        return UtcDates.getAndroidFormat("yMMMd", locale).format(new Date(j));
    }

    public static String getDateString(long j, SimpleDateFormat simpleDateFormat) {
        Calendar todayCalendar = UtcDates.getTodayCalendar();
        Calendar utcCalendar = UtcDates.getUtcCalendar();
        utcCalendar.setTimeInMillis(j);
        if (simpleDateFormat != null) {
            return simpleDateFormat.format(new Date(j));
        }
        if (todayCalendar.get(1) == utcCalendar.get(1)) {
            return getMonthDay(j, Locale.getDefault());
        }
        return getYearMonthDay(j, Locale.getDefault());
    }
}
