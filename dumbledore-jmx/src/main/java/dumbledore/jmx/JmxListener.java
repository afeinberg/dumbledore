package dumbledore.jmx;

import dumbledore.metrics.MetricWrapper;
import dumbledore.metrics.MetricsListener;

import javax.management.ObjectName;

/**
 *
 */
public class JmxListener implements MetricsListener {

    public void added(String domain, String type, MetricWrapper metric) {
        ObjectName name = JmxUtils.createObjectName(domain, type);
        JmxUtils.registerMbean(metric.getObject(), name);
    }


    public void removed(String domain, String type) {
        ObjectName name = JmxUtils.createObjectName(domain, type);
        JmxUtils.unregisterMbean(name);
    }
}
