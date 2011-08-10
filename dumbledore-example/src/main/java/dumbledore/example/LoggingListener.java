package dumbledore.example;


import dumbledore.metrics.SensorWrapper;
import dumbledore.metrics.SensorListener;
import org.apache.log4j.Logger;

/**
 *
 */
public class LoggingListener implements SensorListener {

    private static final Logger logger = Logger.getLogger(LoggingListener.class);

    public void registered(String domain, String type, SensorWrapper sensor) {
        logger.info("Registered a sensor, domain = "
                    +  domain
                    + ", type = "
                    + type);
    }

    public void unregistered(String domain, String type) {
        logger.info("Unregistered a metric, domain = "
                    + domain
                    + ", type = "
                   + type);
    }
}
