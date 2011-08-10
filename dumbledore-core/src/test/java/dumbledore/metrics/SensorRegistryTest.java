package dumbledore.metrics;

import dumbledore.utils.Utils;
import junit.framework.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.List;

public class SensorRegistryTest {

    private List<MockSensorListener> listeners;
    private SensorRegistry registry;

    public SensorRegistryTest() {
        //
    }

    @BeforeMethod
    public void setUp() {
        listeners = Lists.newArrayList();
        listeners.add(new MockSensorListener());
        listeners.add(new MockSensorListener());
        registry = new SensorRegistry(listeners);
    }

    @Test
    public void testListeners() {
        registry.register(new Example(123, 3.14));
        Assert.assertEquals(listeners.get(0).getMetricsAdded(), 1);
        Assert.assertEquals(listeners.get(0).getMetricsRemoved(), 0);

        registry.unregisterSensor(Utils.getPackageName(Example.class),
                                  Utils.getClassName(Example.class));
        Assert.assertEquals(listeners.get(0).getMetricsRemoved(), 1);
        Assert.assertEquals(listeners.get(0).getMetricsAdded(), 1);
    }

    @Test
    public void testAddAndGet() {
        Example example = new Example(123, 3.14);
        registry.register(example);
        SensorWrapper sensor = registry.getSensor(Utils.getPackageName(Example.class),
                                                  Utils.getClassName(Example.class));
        Assert.assertEquals(sensor.getObject(), example);
        Assert.assertTrue(registry.getSensorDomains()
                                    .contains(Utils.getPackageName(Example.class)));
        Assert.assertTrue(registry.getSensorTypes(Utils.getPackageName(Example.class))
                                    .contains(Utils.getClassName(Example.class)));
        Assert.assertEquals(registry.getAllSensors().get(Utils.getPackageName(Example.class))
                                      .get(Utils.getClassName(Example.class)),
                            sensor);
    }

}
