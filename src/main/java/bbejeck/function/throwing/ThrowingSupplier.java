package bbejeck.function.throwing;

import java.util.function.Supplier;

/**
 * User: Bill Bejeck
 * Date: 1/4/16
 * Time: 9:27 PM
 */

@FunctionalInterface
public interface ThrowingSupplier<U> extends Supplier<U> {

    @Override
    default U get() {
        try {
            return getThrows();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    U getThrows() throws Exception;
}
