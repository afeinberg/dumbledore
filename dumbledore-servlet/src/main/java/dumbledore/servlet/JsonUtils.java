package dumbledore.servlet;

import dumbledore.metrics.AttributeDescriptor;
import dumbledore.metrics.SensorDescriptor;

/**
 *
 */
public class JsonUtils {

    public static void sensorToJson(SensorDescriptor sensor, StringBuilder sb)  {
        sb.append("{\n");
        sb.append("\"name\": \"");
        sb.append(sensor.getName());
        sb.append("\", ");
        sb.append("\n\"description\": \"");
        sb.append(sensor.getDescription());
        sb.append("\", ");
        sb.append("\n\"attributes\": {\n");

        int i = 0;
        for(AttributeDescriptor attribute: sensor.getAttributes()) {
            if(i++ > 0)
                sb.append(",\n");
            attributeToJson(sensor, attribute, sb);
        }
        sb.append("\n}");
    }

    public static void attributeToJson(SensorDescriptor sensor,
                                       AttributeDescriptor attribute,
                                       StringBuilder sb) {
        String name = attribute.getName();
        sb.append("  \"");
        sb.append(name);
        sb.append("\": {");
        sb.append("\n    \"description\": \"");
        sb.append(attribute.getDescription());
        sb.append("\", ");
        sb.append("\n    \"data_type\": \"");
        sb.append(attribute.getDataType());
        sb.append("\", ");
        sb.append("\n    \"metric_type\": \"");
        sb.append(attribute.getMetricType());
        sb.append("\", ");
        sb.append("\n    \"value\": \"");
        sb.append(sensor.getAttributeValue(name));
        sb.append("\"\n  }");
    }
}
