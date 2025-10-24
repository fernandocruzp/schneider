package com.example.schneider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.schneider.dto.AuthResponse;
import com.example.schneider.dto.LoginRequest;
import com.example.schneider.dto.RegisterRequest;
import com.example.schneider.entity.Usuario;
import com.example.schneider.repository.UsuarioRepository;
import com.example.schneider.security.JwtUtils;
import com.example.schneider.security.UserPrincipal;

@Service
public class AuthService {
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtUtils jwtUtils;
    
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        System.out.println(loginRequest);
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        
        return new AuthResponse(jwt, userDetails.getId(), userDetails.getNombre(), userDetails.getEmail());
    }
    
    public AuthResponse registerUser(RegisterRequest signUpRequest) {
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso!");
        }
        
        // Crear nueva cuenta de usuario
        Usuario usuario = new Usuario(signUpRequest.getNombre(),
                                    signUpRequest.getApellido(),
                                    signUpRequest.getEmail(), // Este será el email
                                    encoder.encode(signUpRequest.getPassword()));
        
        usuarioRepository.save(usuario);
        
        // Autenticar automáticamente después del registro
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        
        return new AuthResponse(jwt, userDetails.getId(), userDetails.getNombre(), userDetails.getEmail());
    }
}
