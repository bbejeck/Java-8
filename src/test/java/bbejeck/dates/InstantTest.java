package bbejeck.dates;


import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
/**
 * User: Bill Bejeck
 * Date: 3/12/14
 * Time: 10:34 PM
 */
public class InstantTest {

    @Test
    public void test_create_instant()throws Exception {
        Instant start = Instant.now();
        Thread.sleep(2000l);
        Instant end = Instant.now();
        long difference = start.until(end, ChronoUnit.SECONDS);
        assertThat(difference,is(2L));
    }

    @Test
    public void test_ensure_completed_by() throws Exception {
        Instant start = Instant.now();
        Instant deadline = start.plusSeconds(2);
        //Simulated task
        Thread.sleep(1500);
        Instant simulateCompletedTask = Instant.now();
        assertThat(simulateCompletedTask.isBefore(deadline),is(true));


    }


}
