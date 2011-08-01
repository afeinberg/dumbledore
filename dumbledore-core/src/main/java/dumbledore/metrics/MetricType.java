package dumbledore.metrics;

/**
 * Types of metrics that can be exposed. Designed to accommodate the
 * DSTs (Data Source Types) supported by RRD, with the addition of
 * a <em>TIMESTAMP</em> DST.
 *
 * @see <a href="http://oss.oetiker.ch/rrdtool/tut/rrd-beginners.en.html">RRDtool rrd-beginners</a>
 * @author Alex Feinberg
 */
public enum MetricType {
    /**
     * Saves the rate of change of the value over a step period.
     * Assumes the value is always increasing, e.g., traffic counters on
     * a router.
     */
    COUNTER,

    /**
     * Same as {@link #COUNTER}, but allows negative values as
     * well, e.g., to see rate of <em>change</em> of free disk space.
     */
    DERIVE,

    /**
     * Saves the rate of change, but assumes that the previous value is
     * set to 0. The difference between the current and the previous value
     * is always equal to the current value. Will just store the current value
     * divided by the step interval.
     */
    ABSOLUTE,

    /**
     * Saves the actual value, not the rate of change.
     */
    GAUGE
}
