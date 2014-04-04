package bbejeck.dates;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: Bill Bejeck
 * Date: 3/13/14
 * Time: 9:18 PM
 */
public class DateFormatTest {

    private LocalDate localDate = LocalDate.parse("2014-03-13");
    private DateTimeFormatter fullFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
    private DateTimeFormatter longFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
    private DateTimeFormatter mediumFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
    private DateTimeFormatter shortFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    private DateTimeFormatter fullDateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
    private DateTimeFormatter longDateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
    private DateTimeFormatter mediumDateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    private DateTimeFormatter shortDateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);


    @Test
    public void test_date_format() {

        assertThat(localDate.format(fullFormat), is("Thursday, March 13, 2014"));
        assertThat(localDate.format(longFormat), is("March 13, 2014"));
        assertThat(localDate.format(mediumFormat), is("Mar 13, 2014"));
        assertThat(localDate.format(shortFormat), is("3/13/14"));

    }

    @Test
    public void test_date_time_format() {
        LocalTime localTime = LocalTime.parse("14:23:33");
        LocalDateTime dateTime =  localDate.atTime(localTime);

        assertThat(dateTime.format(mediumDateTimeFormat), is("Mar 13, 2014 2:23:33 PM"));
        assertThat(dateTime.format(shortDateTimeFormat), is("3/13/14 2:23 PM"));

        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("America/New_York"));
        assertThat(zonedDateTime.format(fullDateTimeFormat),is("Thursday, March 13, 2014 2:23:33 PM EDT"));
        assertThat(zonedDateTime.format(longDateTimeFormat),is("March 13, 2014 2:23:33 PM EDT"));

    }
}
