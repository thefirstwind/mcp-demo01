package com.mcp.remote;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultMcpServerRegistrar implements McpServerRegistrar {
    private final RestTemplate restTemplate;

    @Value("${mcp.router.url:http://localhost:8025}")
    private String routerUrl;

    public DefaultMcpServerRegistrar(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getLocalHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public Map<String, Object> createDefaultMetadata() {
        return new HashMap<>();
    }

    @Override
    public void validateRegistrationParameters(String serverName, String host, int port, String[] supportedTools) {
        if (serverName == null || serverName.isEmpty()) {
            throw new IllegalArgumentException("Server name cannot be empty");
        }
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Host cannot be empty");
        }
        if (port <= 0) {
            throw new IllegalArgumentException("Port must be a positive integer");
        }
    }

    @Override
    public void registerServer(String serverName, String host, int port, 
                               String[] supportedTools, Map<String, Object> metadata) {
        validateRegistrationParameters(serverName, host, port, supportedTools);
        
        String registerUrl = routerUrl + "/servers/register";
        
        // Prepare registration payload
        RegistrationRequest request = RegistrationRequest.builder()
            .serverName(serverName)
            .host(host)
            .port(port)
            .supportedTools(Arrays.asList(supportedTools))
            .metadata(metadata)
            .build();

        // Send registration request to router
        restTemplate.postForObject(registerUrl, request, Void.class);
    }

    @Override
    public void unregisterServer() {
        // Implement server unregistration logic
        String unregisterUrl = routerUrl + "/servers/unregister";
        restTemplate.postForObject(unregisterUrl, null, Void.class);
    }
} 