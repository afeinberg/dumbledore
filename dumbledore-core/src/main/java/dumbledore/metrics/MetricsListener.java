package dumbledore.metrics;

/**
 * Allows additional metrics systems, e.g., JMX to run their own
 * registration/un-registration methods whenever metrics are added to
 * or removed from {@link MetricsRepository}
 *
 */
public interface MetricsListener {

    /**
     * Executed after a metric is added
     * @param domain Metric's domain, e.g., "voldemort.store.stats"
     * @param type Metric's type, e.g., "aggregate-statistics"
     * @param metric A {@link MetricWrapper} instance wrapping the metric object
     */
    public void added(String domain, String type, MetricWrapper metric);

    /**
     * Executed after a metric is removed
     *
     * @see #added(String, String, MetricWrapper)
     */
    public void removed(String domain, String type);
}
