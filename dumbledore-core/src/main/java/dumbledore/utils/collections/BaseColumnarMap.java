package dumbledore.utils.collections;

import dumbledore.utils.Utils;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class BaseColumnarMap<R, C, V> implements ColumnarMap<R, C, V> {

    private final Map<R, Map<C, V>> underlying;
    private final Supplier<? extends Map<C, V>> factory;

    public BaseColumnarMap(Map<R, Map<C, V>> underlying,
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
        return underlying.isEmpty();
    }
    public int size() {
        int size = 0;
        for(Map<C, V> map: underlying.values())
            size += map.size();
        return size;
    }

    public void clear() {
        underlying.clear();
    }

    public V get(R rowKey, C columnKey) {
        if(rowKey == null || columnKey == null)
            return null;
        Map<C, V> map = underlying.get(rowKey);
        if(map == null)
            return null;
        return map.get(columnKey);
    }

    private Map<C, V> getOrCreate(R rowKey) {
        Map<C, V> map = underlying.get(rowKey);
        if(map == null) {
            map = factory.get();
            underlying.put(rowKey, map);
        }
        return map;
    }

    public V put(R rowKey, C columnKey, V value) {
        Utils.notNull(rowKey);
        Utils.notNull(columnKey);
        Utils.notNull(value);
        return getOrCreate(rowKey).put(columnKey, value);
    }

    public V remove(R rowKey, C columnKey) {
        if((rowKey == null) || (columnKey == null))
            return null;
        Map<C, V> map = underlying.get(rowKey);
        if(map == null)
            return null;
        V value = map.remove(columnKey);
        if(map.isEmpty())
            underlying.remove(rowKey);
        return value;
    }

    @Override
    public Map<C, V> row(R rowKey) {
        return underlying.get(rowKey);
    }

    @Override
    public Collection<R> rowKeySet() {
        return underlying.keySet();
    }

    @Override
    public Map<R, Map<C, V>> rowMap() {
        return underlying;
    }
}
