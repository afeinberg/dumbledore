package dumbledore.metrics;

import dumbledore.annotations.Attribute;
import dumbledore.annotations.Sensor;

/**
*
*/
@Sensor(description = "Example class")
public class Example {

    private final long foo;
    private final double bar;
    private volatile int baz;

    public Example(long foo, double bar) {
        this.foo = foo;
        this.bar = bar;
    }

    @Attribute(name = "foo",
               description = "Foo",
               dataType = DataType.LONG)
    public long getFoo() {
        return foo;
    }

    @Attribute(name = "bar",
               description = "Bar",
               dataType = DataType.DOUBLE)
    public double getBar() {
        return bar;
    }

    @Attribute(name = "baz",
               description = "Baz",
               dataType = DataType.INTEGER)
    public int getBaz() {
        return baz;
    }

    public void setBaz(int baz) {
        this.baz = baz;
    }
}
