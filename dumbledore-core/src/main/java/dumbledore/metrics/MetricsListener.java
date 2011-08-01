package dumbledore.metrics;

/**
 * @author Alex Feinberg
 */
public interface MetricsListener {

    public void added(String domain, String type, MetricWrapper metric);

    public void removed(String domain, String type);
}
