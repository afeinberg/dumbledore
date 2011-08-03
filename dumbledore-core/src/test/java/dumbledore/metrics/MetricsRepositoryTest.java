package dumbledore.metrics;

import dumbledore.utils.Utils;
import junit.framework.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.List;

public class MetricsRepositoryTest {

    private List<MockMetricsListener> listeners;
    private MetricsRepository repository;

    public MetricsRepositoryTest() {
        //
    }

    @BeforeMethod
    public void setUp() {
        listeners = Lists.newArrayList();
        listeners.add(new MockMetricsListener());
        listeners.add(new MockMetricsListener());
        repository = new MetricsRepository(listeners);
    }

    @Test
    public void testListeners() {
        repository.addMetric(new Example(123, 3.14));
        Assert.assertEquals(listeners.get(0).getMetricsAdded(), 1);
        Assert.assertEquals(listeners.get(0).getMetricsRemoved(), 0);

        repository.removeMetric(Utils.getPackageName(Example.class),
                                Utils.getClassName(Example.class));
        Assert.assertEquals(listeners.get(0).getMetricsRemoved(), 1);
        Assert.assertEquals(listeners.get(0).getMetricsAdded(), 1);
    }

    @Test
    public void testAddAndGet() {
        Example example = new Example(123, 3.14);
        repository.addMetric(example);
        MetricWrapper metric = repository.getMetric(Utils.getPackageName(Example.class),
                                                    Utils.getClassName(Example.class));
        Assert.assertEquals(metric.getObject(), example);
        Assert.assertTrue(repository.getMetricsDomains()
                                    .contains(Utils.getPackageName(Example.class)));
        Assert.assertTrue(repository.getMetricsTypes(Utils.getPackageName(Example.class))
                                    .contains(Utils.getClassName(Example.class)));
        Assert.assertEquals(repository.getAllMetrics().get(Utils.getPackageName(Example.class))
                                      .get(Utils.getClassName(Example.class)),
                            metric);
    }

}
