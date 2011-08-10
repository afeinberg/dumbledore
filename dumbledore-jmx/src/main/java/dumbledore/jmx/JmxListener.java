package dumbledore.jmx;

import dumbledore.metrics.SensorDescriptor;
import dumbledore.metrics.SensorListener;

import javax.management.ObjectName;

/**
 *
 */
public class JmxListener implements SensorListener {

    public void registered(String domain, String type, SensorDescriptor sensor) {
        ObjectName name = JmxUtils.createObjectName(domain, type);
        JmxUtils.registerMbean(sensor.getSensor(), name);
    }


    public void unregistered(String domain, String type) {
        ObjectName name = JmxUtils.createObjectName(domain, type);
        JmxUtils.unregisterMbean(name);
    }
}
