package bbejeck.function;

import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * User: Bill Bejeck
 * Date: 7/17/15
 * Time: 10:34 AM
 */
public class PartialFunctions {


    Function<Integer,Function<Integer,Function<BinaryOperator<Integer>,Integer>>> someComputation = i1 -> i2 -> f -> f.apply(i1,i2);

    BinaryOperator<Integer> mult = (i,j) -> i * j;
    BinaryOperator<Integer> divide = (i,j) -> i / j;
    BinaryOperator<Integer> sumSquares = (i,j) -> (i*i) + (j*j);



}
