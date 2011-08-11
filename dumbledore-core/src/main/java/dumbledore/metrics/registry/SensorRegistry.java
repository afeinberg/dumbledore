package dumbledore.metrics.registry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dumbledore.utils.collections.ColumnarHashMap;
import dumbledore.utils.collections.ColumnarMap;
import dumbledore.metrics.DefaultSensorDescriptorFactory;
import dumbledore.metrics.SensorDescriptor;
import dumbledore.metrics.SensorDescriptorFactory;
import dumbledore.utils.Utils;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A registry of sensors. Sensors are indexed by <b>domain</b> and
 * <b>type</b>. Each sensor is identified by unique (domain, type).
 * Multiple domains can have the same type. One can think of this as
 * a table with a compound primary key:
 * </p>
 * <pre>
 *     CREATE TABLE registry(
 *       DOMAIN string,
 *       TYPE string,
 *       SENSOR sensor,
 *       PRIMARY KEY (DOMAIN, TYPE)
 *     )
 * </pre>
 * </p>
 * Each registry instance contains a SensorDescriptorFactory, which
 * takes care of processing an sensor object into a SensorDescriptor
 * (extracting the attributes along the way). The default
 * SensorDescriptorFactory processes the sensor's annotations.
 * </p>
 * When a metric is added or removed from a repository, each
 * {@link SensorListener} associated with the repository is invoked.
 * For a given registry instance, the set of listeners is set at creation time;
 * behaviour is undefined if additional listeners are added to a collection
 * of listeners passed to a SensorRegistry. The registry is thread-safe, but not
 * concurrent: object-wide locks guard the registry's internal state from either
 * access or modification.
 */
@ThreadSafe
public class SensorRegistry {

    @GuardedBy("this")
    private final ColumnarMap<String, String, SensorDescriptor> sensors;
    private final Collection<? extends SensorListener> listeners;
    private final SensorDescriptorFactory factory;

    /**
     * Uses {@link DefaultSensorDescriptorFactory} which processes
     * sensors based on annotations.
     * @see {@link SensorRegistry#SensorRegistry(SensorDescriptorFactory, Collection)}
     *
     * @param listeners An iterable collection of listeners that will
     *                  be individually executed upon registration/un-registration
     *                  of a sensor
     */
    public SensorRegistry(Collection<? extends SensorListener> listeners) {
        this(new DefaultSensorDescriptorFactory(), listeners);
    }

    /**
     * Create a SensorRegistry given a {@link SensorDescriptorFactory} to process
     * the sensors (prior to registration) and a list of listeners that will be
     * fired on registration (after a sensor is processed) and un-registration
     *
     * @param factory A SensorDescriptorFactory instance for this registry
     * @param listeners An iterable collection of SensorListener instances
     */
    public SensorRegistry(SensorDescriptorFactory factory,
                          Collection<? extends SensorListener> listeners) {
        this.factory = Utils.notNull(factory);
        this.listeners = Utils.notNull(listeners);
        sensors = ColumnarHashMap.create();
    }

    /**
     * Register a sensor, using the package name of the object's class as a domain,
     * and the name of the object's class as a type.
     * </p>
     * E.g., If given an instance of "voldemort.store.BdbStorageEngine" the domain would
     * be "voldemort.store" and the type would be "BdbStorageEngine". This is useful
     * cases where there is only one instance of a given class registered as a sensor.
     *
     * @see {@link SensorRegistry#registerSensor(String, String, Object)}
     * @param obj Instantiated sensor object
     */
    public void registerSensor(Object obj) {
        Utils.notNull(obj);
        Class<?> cls = obj.getClass();
        registerSensor(Utils.getPackageName(cls), Utils.getClassName(cls), obj);
    }

    /**
     * Register a sensor, using the package name of the object's class as a domain,
     * and a supplied string as the object's type.
     * </p>
     * E.g., if given an instance of "voldemort.store.StatTrackingStore" but with
     * "messages" as the type, the domain name would be "voldemort.store" and the
     * type would be "messages". This is useful when there are multiple instances of
     * a class registered as sensors, e.g., separate stores powered by the same
     * storage engine in a data store.
     *
     * @see {@link SensorRegistry#registerSensor(String, String, Object)}
     * @param type A meaningful name for the type
     * @param obj A fully instantiated sensor object
     */
    public void registerSensor(String type, Object obj) {
        Utils.notNull(type);
        Utils.notNull(obj);
        Class<?> cls = obj.getClass();
        registerSensor(Utils.getPackageName(cls), type, obj);
    }

    /**
     * Register a sensor, explicitly specifying the domain and the types. Each domain
     * is unique, but a domain may have multiple distinct types in it. In other words,
     * you can think of (DOMAIN, TYPE) as being a compound primary key for the sensor
     * registry.
     * </p>
     * Upon invocation, the sensor is first processed with the registry's
     * SensorDescriptorFactory into a SensorDescriptor. Once the sensor is processed,
     * the sensor is placed into the registry. If a sensor already exists for the
     * given type and domain, the previously sensor is unregistered first. Once
     * a sensor is registered, the listeners are individually invoked on the
     * processed sensor.
     *
     * @param domain A unique domain
     * @param type A type (unique within a domain)
     * @param obj Fully instantiated sensor object
     */
    public void registerSensor(String domain, String type, Object obj) {
        Utils.notNull(domain);
        Utils.notNull(type);
        Utils.notNull(obj);

        SensorDescriptor sensor = factory.get(obj);
        synchronized(this) {
            if(sensors.contains(domain, type))
                unregisterSensor(domain, type);
            sensors.put(domain, type, sensor);
        }

        for(SensorListener listener: listeners) {
            listener.registered(domain, type, sensor);
        }
    }

    /**
     * Assuming a sensor was registered using the package name as a domain and
     * class name as a type, unregister the object.
     *
     * @see {@link SensorRegistry#registerSensor(Object)}
     * @see {@link SensorRegistry#unregisterSensor(String, String)}
     * @param obj The instantiated sensor object
     */
    public void unregisterSensor(Object obj) {
        Utils.notNull(obj);

        Class<?> cls = obj.getClass();
        unregisterSensor(Utils.getPackageName(cls), Utils.getClassName(cls));
    }

    /**
     * Assuming a sensor was registered using the package name as a domain and
     * a supplied type, unregister the object.
     *
     * @see {@link SensorRegistry#registerSensor(String, Object)}
     * @see {@link SensorRegistry#registerSensor(String, String, Object)}
     * @param type Sensor's type
     * @param obj The instantiated sensor object
     */
    public void unregisterSensor(String type, Object obj) {
        Utils.notNull(type);
        Utils.notNull(obj);

        Class<?> cls = obj.getClass();
        unregisterSensor(Utils.getPackageName(cls), type);
    }

    /**
     * Unregister a sensor given a type and domain.
     * </p>
     * Given a type and domain, the sensor is first removed from the registry.
     * Once a sensor has been removed, the unregistration listeners are invoked
     * on the domain and the type.
     *
     * @see {@link SensorRegistry#registerSensor(String, String, Object)}
     * @param domain Sensor's domain
     * @param type Sensor's type
     * @throws IllegalArgumentException if there is no sensor for the given domain
     *                                  and type
     */
    public void unregisterSensor(String domain, String type)  {
        Utils.notNull(domain);
        Utils.notNull(type);

        synchronized(this) {
            if(!sensors.contains(domain, type))
                throw new IllegalArgumentException("No sensor for domain "
                                                   + domain
                                                   + ", type "
                                                   + type
                                                   + " exists!");
            sensors.remove(domain, type);
        }

        for(SensorListener listener: listeners) {
            listener.unregistered(domain, type);
        }
    }

    /**
     * Retrieve a sensor's descriptor given domain and type
     *
     * @param domain The sensor's domain
     * @param type The sensor's type
     * @return The SensorDescriptor instance for the sensor
     */
    public synchronized SensorDescriptor getSensor(String domain, String type) {
        return sensors.get(domain, type);
    }

    /**
     * Get all sensors for a domain. This is equivalent to
     * "SELECT TYPE,SENSOR FROM registry WHERE domain=$domain"
     *
     * @param domain The domain of the sensors
     * @return An immutable map of type to sensor descriptor for the given domain
     */
    public synchronized Map<String, SensorDescriptor> getSensors(String domain) {
        return ImmutableMap.copyOf(sensors.row(domain));
    }

    /**
     * Return a map of domain to a type -> sensor descriptor. This just dumps an
     * immutable copy of the registry's internal state.
     * @return Immutable copy of the registry's internal state
     */
    public synchronized Map<String, Map<String, SensorDescriptor>> getAllSensors() {
        return ImmutableMap.copyOf(sensors.rowMap());
    }

    /**
     * Return a list of all the sensor domains. This is equivalent to
     * "SELECT DISTINCT(DOMAIN) FROM registry"
     * @return An immutable set of all sensor domains
     */
    public synchronized Set<String> getSensorDomains() {
        return ImmutableSet.copyOf(sensors.rowKeySet());
    }

    /**
     * Return a list of all types for a given sensor domain. This is equivalent to
     * "SELECT TYPE FROM registry WHERE DOMAIN=$domain"
     *
     * @param domain The domain of sensors
     * @return An immutable set of all the sensor types for a domain
     */
    public synchronized Set<String> getSensorTypes(String domain) {
        return ImmutableSet.copyOf(sensors.row(domain).keySet());
    }
}
