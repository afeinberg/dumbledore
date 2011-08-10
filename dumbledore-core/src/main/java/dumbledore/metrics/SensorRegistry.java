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
 * A repository of sensors. Metrics are indexed by <b>domain</b> and
 * <b>type</b>. Each metric has a unique (domain, type) pair.
 * Domains are unique, each domain can several unique types.
 * </p>
 * When a metric is added or removed from a repository, each
 * {@link SensorListener} associated with the repository is invoked.
 */
@ThreadSafe
public class SensorRegistry {

    @GuardedBy("this")
    private final Table<String, String, SensorWrapper> sensors;
    private final Collection<? extends SensorListener> listeners;

    public SensorRegistry(Collection<? extends SensorListener> listeners) {
        Utils.notNull(listeners);

        sensors = Tables.createHashBasedTable();
        this.listeners = listeners;
    }

    public void register(Object obj) {
        Class<?> cls = obj.getClass();
        registerSensor(Utils.getPackageName(cls), Utils.getClassName(cls), obj);
    }

    public void registerSensor(String type, Object obj) {
        Class<?> cls = obj.getClass();
        registerSensor(Utils.getPackageName(cls), type, obj);
    }

    public void registerSensor(String domain, String type, Object obj) {
        SensorWrapper sensor = SensorWrapper.fromObject(obj);
        synchronized(this) {
            if(sensors.contains(domain, type))
                unregisterSensor(domain, type);
            sensors.put(domain, type, sensor);
        }

        for(SensorListener listener: listeners) {
            listener.registered(domain, type, sensor);
        }
    }

    public void unregisterSensor(Object obj) {
        Class<?> cls = obj.getClass();
        unregisterSensor(Utils.getPackageName(cls), Utils.getClassName(cls));
    }

    public void unregisterSensor(String type, Object obj) {
        Class<?> cls = obj.getClass();
        unregisterSensor(Utils.getPackageName(cls), type);
    }

    public void unregisterSensor(String domain, String type)  {
        synchronized(this) {
            if(!sensors.contains(type, domain))
                throw new IllegalArgumentException("No sensor for domain "
                                                   + domain
                                                   + ", type "
                                                   + "exists!");
            sensors.remove(type, domain);
        }

        for(SensorListener listener: listeners) {
            listener.unregistered(domain, type);
        }
    }

    public synchronized SensorWrapper getSensor(String domain, String type) {
        return sensors.get(type, domain);
    }

    public synchronized Map<String, SensorWrapper> getSensors(String domain) {
        return ImmutableMap.copyOf(sensors.row(domain));
    }

    public synchronized Map<String, Map<String, SensorWrapper>> getAllSensors() {
        return ImmutableMap.copyOf(sensors.rowMap());
    }

    public synchronized Set<String> getSensorDomains() {
        return ImmutableSet.copyOf(sensors.rowKeySet());
    }

    public synchronized Set<String> getSensorTypes(String domain) {
        return ImmutableSet.copyOf(sensors.row(domain).keySet());
    }
}
