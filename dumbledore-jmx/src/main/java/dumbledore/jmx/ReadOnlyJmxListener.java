package dumbledore.jmx;

import com.google.common.collect.Lists;
import dumbledore.DumbledoreException;
import dumbledore.metrics.AttributeDescriptor;
import dumbledore.metrics.SensorDescriptor;
import dumbledore.metrics.registry.SensorListener;

import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 */
public class ReadOnlyJmxListener implements SensorListener {

    public static ObjectName createObjectName(String domain, String type) {
        try {
            return new ObjectName(domain + ":type=" + type);
        } catch(MalformedObjectNameException e) {
            throw new DumbledoreException(e);
        }
    }

    private ModelMBean createModelMBean(SensorDescriptor sensor) {
        try {
            ModelMBean mbean = new RequiredModelMBean();
            Object o = sensor.getSensor();
            String description = sensor.getDescription();
            ModelMBeanInfo info = new ModelMBeanInfoSupport(o.getClass().getName(),
                                                            description,
                                                            getAttributeInfo(sensor),
                                                            new ModelMBeanConstructorInfo[0],
                                                            getOperationInfo(sensor),
                                                            new ModelMBeanNotificationInfo[0]);
            mbean.setModelMBeanInfo(info);
            mbean.setManagedResource(o, "ObjectReference");

            return mbean;
        } catch(MBeanException e) {
            throw new DumbledoreException(e);
        } catch(InvalidTargetObjectTypeException e) {
            throw new DumbledoreException(e);
        } catch(InstanceNotFoundException e) {
            throw new DumbledoreException(e);
        }
    }

    private static ModelMBeanAttributeInfo[] getAttributeInfo(SensorDescriptor sensor) {
        ArrayList<ModelMBeanAttributeInfo> infos = Lists.newArrayList();
        for(AttributeDescriptor attribute: sensor.getAttributes()) {
            try {
                String name = attribute.getName();
                Method getter = attribute.getValueMethod();
                String description = attribute.getDescription();
                ModelMBeanAttributeInfo info = new ModelMBeanAttributeInfo(name,
                                                                           description,
                                                                           getter,
                                                                           null);
                Descriptor descriptor = info.getDescriptor();
                if(getter != null)
                    descriptor.setField("getMethod", getter.getName());
                info.setDescriptor(descriptor);
                infos.add(info);

            } catch(IntrospectionException e) {
                throw new DumbledoreException(e);
            }
        }

        return infos.toArray(new ModelMBeanAttributeInfo[infos.size()]);
    }

    private static ModelMBeanOperationInfo[] getOperationInfo(SensorDescriptor sensor) {
        ArrayList<ModelMBeanOperationInfo> infos = Lists.newArrayList();
        for(AttributeDescriptor attribute: sensor.getAttributes()) {
            Method method = attribute.getValueMethod();
            String description = attribute.getDescription();
            int visibility = 4;
            int impact = MBeanOperationInfo.INFO;
            ModelMBeanOperationInfo info = new ModelMBeanOperationInfo(method.getName(),
                                                                       description,
                                                                       new MBeanParameterInfo[0],
                                                                       method.getReturnType()
                                                                             .getName(),
                                                                       impact);
            info.getDescriptor().setField("visibility", Integer.toString(visibility));
            infos.add(info);
        }

        return infos.toArray(new ModelMBeanOperationInfo[infos.size()]);
    }

    public void registered(String domain, String type, SensorDescriptor sensor) {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = createObjectName(domain, type);
        JmxUtils.registerMbean(server, createModelMBean(sensor), name);
    }

    public void unregistered(String domain, String type) {
        ObjectName name = JmxUtils.createObjectName(domain, type);
        JmxUtils.unregisterMbean(name);
    }
}
