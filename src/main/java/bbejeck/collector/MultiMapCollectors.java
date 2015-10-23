package bbejeck.collector;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * User: Bill Bejeck
 * Date: 10/20/15
 * Time: 10:31 PM
 */
public class MultiMapCollectors {

     public static <K,T> ListMulitmapCollector<K,T> listMultimap(Function<T, K> keyFunction) {

         return new ListMulitmapCollector<>(keyFunction);
     }

     public static <K,T> HashSetMulitmapCollector<K,T> setMulitmap(Function<T, K> keyFunction) {
         return new HashSetMulitmapCollector<>(keyFunction);
     }

     public static <K,T> LinkedListMulitmapCollector<K,T> linkedListMulitmap(Function<T,K> keyFunction) {
         return new LinkedListMulitmapCollector<>(keyFunction);
     }


    private static class ListMulitmapCollector<K,T> extends MultimapCollector {


        private ListMulitmapCollector(Function<T, K> keyFunction) {
            super(keyFunction);
        }

        @Override
        public Supplier<ArrayListMultimap<K,T>> supplier() {
            return ArrayListMultimap::create;

        }
    }


    private static class HashSetMulitmapCollector<K,T> extends MultimapCollector {

        private HashSetMulitmapCollector(Function<T, K> keyFunction) {
            super(keyFunction);
        }

        @Override
        public Supplier<HashMultimap<K,T>> supplier() {
            return HashMultimap::create;

        }
    }

    private static class LinkedListMulitmapCollector<K,T> extends MultimapCollector {

        private LinkedListMulitmapCollector(Function<T, K> keyFunction) {
            super(keyFunction);
        }

        @Override
        public Supplier<LinkedHashMultimap<K,T>> supplier() {
            return LinkedHashMultimap::create;

        }
    }

    private static abstract class MultimapCollector<K,T, A extends Multimap<K,T>, R extends Multimap<K,T>> implements Collector  {

        private Function<T, K> keyFunction;

        private MultimapCollector(Function<T, K> keyFunction) {
            Preconditions.checkNotNull(keyFunction,"The keyFunction can't be null");
            this.keyFunction = keyFunction;
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return (map, value) -> map.put(keyFunction.apply(value),value);
        }


        @Override
        public BinaryOperator<A> combiner() {
            return (c1, c2) -> {
                c1.putAll(c2);
                return c1;
            };
        }

        @Override
        @SuppressWarnings("unchecked")
        public Function<A, R> finisher() {
            return mmap -> (R) mmap;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Sets.newHashSet(Characteristics.CONCURRENT);
        }
    }
}
