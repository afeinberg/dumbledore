package dumbledore.collections;

import java.util.Collection;
import java.util.Map;

/**
 * Inspired by guava's Table, but built to avoid an external dependency on
 * guava (which may bring about binary incompatibilities with applications
 * still using google-collections
 *
 * @param <R> Type of the table row keys
 * @param <C> Type of the table column key
 * @param <V> Type of the mapped values
 */
public interface ColumnarMap<R, C, V> {

    public boolean contains(R rowKey, C columnKey);

    public boolean containsRow(R rowKey);

    public boolean isEmpty();

    public int size();

    public void clear();

    public V get(R rowKey, C columnKey);

    public V put(R rowKey, C columnKey, V value);

    public V remove(R rowKey, C columnKey);

    public Map<C, V> row(R rowKey);

    public Collection<R> rowKeySet();

    public Map<R, Map<C, V>> rowMap();
}
