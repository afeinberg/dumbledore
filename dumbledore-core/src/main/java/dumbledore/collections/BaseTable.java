package dumbledore.collections;

import dumbledore.utils.Supplier;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class BaseTable<R, C, V> implements Table<R, C, V> {

    private final Map<R, Map<C, V>> underlying;
    private final Supplier<? extends Map<C, V>> factory;

    public BaseTable(Map<R, Map<C, V>> underlying,
                     Supplier<? extends Map<C, V>> factory) {
        this.underlying = underlying;
        this.factory = factory;
    }

    public boolean contains(R rowKey, C columnKey) {
        if((rowKey == null) || (columnKey == null))
            return false;
        Map<C, V> map = underlying.get(rowKey);
        return map != null && map.containsKey(columnKey);
    }

    public boolean containsRow(R rowKey) {
        return rowKey != null && underlying.containsKey(rowKey);
    }

    public boolean isEmpty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int size() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void clear() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public V put(R rowKey, C columnKey, V value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public V putAll(Table<? extends R, ? extends C, ? extends V> table) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public V remove(R rowKey, C columnKey) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<C, V> row(R rowKey) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<R, V> column(C columnKey) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<V> values() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<R> rowKeys() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<C> columnKeys(R rowKey, C columnKey) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map<R, Map<C, V>> rowMap() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
