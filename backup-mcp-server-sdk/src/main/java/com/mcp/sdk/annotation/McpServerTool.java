package com.mcp.sdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods as MCP server tools
 * These methods can be discovered and registered as available tools for a server
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface McpServerTool {
    /**
     * Name of the tool
     * @return Tool name
     */
    String name();

    /**
     * Description of the tool's functionality
     * @return Tool description
     */
    String description() default "";

    /**
     * Indicates if the tool is currently available
     * @return Whether the tool is available
     */
    boolean enabled() default true;

    /**
     * Priority of the tool (for potential load balancing or selection)
     * @return Tool priority
     */
    int priority() default 0;
} 