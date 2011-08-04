package dumbledore.example;


import dumbledore.metrics.MetricWrapper;
import dumbledore.metrics.MetricsListener;
import org.apache.log4j.Logger;

/**
 *
 */
public class LoggingListener implements MetricsListener {

    private static final Logger logger = Logger.getLogger(LoggingListener.class);

    public void added(String domain, String type, MetricWrapper metric) {
        logger.info("Registered a metric, domain = "
                    +  domain
                    + ", type = "
                    + type);
    }

    public void removed(String domain, String type) {
        logger.info("Unregistered a metric, domain = "
                    + domain
                    + ", type = "
                   + type);
    }
}
