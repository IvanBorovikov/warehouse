package com.warehouse.warehouse_backend.service.impl;

import com.warehouse.warehouse_backend.dto.token.JwtAuthenticationDto;
import com.warehouse.warehouse_backend.dto.token.RefreshTokenDto;
import com.warehouse.warehouse_backend.dto.token.UserCredentialsDto;
import com.warehouse.warehouse_backend.dto.user.UserDTO;
import com.warehouse.warehouse_backend.dto.user.UserUpdateDTO;
import com.warehouse.warehouse_backend.exception.NotFoundException;
import com.warehouse.warehouse_backend.model.User;
import com.warehouse.warehouse_backend.repository.UserRepository;
import com.warehouse.warehouse_backend.config.JwtService;
import com.warehouse.warehouse_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(UserDTO userDTO){

        if (userRepository.existsByEmail(userDTO.getEmail())){
            throw new IllegalArgumentException("Пользователь с таким " + userDTO.getEmail()
            + " существует");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setRole(userDTO.getRole());
        userRepository.save(user);

        return new UserDTO(user.getUsername(), user.getPassword(),
                user.getEmail(), user.getFullName(), user.getRole(), user.getCreatedAt());
    }

    @Transactional
    public Page<UserUpdateDTO> findAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(user ->
                new UserUpdateDTO(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(),
                        user.getFullName(), user.getRole(), user.getCreatedAt()));
    }

    @Transactional
    public Optional<UserUpdateDTO> findById(Long id){
        return userRepository.findById(id).map(user ->
                new UserUpdateDTO(user.getId(),user.getUsername(), user.getPassword(), user.getEmail(),
                        user.getFullName(), user.getRole(), user.getCreatedAt()));
    }

    @Transactional
    public UserUpdateDTO updateUser(Long id, UserUpdateDTO userDTO){
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователя с таким id не найден"));

        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);

        return userDTO;
    }

    @Transactional
    public void deleteUser(Long id){
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
            User user = findByEmailForToken(jwtService.getEmailFromToken(refreshToken));
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

    private User findByEmailForToken(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(() -> new Exception(String.format("User with email %s not found", email)));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(""));
    }


    @Override
    public boolean findEmailOfRefreshPassword(String email) {

        return userRepository.existsByEmail(email);
    }
}
