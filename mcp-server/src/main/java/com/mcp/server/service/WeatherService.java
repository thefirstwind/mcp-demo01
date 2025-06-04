package com.mcp.server.service;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    public String getCityWeather(String city) {
        // 模拟天气查询
        return city + "：晴朗，气温25°C，湿度40%";
    }
} 