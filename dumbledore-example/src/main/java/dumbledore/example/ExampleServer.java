package dumbledore.example;

import com.google.common.collect.Lists;
import dumbledore.DumbledoreException;
import dumbledore.annotations.Attribute;
import dumbledore.annotations.Sensor;
import dumbledore.jmx.ReadWriteJmxListener;
import dumbledore.jmx.ReadOnlyJmxListener;
import dumbledore.metrics.DataType;
import dumbledore.metrics.MetricType;
import dumbledore.metrics.registry.SensorListener;
import dumbledore.metrics.registry.SensorRegistry;
import dumbledore.servlet.DumbledoreServlet;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
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
    private final Servlet exampleServlet;

    public ExampleServer(ExampleServerConfig config) {
        this.config = config;
        registry = createSensorRegistry();
        started = new AtomicBoolean(false);
        exampleServlet = new ExampleServlet();
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
        registry.registerSensor("dumbledore.example", "ExampleServer", this);
        registry.registerSensor(exampleServlet);
        startHttp();
    }

    public void startHttp() {
        try {
            Connector connector = new SelectChannelConnector();
            connector.setPort(config.getPort());
            Server httpServer = new Server();
            httpServer.setConnectors(new Connector[] { connector });
            Context context = new Context(httpServer, "/", Context.NO_SESSIONS);
            context.addServlet(new ServletHolder(new DumbledoreServlet(registry)), "/metrics");
            context.addServlet(new ServletHolder(exampleServlet), "/example");
            httpServer.start();
            logger.info("HTTP server started on " + config.getPort());
        } catch(Exception e) {
            throw new DumbledoreException(e);
        }
    }

    public void stop() {
        logger.info("Stopping server");
        if(!(isStarted() && started.compareAndSet(true, false))) {
            logger.info("The service is already stopped, ignoring duplicate attempt.");
            return;
        }
        registry.unregisterSensor(exampleServlet);
        registry.unregisterSensor("dumbledore.example", "ExampleServer");
    }

    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser();
        parser.accepts("help", "print help information");
        parser.accepts("port")
              .withRequiredArg()
              .describedAs("port")
              .ofType(Integer.class);
        parser.accepts("disable-jmx");
        parser.accepts("enable-jmx-rw");

        OptionSet options = parser.parse(args);
        if(options.has("help")) {
            parser.printHelpOn(System.out);
            System.exit(0);
        }

        ExampleServerConfig.Builder builder = ExampleServerConfig.newBuilder();
        if(options.has("disable-jmx")) {
            builder.setJmxEnabled(false);
        } else if(options.has("enable-jmx-rw")) {
            builder.setReadWriteJmxEnabled(true);
        }
        if(options.has("port")) {
            builder.setPort((Integer) options.valueOf("port"));
        }
        ExampleServerConfig config = builder.build();

        final ExampleServer server = new ExampleServer(config);
        if(!server.isStarted())
            server.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                if(server.isStarted())
                    server.stop();
            }
        });
    }

}
