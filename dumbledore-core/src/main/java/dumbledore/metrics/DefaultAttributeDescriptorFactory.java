package dumbledore.metrics;

import dumbledore.annotations.Attribute;
import dumbledore.utils.Utils;

import java.lang.reflect.Method;

/**
 *
 */
public class DefaultAttributeDescriptorFactory implements AttributeDescriptorFactory {

    public AttributeDescriptor get(Object obj, Method method) {
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
