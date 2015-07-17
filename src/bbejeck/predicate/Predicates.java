package bbejeck.predicate;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * User: Bill Bejeck
 * Date: 4/13/14
 * Time: 11:40 PM
 */
public class Predicates {


    public static <T> Predicate<T> or (Predicate<T> ... predicates){
        return Stream.of(predicates).reduce(Predicate::or).get();
    }

    public static <T> Predicate<T> and (Predicate<T> ... predicates){
        return Stream.of(predicates).reduce(Predicate::and).get();
    }
}
