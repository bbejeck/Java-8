package bbejeck.dates;


import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.time.OffsetTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * User: Bill Bejeck
 * Date: 3/11/14
 * Time: 10:16 PM
 */
public class DurationTest {

    @Test
    public void test_add_time(){
        Duration oneHourThirtyMinutes = Duration.ofHours(1).plusMinutes(30);
        OffsetTime now = OffsetTime.parse("10:15:30-05:00");
        OffsetTime later = now.plus(oneHourThirtyMinutes);
        assertThat(later.toString(),is("11:45:30-05:00"));

        Duration threeHours = Duration.parse("PT3H");
        LocalTime twoPM = LocalTime.parse("14:00:00");
        LocalTime fivePM = twoPM.plus(threeHours);
        assertThat(fivePM.toString(),is("17:00"));
    }

    @Test
    public void test_time_between(){
        LocalTime earlier = LocalTime.parse("09:30:25");
        LocalTime later = LocalTime.parse("15:33:47");
        Duration timeSpan = Duration.between(earlier,later);
        assertThat(timeSpan.toString(),is("PT6H3M22S"));
        assertThat(LocalTime.MIDNIGHT.plus(timeSpan).toString(),is("06:03:22"));
    }

    @Test
    public void test_subtract_time(){
        OffsetTime offsetTime = OffsetTime.parse("13:34:00+01:00");
        LocalTime earlier = LocalTime.parse("09:30:25");
        LocalTime later = LocalTime.parse("15:33:47");
        Duration timeSpan = Duration.between(earlier,later);
        OffsetTime adjustedOffsetTime = offsetTime.minus(timeSpan);
        assertThat(adjustedOffsetTime.toString(),is("07:30:38+01:00"));
    }
}
