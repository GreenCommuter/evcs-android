package org.evcs.android.util;

import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

/**
 * Handy methods for time calculations and constants.
 */
public final class TimeUtils {

    public static final int HOUR_UNIT = 1;

    private static final int HOUR_IN_MINUTES = 60;

    private static final int HALF_HOUR_IN_MINUTES = 60 / 2;

    public static final long MINUTE_IN_MILLISECONDS = 60000L;

    private TimeUtils() {}

    public static int truncateToHalfHours(@IntRange(from = 0, to = 60) int minutes) {
        return minutes < HALF_HOUR_IN_MINUTES ? 0 : HALF_HOUR_IN_MINUTES;
    }

    public static int minutesToHours(int minutes) {
        return (int) Math.floor(minutes /(double)HOUR_IN_MINUTES);
    }

    /**
        * Returns the current {@link DateTime} rounded to half hours.
        *
        * @param dateTime Date to round
        *
        * @return Rounded current time
     */
     public static DateTime truncateDateTimeToHalfHour(DateTime dateTime) {
        return trimDateTime(dateTime.withMinuteOfHour(TimeUtils.truncateToHalfHours(dateTime.getMinuteOfHour())));
     }

    public static DateTime trimDateTime(@Nullable DateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.withSecondOfMinute(0).withMillisOfSecond(0);
    }

    public static boolean sameDate(DateTime dateTime1, DateTime dateTime2) {
        return dateTime1.withTimeAtStartOfDay().equals(
               dateTime2.withTimeAtStartOfDay());
    }

    /**
     * Returns a {@link LocalDate} from a string.
     *
     * @param value String to parse
     * @return <b>null</b> if value is empty or an invalid format. {@link LocalDate} otherwise
     */
    public LocalDate dateFromString(@Nullable String value, @NonNull DateTimeFormatter formatter) {
        try {
            return TextUtils.isEmpty(value) ? null : LocalDate.parse(value, formatter);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
