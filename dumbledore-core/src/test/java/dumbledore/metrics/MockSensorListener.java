package dumbledore.metrics;

public class MockSensorListener implements SensorListener {

    private volatile int metricsAdded;
    private volatile int metricsRemoved;

    public MockSensorListener() {
        metricsAdded = 0;
        metricsRemoved = 0;
    }

    @Override
    public void added(String domain, String type, SensorWrapper sensor) {
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
