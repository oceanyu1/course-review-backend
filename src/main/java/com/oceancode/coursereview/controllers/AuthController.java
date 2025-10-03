package com.oceancode.coursereview.controllers;

import com.oceancode.coursereview.domain.dtos.JwtResponse;
import com.oceancode.coursereview.domain.dtos.LoginRequest;
import com.oceancode.coursereview.domain.dtos.RegisterRequest;
import com.oceancode.coursereview.domain.entities.Role;
import com.oceancode.coursereview.domain.entities.User;
import com.oceancode.coursereview.security.JwtUtil;
import com.oceancode.coursereview.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        // Check if user already exists
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .program(request.getProgram())
                .year(request.getYear())
                .role(Role.USER)
                .build();

        userService.save(user);

        // Generate JWT token
        String jwt = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(new JwtResponse(user.getId(), jwt, user.getEmail(), user.getName(),
                user.getProgram(), user.getYear()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body("Error: Invalid email or password!");
        }

        // Get user details
        User user = userService.findByEmail(request.getEmail());

        // Generate JWT token
        String jwt = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(new JwtResponse(user.getId(), jwt, user.getEmail(), user.getName(),
                user.getProgram(), user.getYear()));
    }
}
