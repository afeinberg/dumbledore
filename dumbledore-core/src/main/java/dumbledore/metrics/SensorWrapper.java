package dumbledore.metrics;

import com.google.common.collect.Maps;
import dumbledore.annotations.Sensor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class SensorWrapper {

    private final Object object;
    private final String description;
    private final Map<String, AttributeWrapper> attributes;

    public SensorWrapper(Object object,
                         String description,
                         Map<String, AttributeWrapper> attributes) {
        this.object = object;
        this.description = description;
        this.attributes = attributes;
    }

    public static SensorWrapper fromObject(Object object) {
        Map<String, AttributeWrapper> attributes = extractAttributes(object);
        String description = extractDescription(object);
        return new SensorWrapper(object, description, attributes);
    }

    private static String extractDescription(Object object) {
        return object.getClass().getAnnotation(Sensor.class).description();
    }

    public static Map<String, AttributeWrapper> extractAttributes(Object obj) {
        Class<?> cls = obj.getClass();
        if(!cls.isAnnotationPresent(Sensor.class))
            throw new IllegalArgumentException("Class " + cls + " not annotated as Sensor!");
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
