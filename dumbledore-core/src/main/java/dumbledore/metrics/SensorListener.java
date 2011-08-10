package dumbledore.metrics;

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
     * @param sensor A {@link SensorWrapper} instance wrapping the sensor object
     */
    public void registered(String domain, String type, SensorWrapper sensor);

    /**
     * Executed after a metric is removed
     *
     * @see #registered(String, String, SensorWrapper)
     */
    public void unregistered(String domain, String type);
}
