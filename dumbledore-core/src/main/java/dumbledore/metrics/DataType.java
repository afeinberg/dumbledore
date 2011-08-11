package dumbledore.metrics;

/**
 * Describes kinds of data that may be held by an attribute. These are meant as
 * helpers for applications consuming the metrics, e.g., if we annotate an
 * attribute as returning Storage than "256000000" can be pretty-printed as
 * "256M".
 *
 */
public enum DataType {

    /**
     *
     */
    INTEGER,

    /**
     *
     */
    LONG,

    /**
     *
     */
    DOUBLE,

    /**
     * Percentage, expressed as a double, e.g., 0.34 for 34%
     */
    PERCENT,

    /**
     * Bytes of stored data, expressed as a long
     */
    STORAGE,

    /**
     * Time interval in milliseconds
     */
    DURATION,

    /**
     *
     */
    TIME,


    /**
     *
     */
    DATE,

    /**
     *
     */
    DATETTIME,

    /**
     *
     */
    STRING,

    /**
     *
     */
    BOOLEAN
}
