package com.example.schneider.dto;

public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    
    // Constructores
    public AuthResponse() {}
    
    public AuthResponse(String token, Long id, String nombre, String email) {
        this.token = token;
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }
    
    // Getters y Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
