package dumbledore.metrics;

import dumbledore.utils.Utils;

import java.lang.reflect.Method;

/**
 *
 */
public class AttributeDescriptor {

    private final String name;
    private final String description;
    private final DataType dataType;
    private final MetricType metricType;
    private final Method valueMethod;

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
