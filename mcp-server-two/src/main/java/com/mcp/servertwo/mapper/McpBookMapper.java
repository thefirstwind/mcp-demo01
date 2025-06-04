package com.mcp.servertwo.mapper;

import com.mcp.servertwo.model.McpBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface McpBookMapper {
    @Select("SELECT * FROM mcp_books WHERE author = #{author}")
    List<McpBook> findBooksByAuthor(String author);

    @Select("SELECT * FROM mcp_books")
    List<McpBook> listAllBooks();

    @Select("SELECT * FROM mcp_books WHERE id = #{id}")
    McpBook getBookDetails(Long id);
} 