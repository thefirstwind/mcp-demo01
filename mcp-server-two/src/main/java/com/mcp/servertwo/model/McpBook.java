package com.mcp.servertwo.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class McpBook {
    private Long id;
    private String title;
    private String author;
    private LocalDate publishDate;
    private String isbn;
} 