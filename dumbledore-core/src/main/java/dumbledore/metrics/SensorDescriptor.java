package dumbledore.metrics;

import dumbledore.DumbledoreException;
import dumbledore.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

/**
 * A POJO descriptor for the Sensor annotation
 */
public class SensorDescriptor {

    private final Object sensor;
    private final String name;
    private final String description;
    private final Map<String, AttributeDescriptor> attributes;

    /**
     * Create a SensorDescriptor
     * @param sensor The underlying object for the sense
     * @param name A short name for the sensor
     * @param description The description of the sensor's functionality
     * @param attributes A map of attribute name to Attribute Descriptor
     */
    public SensorDescriptor(Object sensor,
                            String name,
                            String description,
                            Map<String, AttributeDescriptor> attributes) {
        this.sensor = Utils.notNull(sensor);
        this.name = Utils.notNull(name);
        this.description = Utils.notNull(description);
        this.attributes = Utils.notNull(attributes);
    }

    /**
     * Return the object backing the sensor
     * @return Object backing the sensor
     */
    public Object getSensor() {
        return sensor;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get an attribute with a specific name for the sensor
     * @param attrName Name of the attribute
     * @return The Attribute, or null if none was found
     */
    public AttributeDescriptor getAttribute(String attrName) {
        return attributes.get(attrName);
    }

    /**
     * Return all attributes in a sensor
     * @return Descriptors of all sensors in the attribute
     */
    public Collection<AttributeDescriptor> getAttributes() {
        return attributes.values();
    }

    /**
     * Return the names of all sensors
     * @return A collection of Strings, each being a sensor name
     */
    public Collection<String> getAttributeNames() {
        return attributes.keySet();
    }

    /**
     * Given an attribute name, invoke the method backing the attribute
     * on the objected associated with the sensor and return the method's
     * return value.
     * @param attrName Name of the attribute
     * @throws IllegalArgumentException if the attribute name is invalid
     * @throws DumbledoreException if there is an error invoking the method
     * @return Value returned by the attribute's method
     */
    public Object getAttributeValue(String attrName) {
        Utils.notNull(attrName);
        AttributeDescriptor descriptor = attributes.get(attrName);
        if(descriptor == null)
            throw new IllegalArgumentException("Attribute " + attrName + " is invalid");
        try {
            return descriptor.getValueMethod().invoke(sensor);
        } catch(IllegalAccessException iae) {
            throw new DumbledoreException("Illegal access exception invoking " +
                                          attrName,
                                          iae);
        } catch(InvocationTargetException ite) {
            throw new DumbledoreException("InvocationTargetException invoking " +
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
