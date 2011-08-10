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

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public boolean isJmxEnabled() {
            return jmxEnabled;
        }

        public Builder setJmxEnabled(boolean jmxEnabled) {
            this.jmxEnabled = jmxEnabled;
            return this;
        }

        public boolean isReadWriteJmxEnabled() {
            return isReadWriteJmxEnabled;
        }

        public Builder setReadWriteJmxEnabled(boolean readWriteJmxEnabled) {
            this.isReadWriteJmxEnabled = readWriteJmxEnabled;
            return this;
        }

        public ExampleServerConfig build() {
            return new ExampleServerConfig(this);
        }
    }

    public ExampleServerConfig(Builder builder) {
        this.port = builder.getPort();
        this.jmxEnabled = builder.isJmxEnabled();
        this.readWriteJmxEnabled = builder.isReadWriteJmxEnabled();
    }

    public static Builder newBuilder() {
        return new Builder();
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
