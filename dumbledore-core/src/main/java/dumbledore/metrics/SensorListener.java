package dumbledore.metrics;

/**
 * Allows additional metrics systems, e.g., JMX to run their own
 * registration/un-registration methods whenever metrics are added to
 * or removed from {@link SensorRepository}
 *
 */
public interface SensorListener {

    /**
     * Executed after a sensor is added
     * @param domain Sensor's domain, e.g., "voldemort.store.stats"
     * @param type Sensor's type, e.g., "aggregate-statistics"
     * @param sensor A {@link SensorWrapper} instance wrapping the sensor object
     */
    public void added(String domain, String type, SensorWrapper sensor);

    /**
     * Executed after a metric is removed
     *
     * @see #added(String, String, SensorWrapper)
     */
    public void removed(String domain, String type);
}
