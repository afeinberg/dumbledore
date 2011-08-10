package dumbledore.metrics;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dumbledore.collections.Table;
import dumbledore.collections.Tables;
import dumbledore.utils.Utils;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A repository of metrics. Metrics are indexed by <b>domain</b> and
 * <b>type</b>. Each metric has a unique (domain, type) pair.
 * Domains are unique, each domain can several unique types.
 * </p>
 * When a metric is added or removed from a repository, each
 * {@link SensorListener} associated with the repository is invoked.
 */
@ThreadSafe
public class SensorRepository {

    @GuardedBy("this")
    private final Table<String, String, SensorWrapper> metrics;
    private final Collection<? extends SensorListener> listeners;

    public SensorRepository(Collection<? extends SensorListener> listeners) {
        Utils.notNull(listeners);

        metrics = Tables.createHashBasedTable();
        this.listeners = listeners;
    }

    public void addMetric(Object obj) {
        Class<?> cls = obj.getClass();
        addMetric(Utils.getPackageName(cls), Utils.getClassName(cls), obj);
    }

    public void addMetric(String type, Object obj) {
        Class<?> cls = obj.getClass();
        addMetric(Utils.getPackageName(cls), type, obj);
    }

    public void addMetric(String domain, String type, Object obj) {
        SensorWrapper sensor = SensorWrapper.fromObject(obj);
        synchronized(this) {
            if(metrics.contains(domain, type))
                removeMetric(domain, type);
            metrics.put(domain, type, sensor);
        }

        for(SensorListener listener: listeners) {
            listener.added(domain, type, sensor);
        }
    }

    public void removeMetric(Object obj) {
        Class<?> cls = obj.getClass();
        removeMetric(Utils.getPackageName(cls), Utils.getClassName(cls));
    }

    public void removeMetric(String type, Object obj) {
        Class<?> cls = obj.getClass();
        removeMetric(Utils.getPackageName(cls), type);
    }

    public void removeMetric(String domain, String type)  {
        synchronized(this) {
            if(!metrics.contains(type, domain))
                throw new IllegalArgumentException("No sensor for domain "
                                                   + domain
                                                   + ", type "
                                                   + "exists!");
            metrics.remove(type, domain);
        }

        for(SensorListener listener: listeners) {
            listener.removed(domain, type);
        }
    }

    public synchronized SensorWrapper getMetric(String domain, String type) {
        return metrics.get(type, domain);
    }

    public synchronized Map<String, SensorWrapper> getMetrics(String domain) {
        return ImmutableMap.copyOf(metrics.row(domain));
    }

    public synchronized Map<String, Map<String, SensorWrapper>> getAllMetrics() {
        return ImmutableMap.copyOf(metrics.rowMap());
    }

    public synchronized Set<String> getMetricsDomains() {
        return ImmutableSet.copyOf(metrics.rowKeySet());
    }

    public synchronized Set<String> getMetricsTypes(String domain) {
        return ImmutableSet.copyOf(metrics.row(domain).keySet());
    }
}
