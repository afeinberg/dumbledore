package dumbledore.metrics;

import com.google.common.collect.Maps;
import dumbledore.annotations.Attribute;
import dumbledore.annotations.Sensor;
import dumbledore.utils.Utils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 *
 */
public class DefaultSensorDescriptorFactory implements SensorDescriptorFactory {

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
            AttributeDescriptor attr = extractAttribute(sensor, method);
            if(attr != null)
                attrs.put(attr.getName(), attr);
        }
        return Collections.unmodifiableMap(attrs);
    }

    public AttributeDescriptor extractAttribute(Object obj, Method method) {
        if(obj == null || method == null)
            return null;

        Attribute annotation = method.getAnnotation(Attribute.class);
        if(annotation == null)
            return null;

        return new AttributeDescriptor(annotation.name(),
                                       annotation.description(),
                                       annotation.dataType(),
                                       annotation.metricType(),
                                       method);
    }
}
