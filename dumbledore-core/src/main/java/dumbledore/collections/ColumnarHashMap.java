package dumbledore.collections;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * Based on guava's HashBasedTable
 */
public class ColumnarHashMap<R, C, V> extends BaseColumnarMap<R, C, V> {

    private static class Factory<C, V> implements Supplier<Map<C, V>> {
        private final int expectedSize;

        Factory(int expectedSize) {
            this.expectedSize = expectedSize;
        }

        @Override
        public Map<C, V> get() {
            return Maps.newHashMapWithExpectedSize(expectedSize);
        }
    }

    public static <R, C, V> ColumnarHashMap<R, C, V> create() {
        return new ColumnarHashMap<R, C, V>(new HashMap<R, Map<C, V>>(),
                                            new Factory<C, V>(0));
    }

    public static <R, C, V> ColumnarHashMap<R, C, V> create(int expectedRows,
                                                            int expectedCellsPerRow) {
        Map<R, Map<C, V>> underlying = Maps.newHashMapWithExpectedSize(expectedRows);
        return new ColumnarHashMap<R, C, V>(underlying,
                                            new Factory<C, V>(expectedCellsPerRow));
    }

    public ColumnarHashMap(Map<R, Map<C, V>> underlying, Supplier<? extends Map<C, V>> factory) {
        super(underlying, factory);
    }
}
