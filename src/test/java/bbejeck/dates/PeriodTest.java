package bbejeck.dates;

import org.junit.Test;

import java.time.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
/**
 * User: Bill Bejeck
 * Date: 3/11/14
 * Time: 11:36 PM
 */
public class PeriodTest {

    @Test
    public void test_add_days(){
        LocalDateTime today = LocalDateTime.parse("2014-03-12T19:36:33");
        Period sixDays = Period.ofDays(6);
        LocalDateTime nextWeek = today.plus(sixDays);
        assertThat(nextWeek.toString(),is("2014-03-18T19:36:33"));
    }

    @Test
    public void test_period_between_dates(){
        LocalDate twins = LocalDate.parse("2003-11-18");
        LocalDate mayhem = LocalDate.parse("2009-06-01");
        Period timeBetween = Period.between(twins,mayhem);
        assertThat(timeBetween.getYears(),is(5));
        assertThat(timeBetween.getMonths(),is(6));
        assertThat(timeBetween.getDays(),is(14));
    }

    @Test
    public void test_add_months(){
        Period threeMonthSpan = Period.parse("P3M22D");
        LocalDate today = LocalDate.parse("2014-03-12");
        LocalDate future = today.plus(threeMonthSpan);
        assertThat(future.toString(),is("2014-07-04"));
    }


    @Test
    public void test_subtract_days(){
        LocalDate today = LocalDate.parse("2014-03-12");
        Period twoWeeks = Period.ofWeeks(2);
        LocalDate past = today.minus(twoWeeks);
        assertThat(past.toString(),is("2014-02-26"));
    }

    @Test
    public void test_adjust_period(){
        Period oneDay = Period.parse("P1D");
        Period oneMonth = oneDay.plusMonths(1);
        assertThat(oneMonth.toString(),is("P1M1D"));
        Period oneYear = oneMonth.plusYears(1);
        assertThat(oneYear.toString(),is("P1Y1M1D"));
    }


}
