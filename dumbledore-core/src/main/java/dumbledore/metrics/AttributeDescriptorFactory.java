package dumbledore.metrics;

import java.lang.reflect.Method;

/**
 *
 */
public interface AttributeDescriptorFactory {

    public AttributeDescriptor get(Object obj, Method method);
}
