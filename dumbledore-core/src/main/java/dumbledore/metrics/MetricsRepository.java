package dumbledore.metrics;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.List;

/**
 * @author Alex Feinberg
 */
@ThreadSafe
public class MetricsRepository {

    @GuardedBy("this")
    private final Table<String, String, MetricWrapper> registry;
    private final List<MetricsListener> listeners;

    public MetricsRepository(List<MetricsListener> listeners) {
        Preconditions.checkNotNull(listeners);

        registry = HashBasedTable.create();
        this.listeners = listeners;
    }
}
