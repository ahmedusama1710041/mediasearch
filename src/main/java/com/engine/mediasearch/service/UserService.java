package com.engine.mediasearch.service;

import com.engine.mediasearch.interfaces.dto.UserLoginDTO;
import com.engine.mediasearch.interfaces.dto.UserRegistrationDTO;
import com.engine.mediasearch.interfaces.dto.UserResponseDTO;
import com.engine.mediasearch.model.entity.User;
import com.engine.mediasearch.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Registers a new user.
     * Validates uniqueness of username and email, hashes the password, and saves to MySQL.
     */
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    /**
     * Authenticates a user based on username and password.
     */
    public UserResponseDTO authenticateUser(UserLoginDTO loginDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(loginDTO.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
            }
        }
        throw new RuntimeException("Invalid credentials");
    }
}
