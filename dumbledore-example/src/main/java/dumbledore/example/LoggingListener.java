package dumbledore.example;


import dumbledore.metrics.SensorDescriptor;
import dumbledore.metrics.registry.SensorListener;
import org.apache.log4j.Logger;

/**
 *
 */
public class LoggingListener implements SensorListener {

    private static final Logger logger = Logger.getLogger(LoggingListener.class);

    public void registered(String domain, String type, SensorDescriptor sensor) {
        logger.info("Registered a sensor, domain = "
                    +  domain
                    + ", type = "
                    + type);
        if(logger.isDebugEnabled())
            logger.debug(sensor.toString());
    }

    public void unregistered(String domain, String type) {
        logger.info("Unregistered a sensor, domain = "
                    + domain
                    + ", type = "
                   + type);
    }
}
