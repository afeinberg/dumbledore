package dumbledore.collections;

import java.util.Collection;
import java.util.Map;

/**
 * Inspired by guava's Table collection, but built to avoid
 * an external dependency on guava (which may bring about binary
 * incompatibilities with applications still using google collections/base)
 *
 * @param <R> Type of the table row keys
 * @param <C> Type of the table column key
 * @param <V> Type of the mapped values
 */
public interface Table<R, C, V> {

    public boolean contains(R rowKey, C columnKey);

    public boolean containsRow(R rowKey);

    public boolean isEmpty();

    public int size();

    public void clear();

    public V put(R rowKey, C columnKey, V value);

    public V putAll(Table<? extends R, ? extends C, ? extends V> table);

    public V remove(R rowKey, C columnKey);

    public Map<C, V> row(R rowKey);

    public Map<R, V> column(C columnKey);

    public Collection<V> values();

    public Collection<R> rowKeys();

    public Collection<C> columnKeys(R rowKey, C columnKey);

    public Map<R, Map<C, V>> rowMap();
}
