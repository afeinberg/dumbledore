package dumbledore.metrics;

import com.google.common.collect.Maps;
import dumbledore.annotations.Sensor;
import dumbledore.utils.Utils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 *
 */
public class DefaultSensorDescriptorFactory implements SensorDescriptorFactory {

    private final AttributeDescriptorFactory factory;

    public DefaultSensorDescriptorFactory(AttributeDescriptorFactory factory) {
        this.factory = Utils.notNull(factory);
    }

    public SensorDescriptor get(Object sensor) {
        Utils.notNull(sensor);
        Class<?> cls = sensor.getClass();
        Sensor annotation = cls.getAnnotation(Sensor.class);
        Utils.notNull(annotation, "Sensor annotation must be present!");
        String name = "".equals(annotation.name()) ? Utils.getClassName(cls)
                                                   : annotation.name();
        String description = annotation.description();
        Map<String, AttributeDescriptor> attributes = getAttributes(sensor);
        return new SensorDescriptor(sensor,
                                    name,
                                    description,
                                    attributes);
    }

    private Map<String, AttributeDescriptor> getAttributes(Object sensor) {
        Class<?> cls = sensor.getClass();
        Method[] methods = cls.getDeclaredMethods();
        Map<String, AttributeDescriptor> attrs = Maps.newHashMapWithExpectedSize(methods.length);
        for(Method method: methods) {
            AttributeDescriptor attr = factory.get(sensor, method);
            if(attr != null)
                attrs.put(attr.getName(), attr);
        }
        return Collections.unmodifiableMap(attrs);
    }
}
