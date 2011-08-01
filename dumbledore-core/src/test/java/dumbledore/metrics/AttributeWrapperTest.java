package dumbledore.metrics;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @author Alex Feinberg
 */
public class AttributeWrapperTest {

    private final Example example;

    public AttributeWrapperTest() {
        example = new Example(123,
                              3.14);
    }

    @Test
    public void testFromMethod() throws Exception {
        Method meth = Example.class.getDeclaredMethod("getFoo");
        AttributeWrapper attr = AttributeWrapper.fromMethod(example, meth);
        Assert.assertEquals(attr.getName(), "foo");
        Assert.assertEquals(attr.getDescription(), "Foo");
        Assert.assertEquals(attr.getDataType(), DataType.LONG);
        Assert.assertEquals(attr.getMetricType(), MetricType.GAUGE);
        Assert.assertEquals(attr.getMethod(), meth);
        Assert.assertEquals(attr.getMetricObject(), example);
    }

    @Test
    public void testGetValue() throws Exception {
        Method fooMeth = Example.class.getDeclaredMethod("getFoo");
        AttributeWrapper fooAttr = AttributeWrapper.fromMethod(example, fooMeth);
        Method barMeth = Example.class.getDeclaredMethod("getBar");
        AttributeWrapper barAttr = AttributeWrapper.fromMethod(example, barMeth);
        Method bazMeth = Example.class.getDeclaredMethod("getBaz");
        AttributeWrapper bazAttr = AttributeWrapper.fromMethod(example, bazMeth);

        Assert.assertEquals(fooAttr.getValue(), 123l);
        Assert.assertEquals(barAttr.getValue(), 3.14d);

        example.setBaz(7);

        Assert.assertEquals(bazAttr.getValue(),  7);
    }
}
