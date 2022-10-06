package org.evcs.android.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TimeUtilsTest {

    private DateTimeFormatter mFormatter;

    @Before
    public void prepareTest() {
        mFormatter = DateTimeFormat.forPattern("HH:mm:ss");
    }

    @Test
    public void testTruncateDateTimeWithSeconds() {
        DateTime toTruncate = mFormatter.parseDateTime("15:34:45");
        DateTime toCompare = mFormatter.parseDateTime("15:30:00");

        assertThat(TimeUtils.truncateDateTimeToHalfHour(toTruncate)).isEqualTo(toCompare);
    }

    @Test
    public void testTruncateDateTimeQuarterHour() {
        DateTime toTruncate = mFormatter.parseDateTime("18:15:21");
        DateTime toCompare = mFormatter.parseDateTime("18:00:00");

        assertThat(TimeUtils.truncateDateTimeToHalfHour(toTruncate)).isEqualTo(toCompare);
    }

    @Test
    public void testTruncateDateWithOneSecondLeft() {
        DateTime toTruncate = mFormatter.parseDateTime("23:59:59");
        DateTime toCompare = mFormatter.parseDateTime("23:30:00");

        assertThat(TimeUtils.truncateDateTimeToHalfHour(toTruncate)).isEqualTo(toCompare);
    }
}
