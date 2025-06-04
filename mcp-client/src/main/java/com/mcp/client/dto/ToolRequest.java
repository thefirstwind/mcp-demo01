package com.mcp.client.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ToolRequest {
    private String toolName;
    private Map<String, Object> parameters;
} 