package dumbledore.metrics;

import dumbledore.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class SensorDescriptor {
    private final Object sensor;
    private final String name;
    private final String description;
    private final Map<String, AttributeDescriptor> attributes;

    public SensorDescriptor(Object sensor,
                            String name,
                            String description,
                            Map<String, AttributeDescriptor> attributes) {
        this.sensor = Utils.notNull(sensor);
        this.name = Utils.notNull(name);
        this.description = Utils.notNull(description);
        this.attributes = Utils.notNull(attributes);
    }

    public Object getSensor() {
        return sensor;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public AttributeDescriptor getAttribute(String attrName) {
        return attributes.get(attrName);
    }

    public Collection<AttributeDescriptor> getAttributes() {
        return attributes.values();
    }

    public Collection<String> getAttributeNames() {
        return attributes.keySet();
    }

    public Object getAttributeValue(String attrName) {
        Utils.notNull(attrName);
        AttributeDescriptor descriptor = attributes.get(attrName);
        if(descriptor == null)
            throw new IllegalArgumentException("Attribute " + attrName + " is invalid");
        try {
            return descriptor.getValueMethod().invoke(sensor);
        } catch(IllegalAccessException iae) {
            throw new IllegalStateException("Illegal access exception invoking " +
                                            attrName,
                                            iae);
        } catch(InvocationTargetException ite) {
            throw new IllegalStateException("InvocationTargetException invoking " +
                                            attrName,
                                            ite);
        }
    }

    @Override
    public String toString() {
        return "SensorDescriptor{" +
               "sensor=" + sensor +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", attributes=" + attributes +
               '}';
    }
}
