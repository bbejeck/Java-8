package bbejeck.function.throwing;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * User: Bill Bejeck
 * Date: 1/8/16
 * Time: 2:56 PM
 */

@FunctionalInterface
public interface ThrowingBiConsumer<T, U> extends BiConsumer<T, U> {

    @Override
    default void accept(T t, U u) {
        try {
            acceptThrows(t,u);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    default  ThrowingBiConsumer<T, U> andThen(ThrowingBiConsumer<? super T, ? super U> after){
        Objects.requireNonNull(after, "BiConsumer can't be null");
        return (l, r) -> {
            accept(l, r);
            after.accept(l, r);
        };

    }

    void acceptThrows(T t, U u) throws Exception;

}
