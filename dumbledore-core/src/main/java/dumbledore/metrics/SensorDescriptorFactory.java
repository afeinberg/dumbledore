package dumbledore.metrics;

/**
 * Process Java objects into sensors. Used by the SensorRegistry.
 * Kept as an interface in order to allow multiple implementations
 */
public interface SensorDescriptorFactory {

    /**
     * Process a sensor, extract all attributes (creating a descriptor for
     * for each attribute), constructor a Sensor Descriptor.
     * @param sensor A plain object that will be processed into sensor
     * @return A fully constructed SensorDescriptor
     */
    public SensorDescriptor get(Object sensor);
}
