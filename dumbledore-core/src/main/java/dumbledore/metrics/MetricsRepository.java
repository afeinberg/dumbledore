package dumbledore.metrics;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
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
 * {@link MetricsListener} associated with the repository is invoked.
 */
@ThreadSafe
public class MetricsRepository {

    @GuardedBy("this")
    private final Table<String, String, MetricWrapper> metrics;
    private final Collection<? extends MetricsListener> listeners;

    public MetricsRepository(Collection<? extends MetricsListener> listeners) {
        Preconditions.checkNotNull(listeners);

        metrics = HashBasedTable.create();
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
        MetricWrapper metric = MetricWrapper.fromObject(obj);
        synchronized(this) {
            if(metrics.contains(domain, type))
                removeMetric(domain, type);
            metrics.put(domain, type, metric);
        }

        for(MetricsListener listener: listeners) {
            listener.added(domain, type, metric);
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

        for(MetricsListener listener: listeners) {
            listener.removed(domain, type);
        }
    }

    public synchronized MetricWrapper getMetric(String domain, String type) {
        return metrics.get(type, domain);
    }

    public synchronized Map<String, MetricWrapper> getMetrics(String domain) {
        return ImmutableMap.copyOf(metrics.row(domain));
    }

    public synchronized Map<String, Map<String, MetricWrapper>> getAllMetrics() {
        return ImmutableMap.copyOf(metrics.rowMap());
    }

    public synchronized Set<String> getMetricsDomains() {
        return ImmutableSet.copyOf(metrics.rowKeySet());
    }

    public synchronized Set<String> getMetricsTypes(String domain) {
        return ImmutableSet.copyOf(metrics.row(domain).keySet());
    }
}
