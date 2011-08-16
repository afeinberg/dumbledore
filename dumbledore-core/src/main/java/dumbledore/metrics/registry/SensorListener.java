package dumbledore.metrics.registry;

import dumbledore.metrics.SensorDescriptor;

/**
 * Allows additional metrics systems, e.g., JMX to run their own
 * registration/un-registration methods whenever metrics are added to
 * or removed from {@link SensorRegistry}
 *
 */
public interface SensorListener {

    /**
     * Executed after a sensor is added
     * @param domain Sensor's domain, e.g., "voldemort.store.stats"
     * @param type Sensor's type, e.g., "aggregate-statistics"
     * @param sensor A {@link SensorDescriptor} instance wrapping the sensor object
     */
    public void registered(String domain, String type, SensorDescriptor sensor);

    /**
     * Executed after a metric is removed
     */
    public void unregistered(String domain, String type);
}
