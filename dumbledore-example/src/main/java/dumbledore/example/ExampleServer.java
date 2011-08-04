package dumbledore.example;

import com.google.common.collect.ImmutableList;
import dumbledore.annotations.Attribute;
import dumbledore.annotations.Metric;
import dumbledore.jmx.JmxListener;
import dumbledore.metrics.DataType;
import dumbledore.metrics.MetricType;
import dumbledore.metrics.MetricsRepository;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
@Metric(description = "An example server")
public class ExampleServer {

    private static final Logger logger = Logger.getLogger(ExampleServer.class);

    private final int port;
    private final MetricsRepository repository;
    private final AtomicBoolean started;

    public ExampleServer(int port) {
        this.port = port;
        this.repository = new MetricsRepository(ImmutableList.of(new LoggingListener(),
                                                                 new JmxListener()));
        this.started = new AtomicBoolean(false);
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
        repository.addMetric(this);
        // TODO: add a sample http servlet
    }

    public void stop() {
        logger.info("Stopping server");
        if(!(isStarted() && started.compareAndSet(true, false))) {
            logger.info("The service is already stopped, ignoring duplicate attempt.");
            return;
        }
        repository.removeMetric(this);
    }
}