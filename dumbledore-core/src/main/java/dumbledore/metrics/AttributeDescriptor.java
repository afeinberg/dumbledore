package dumbledore.metrics;

import dumbledore.utils.Utils;

import java.lang.reflect.Method;

/**
 * A POJO descriptor for the Attribute annotation.
 *
 */
public class AttributeDescriptor {

    private final String name;
    private final String description;
    private final DataType dataType;
    private final MetricType metricType;
    private final Method valueMethod;

    /**
     * Create an attribute descriptor
     * @param name Name of the attribute
     * @param description Description of the Attribute, meant for humans
     * @param dataType Data type the Attribute returns
     * @param metricType Kind of metric that the attribute keeps
     * @param valueMethod Method to be invoked to get the value of the attribute
     */
    public AttributeDescriptor(String name,
                               String description,
                               DataType dataType,
                               MetricType metricType,
                               Method valueMethod) {
        this.name = Utils.notNull(name);
        this.description = Utils.notNull(description);
        this.dataType = Utils.notNull(dataType);
        this.metricType = Utils.notNull(metricType);
        this.valueMethod = Utils.notNull(valueMethod);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public DataType getDataType() {
        return dataType;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public Method getValueMethod() {
        return valueMethod;
    }

    @Override
    public String toString() {
        return "AttributeDescriptor{" +
               "name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", dataType=" + dataType +
               ", metricType=" + metricType +
               '}';
    }
}
