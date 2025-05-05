package com.engine.mediasearch.interfaces.rest;

import com.engine.mediasearch.interfaces.dto.UserLoginDTO;
import com.engine.mediasearch.interfaces.dto.UserRegistrationDTO;
import com.engine.mediasearch.interfaces.dto.UserResponseDTO;
import com.engine.mediasearch.security.JwtUtil;
import com.engine.mediasearch.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * (1.a) User Registration Endpoint
     * URL: POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO response = userService.registerUser(registrationDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * (1.a) User Authentication Endpoint
     * URL: POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO) {
        try {
            UserResponseDTO userResponse = userService.authenticateUser(loginDTO);
            // Generate JWT token based on the username
            String token = jwtUtil.generateToken(userResponse.getUsername());
            // Return a response including both user details and token
            return new ResponseEntity<>(new JwtResponse(userResponse, token), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    // Response class to encapsulate user details and JWT token
    @Setter
    @Getter
    public static class JwtResponse {
        private UserResponseDTO user;
        private String token;

        public JwtResponse(UserResponseDTO user, String token) {
            this.user = user;
            this.token = token;
        }

    }
}
