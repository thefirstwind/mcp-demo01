package com.mcp.sdk.processor;

import com.mcp.sdk.annotation.McpServerTool;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class McpServerToolProcessor {
    /**
     * Discover and process all methods annotated with McpServerTool
     * 
     * @param services List of services to process
     * @return Map of discovered tools with their details
     */
    public Map<String, List<McpServerToolInfo>> discoverServerTools(List<Object> services) {
        Map<String, List<McpServerToolInfo>> discoveredTools = new HashMap<>();

        for (Object service : services) {
            List<McpServerToolInfo> serviceTools = new ArrayList<>();
            
            // Find all methods with McpServerTool annotation
            for (Method method : service.getClass().getDeclaredMethods()) {
                McpServerTool toolAnnotation = AnnotationUtils.findAnnotation(method, McpServerTool.class);
                
                if (toolAnnotation != null) {
                    McpServerToolInfo toolInfo = new McpServerToolInfo(
                        toolAnnotation.name(),
                        toolAnnotation.description(),
                        toolAnnotation.enabled(),
                        toolAnnotation.priority(),
                        service.getClass().getSimpleName(),
                        method
                    );
                    
                    serviceTools.add(toolInfo);
                }
            }
            
            if (!serviceTools.isEmpty()) {
                discoveredTools.put(service.getClass().getSimpleName(), serviceTools);
            }
        }

        return discoveredTools;
    }

    /**
     * Represents information about a discovered MCP server tool
     */
    public static class McpServerToolInfo {
        private final String name;
        private final String description;
        private final boolean enabled;
        private final int priority;
        private final String serviceName;
        private final Method method;

        public McpServerToolInfo(String name, String description, boolean enabled, 
                                 int priority, String serviceName, Method method) {
            this.name = name;
            this.description = description;
            this.enabled = enabled;
            this.priority = priority;
            this.serviceName = serviceName;
            this.method = method;
        }

        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public boolean isEnabled() { return enabled; }
        public int getPriority() { return priority; }
        public String getServiceName() { return serviceName; }
        public Method getMethod() { return method; }

        @Override
        public String toString() {
            return "McpServerToolInfo{" +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", enabled=" + enabled +
                    ", priority=" + priority +
                    ", serviceName='" + serviceName + '\'' +
                    '}';
        }
    }
} 