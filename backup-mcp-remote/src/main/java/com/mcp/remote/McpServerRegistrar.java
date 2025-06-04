package com.mcp.remote;

import java.net.UnknownHostException;
import java.util.Map;

public interface McpServerRegistrar {
    /**
     * 获取本地主机地址
     * 
     * @return 本地主机IP地址
     * @throws UnknownHostException 如果无法获取主机地址
     */
    String getLocalHostAddress() throws UnknownHostException;

    /**
     * 创建默认的元数据
     * 
     * @return 默认元数据映射
     */
    Map<String, Object> createDefaultMetadata();

    /**
     * 验证服务器注册参数
     * 
     * @param serverName 服务器名称
     * @param host 主机地址
     * @param port 端口号
     * @param supportedTools 支持的工具列表
     * @throws IllegalArgumentException 如果参数无效
     */
    void validateRegistrationParameters(String serverName, String host, int port, String[] supportedTools);

    /**
     * 注册服务器
     * 
     * @param serverName 服务器名称
     * @param host 主机地址
     * @param port 端口号
     * @param supportedTools 支持的工具列表
     * @param metadata 服务器元数据
     */
    void registerServer(String serverName, String host, int port, 
                        String[] supportedTools, Map<String, Object> metadata);

    /**
     * 取消注册服务器
     */
    void unregisterServer();
} 