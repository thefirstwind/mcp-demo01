package com.mcp.remote;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RegistrationRequest implements Serializable {
    private String serverName;
    private String host;
    private int port;
    private List<String> supportedTools;
    private Map<String, Object> metadata;

    public RegistrationRequest() {}

    public RegistrationRequest(String serverName, String host, int port, 
                               List<String> supportedTools, 
                               Map<String, Object> metadata) {
        this.serverName = serverName;
        this.host = host;
        this.port = port;
        this.supportedTools = supportedTools;
        this.metadata = metadata;
    }

    // Getters and setters
    public String getServerName() { return serverName; }
    public void setServerName(String serverName) { this.serverName = serverName; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public List<String> getSupportedTools() { return supportedTools; }
    public void setSupportedTools(List<String> supportedTools) { this.supportedTools = supportedTools; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    /**
     * Create a builder for RegistrationRequest
     * 
     * @return Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for RegistrationRequest
     */
    public static class Builder {
        private String serverName;
        private String host;
        private int port;
        private List<String> supportedTools;
        private Map<String, Object> metadata = new java.util.HashMap<>();

        public Builder serverName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder supportedTools(List<String> supportedTools) {
            this.supportedTools = supportedTools;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata.putAll(metadata);
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        public RegistrationRequest build() {
            return new RegistrationRequest(serverName, host, port, supportedTools, metadata);
        }
    }
} 