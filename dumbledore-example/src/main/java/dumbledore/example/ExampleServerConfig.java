package dumbledore.example;

/**
 *
 */
public class ExampleServerConfig {

    private final int port;
    private final boolean jmxEnabled;
    private final boolean readWriteJmxEnabled;

    public static class Builder {
        private int port = 7777;
        private boolean jmxEnabled = true;
        private boolean isReadWriteJmxEnabled = false;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public boolean isJmxEnabled() {
            return jmxEnabled;
        }

        public void setJmxEnabled(boolean jmxEnabled) {
            this.jmxEnabled = jmxEnabled;
        }

        public boolean isReadWriteJmxEnabled() {
            return isReadWriteJmxEnabled;
        }

        public void setReadWriteJmxEnabled(boolean readWriteJmxEnabled) {
            this.isReadWriteJmxEnabled = readWriteJmxEnabled;
        }
    }

    public ExampleServerConfig(Builder builder) {
        this.port = builder.getPort();
        this.jmxEnabled = builder.isJmxEnabled();
        this.readWriteJmxEnabled = builder.isReadWriteJmxEnabled();
    }

    public int getPort() {
        return port;
    }

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    public boolean isReadWriteJmxEnabled() {
        return readWriteJmxEnabled;
    }
}
