package com.example.schneider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/all")
    public String allAccess() {
        return "Contenido público.";
    }
    
    @GetMapping("/db")
    public String testDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            return "Database connection successful: " + connection.getMetaData().getDatabaseProductName();
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
    
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userAccess() {
        return "Contenido de usuario.";
    }
}
