package dumbledore.jmx;

import dumbledore.metrics.SensorWrapper;
import dumbledore.metrics.SensorListener;

import javax.management.ObjectName;

/**
 *
 */
public class JmxListener implements SensorListener {

    public void registered(String domain, String type, SensorWrapper sensor) {
        ObjectName name = JmxUtils.createObjectName(domain, type);
        JmxUtils.registerMbean(sensor.getObject(), name);
    }


    public void unregistered(String domain, String type) {
        ObjectName name = JmxUtils.createObjectName(domain, type);
        JmxUtils.unregisterMbean(name);
    }
}
