package dumbledore.example;

import com.google.common.collect.Lists;
import dumbledore.annotations.Attribute;
import dumbledore.annotations.Sensor;
import dumbledore.jmx.ReadWriteJmxListener;
import dumbledore.jmx.ReadOnlyJmxListener;
import dumbledore.metrics.DataType;
import dumbledore.metrics.MetricType;
import dumbledore.metrics.registry.SensorListener;
import dumbledore.metrics.registry.SensorRegistry;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
@Sensor(description = "An example server")
public class ExampleServer {

    private static final Logger logger = Logger.getLogger(ExampleServer.class);

    private final ExampleServerConfig config;
    private final SensorRegistry registry;
    private final AtomicBoolean started;

    public ExampleServer(ExampleServerConfig config) {
        this.config = config;
        this.registry = createSensorRegistry();
        this.started = new AtomicBoolean(false);
    }

    private SensorRegistry createSensorRegistry() {
        List<SensorListener> listeners = Lists.newArrayList();
        listeners.add(new LoggingListener());
        if(config.isJmxEnabled()) {
            if(config.isReadWriteJmxEnabled()) {
                listeners.add(new ReadWriteJmxListener());
            } else {
                listeners.add(new ReadOnlyJmxListener());
            }
        }
        return new SensorRegistry(listeners);
    }

    @Attribute(name = "isStarted",
               description = "See if the server is started",
               dataType = DataType.BOOLEAN,
               metricType = MetricType.GAUGE)
    public boolean isStarted() {
        return started.get();
    }

    public void start() {
        boolean isntStarted = started.compareAndSet(false, true);
        if(!isntStarted)
            throw new IllegalStateException("Server is already started!");
        registry.register(this);
        // TODO: add a sample http servlet
    }

    public void stop() {
        logger.info("Stopping server");
        if(!(isStarted() && started.compareAndSet(true, false))) {
            logger.info("The service is already stopped, ignoring duplicate attempt.");
            return;
        }
        registry.unregisterSensor(this);
    }
}
