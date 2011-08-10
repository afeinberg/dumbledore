package dumbledore.metrics;

public class MockSensorListener implements SensorListener {

    private volatile int metricsAdded;
    private volatile int metricsRemoved;

    public MockSensorListener() {
        metricsAdded = 0;
        metricsRemoved = 0;
    }

    public void registered(String domain, String type, SensorDescriptor sensor) {
        metricsAdded++;
    }

    public void unregistered(String domain, String type) {
        metricsRemoved++;
    }

    public int getMetricsAdded() {
        return metricsAdded;
    }

    public int getMetricsRemoved() {
        return metricsRemoved;
    }
}
