package dumbledore.metrics;

import com.google.common.collect.Maps;
import dumbledore.annotations.Metric;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * @author Alex Feinberg
 */
public class MetricWrapper {

    private final Object object;
    private final String description;
    private final Map<String, AttributeWrapper> attributes;

    public MetricWrapper(Object object,
                         String description,
                         Map<String, AttributeWrapper> attributes) {
        this.object = object;
        this.description = description;
        this.attributes = attributes;
    }

    public static MetricWrapper fromObject(Object object) {
        Map<String, AttributeWrapper> attributes = extractAttributes(object);
        String description = extractDescription(object);
        return new MetricWrapper(object, description, attributes);
    }

    private static String extractDescription(Object object) {
        return object.getClass().getAnnotation(Metric.class).description();
    }

    public static Map<String, AttributeWrapper> extractAttributes(Object obj) {
        Class<?> cls = obj.getClass();
        if(!cls.isAnnotationPresent(Metric.class))
            throw new IllegalArgumentException("Class " + cls + " not annotated as Metric!");
        Map<String, AttributeWrapper> retVal = Maps.newHashMap();
        for(Method meth: cls.getDeclaredMethods()) {
            if(AttributeWrapper.isAttribute(meth)) {
                AttributeWrapper attr = AttributeWrapper.fromMethod(obj, meth);
                retVal.put(attr.getName(), attr);
            }
        }
        return retVal;
    }

    public AttributeWrapper getAttribute(String name) {
        return attributes.get(name);
    }

    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public String getDescription() {
        return description;
    }

    public Object getObject() {
        return object;
    }

    public Collection<AttributeWrapper> getAttributes() {
        return attributes.values();
    }
}
