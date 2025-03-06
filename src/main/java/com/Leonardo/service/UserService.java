package com.Leonardo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Leonardo.dtos.UserDto;
import com.Leonardo.model.Role;
import com.Leonardo.model.User;
import com.Leonardo.repository.RoleRepository;
import com.Leonardo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserDto> getAllUsersDto() {
        return userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .username(user.getUsername())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .country(user.getCountry())
                        .role(user.getRole().getName().replace("ROLE_", "")) // Remove the prefix for display
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public User updateUser(User user, UserDto updatedUserDto) {
        user.setFirstname(updatedUserDto.getFirstname());
        user.setLastname(updatedUserDto.getLastname());
        user.setCountry(updatedUserDto.getCountry());

        // Aquí busca el rol en la base de datos
        Role role = roleRepository.findByName("ROLE_" + updatedUserDto.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.setRole(role);

        // Aquí codifica la contraseña si se proporciona
        if (updatedUserDto.getPassword() != null && !updatedUserDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }

        return userRepository.save(user);
    }

    public String extractUsernameFromToken(String token) {
        return jwtService.extractUsername(token.replace("Bearer ", ""));
    }

    public Map<String, Object> extractAllInfoFromToken(String token) {
        return jwtService.extractAllInfo(token.replace("Bearer ", ""));
    }

    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userRepository.delete(user);
    }
}
