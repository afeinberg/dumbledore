package dumbledore.collections;


import dumbledore.utils.Supplier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Feinberg
 */
public class Tables {

    public static class HashMapSupplier<K, V>  implements Supplier<HashMap<K, V>> {
        @Override
        public HashMap<K, V> get() {
            return new HashMap<K, V>();
        }
    }

    public static <R, C, V> Table<R, C, V> createHashBasedTable() {
        return new BaseTable<R, C, V>(new HashMap<R, Map<C, V>>(),
                                      new HashMapSupplier<C, V>());
    }
}
