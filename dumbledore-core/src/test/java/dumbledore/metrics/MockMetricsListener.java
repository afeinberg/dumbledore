package dumbledore.metrics;

public class MockMetricsListener implements MetricsListener {

    private volatile int metricsAdded;
    private volatile int metricsRemoved;

    public MockMetricsListener() {
        metricsAdded = 0;
        metricsRemoved = 0;
    }

    @Override
    public void added(String domain, String type, MetricWrapper metric) {
        metricsAdded++;
    }

    @Override
    public void removed(String domain, String type) {
        metricsRemoved++;
    }

    public int getMetricsAdded() {
        return metricsAdded;
    }

    public int getMetricsRemoved() {
        return metricsRemoved;
    }
}
