package dumbledore.metrics;


import dumbledore.annotations.Attribute;

import java.lang.reflect.Method;

/**
 * @author Alex Feinberg
 */
public class AttributeWrapper {

    private final String name;
    private final String description;
    private final DataType dataType;
    private final MetricType metricType;
    private final Object metricObject;
    private final Method method;

    public AttributeWrapper(String name,
                            String description,
                            DataType dataType,
                            MetricType metricType,
                            Object metricObject,
                            Method method) {
        this.name = name;
        this.description = description;
        this.dataType = dataType;
        this.metricType = metricType;
        this.metricObject = metricObject;
        this.method = method;
    }

    public static boolean isAttribute(Method meth) {
        return meth.isAnnotationPresent(Attribute.class);
    }

    public static AttributeWrapper fromMethod(Object obj, Method meth) {
        if(!isAttribute(meth))
            throw new IllegalArgumentException("Method "
                                               + meth
                                               + " not annotated as an Attribute!");
        Attribute attr = meth.getAnnotation(Attribute.class);
        return new AttributeWrapper(attr.name(),
                                    attr.description(),
                                    attr.dataType(),
                                    attr.metricType(),
                                    obj,
                                    meth);
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

    public Object getMetricObject() {
        return metricObject;
    }

    public Method getMethod() {
        return method;
    }

    public Object getValue() {
        try {
            return getMethod().invoke(getMetricObject());
        } catch(Exception e) {
            throw new RuntimeException("Error getting the attribute's value ", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttributeWrapper that = (AttributeWrapper) o;

        if (dataType != that.dataType) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        if (metricObject != null ? !metricObject.equals(that.metricObject) : that.metricObject != null)
            return false;
        if (metricType != that.metricType) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 31 * result + (metricType != null ? metricType.hashCode() : 0);
        result = 31 * result + (metricObject != null ? metricObject.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }
}
