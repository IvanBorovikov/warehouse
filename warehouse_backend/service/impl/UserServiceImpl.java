package com.warehouse.warehouse_backend.service.impl;

import com.warehouse.warehouse_backend.dto.token.JwtAuthenticationDto;
import com.warehouse.warehouse_backend.dto.token.RefreshTokenDto;
import com.warehouse.warehouse_backend.dto.token.UserCredentialsDto;
import com.warehouse.warehouse_backend.dto.UserDTO;
import com.warehouse.warehouse_backend.model.User;
import com.warehouse.warehouse_backend.repository.UserRepository;
import com.warehouse.warehouse_backend.config.JwtService;
import com.warehouse.warehouse_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public User registerUser(User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Пользователь с таким " + user.getEmail()
            + " существует");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findById(int id){
        return userRepository.findById(id);
    }

    public UserDTO updateUser(int id, UserDTO userDTO){
        User user = findById(id).get();

        user.setFull_name(userDTO.getFull_name());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);

        return userDTO;
    }

    public void deleteUser(int id){
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()){
            throw new IllegalArgumentException("Пользователь не найден");
        }

        userRepository.deleteById(id);
    }

    @Override
    public JwtAuthenticationDto signIn(UserCredentialsDto dto) throws AuthenticationException {
        User user = findByCredentials(dto);
        return jwtService.generateAuthToken(user.getEmail());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto dto) throws Exception {
        String refreshToken = dto.getRefreshToken();
        if (refreshToken != null && jwtService.validateJWTToken(refreshToken)){
            User user = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(user.getEmail(), refreshToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    private User findByCredentials(UserCredentialsDto dto) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());

        if (optionalUser.isPresent()){
            User user = optionalUser.get();

            boolean matches = passwordEncoder.matches(dto.getPassword(), user.getPassword()); // Для логов
            System.out.println("Password matches: " + matches);
            if (passwordEncoder.matches(dto.getPassword(), user.getPassword())){
                return user;
            }
        }

        throw new AuthenticationException("Email or password is not correct");
    }

    private User findByEmail(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(() -> new Exception(String.format("User with email %s not found", email)));
    }
}
