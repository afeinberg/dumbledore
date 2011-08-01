package dumbledore.annotations;

import dumbledore.metrics.DataType;
import dumbledore.metrics.MetricType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Alex Feinberg
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Attribute {

    public String name();

    public String description();

    public DataType dataType();

    public MetricType metricType() default MetricType.GAUGE;
}
