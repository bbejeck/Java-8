package bbejeck.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * User: Bill Bejeck
 * Date: 10/12/14
 * Time: 1:32 PM
 */
public class CustomCollectors {
    private static final BinaryOperator NOOP_COMBINER = (l, r) -> {
        throw new RuntimeException("Parallel execution not supported");
    };

    @SuppressWarnings("unchecked")
    public static <T, R> Collector<T, R, R> of(Supplier<R> container, BiConsumer<R, T> accumulator) {
        return Collector.of(container, accumulator, NOOP_COMBINER);
    }


    public static <T> Collector<Optional<T>, List<T>, List<T>> optionalToList() {

        return optionalValuesList((l, v) -> v.ifPresent(l::add));
    }


    public static <T> Collector<Optional<T>, List<T>, List<T>> optionalToList(T defaultValue) {

        return optionalValuesList((l, v) -> l.add(v.orElse(defaultValue)));
    }

    private static <T> Collector<Optional<T>, List<T>, List<T>> optionalValuesList(BiConsumer<List<T>, Optional<T>> accumulator) {
        Supplier<List<T>> supplier = ArrayList::new;

        BinaryOperator<List<T>> combiner = (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        };

        Function<List<T>, List<T>> finisher = l1 -> l1;

        return Collector.of(supplier, accumulator, combiner, finisher);

    }


}
