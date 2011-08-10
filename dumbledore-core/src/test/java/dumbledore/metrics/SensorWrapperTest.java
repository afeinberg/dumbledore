package dumbledore.metrics;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Method;


/**
 *
 */
public class SensorWrapperTest {

    private final Example example;

    public SensorWrapperTest() {
        this.example = new Example(123,
                                   3.14);
    }

    @Test
    public void testFromObject() throws Exception {
        SensorWrapper wrapper = SensorWrapper.fromObject(example);
        Method fooMeth = Example.class.getDeclaredMethod("getFoo");
        AttributeWrapper fooAttr = AttributeWrapper.fromMethod(example, fooMeth);

        Assert.assertEquals(wrapper.getDescription(), "Example class");
        Assert.assertEquals(wrapper.getAttribute("foo"), fooAttr);
    }
}
