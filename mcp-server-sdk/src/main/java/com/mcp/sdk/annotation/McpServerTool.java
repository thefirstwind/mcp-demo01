package com.mcp.sdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface McpServerTool {
    /**
     * 工具名称
     */
    String name();

    /**
     * 工具描述
     */
    String description() default "";

    /**
     * 工具优先级
     */
    int priority() default 0;

    /**
     * 是否启用
     */
    boolean enabled() default true;
} 