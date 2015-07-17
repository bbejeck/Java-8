package bbejeck.function;

import org.junit.Test;

import java.util.function.BinaryOperator;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * User: Bill Bejeck
 * Date: 7/17/15
 * Time: 11:12 AM
 */
public class PartiallyAppliedFunctionsTest {

    Function<Integer,Function<Integer,Function<BinaryOperator<Integer>,Integer>>> someComputation = i1 -> i2 -> f -> f.apply(i1,i2);

    BinaryOperator<Integer> mult = (i,j) -> i * j;
    BinaryOperator<Integer> divide = (i,j) -> i / j;
    BinaryOperator<Integer> sumSquares = (i,j) -> (i*i) + (j*j);

    int first = 10;
    int second = 5;

    Function<Integer,Function<BinaryOperator<Integer>,Integer>> partial1 = someComputation.apply(first);
    Function<BinaryOperator<Integer>,Integer> partial2 = partial1.apply(second);


    @Test
    public void test_multiplication(){
        assertThat(partial2.apply(mult),is(50));
    }

    @Test
    public void test_divide(){
        assertThat(partial2.apply(divide),is(2));
    }

    @Test
    public void test_sum_squares(){
        assertThat(partial2.apply(sumSquares),is(125));
    }

}
