package com.warehouse.warehouse_backend.controller;

import com.warehouse.warehouse_backend.dto.UserDTO;
import com.warehouse.warehouse_backend.model.User;
import com.warehouse.warehouse_backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userServiceImpl;

    private final PasswordEncoder passwordEncoder;



    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO register(@RequestBody @Valid UserDTO userDTO){

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());
        user.setFull_name(userDTO.getFull_name());
        user.setCreated_at(LocalDateTime.now());

        User savedUser = userServiceImpl.registerUser(user);

        UserDTO response = new UserDTO();
        response.setUsername(savedUser.getUsername());
        response.setRole(savedUser.getRole());
        response.setEmail(savedUser.getEmail());
        response.setFull_name(savedUser.getFull_name());
        response.setCreated_at(savedUser.getCreated_at());


        return response;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> allUsers = userServiceImpl.findAllUsers();

        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<User>> findById(@PathVariable("id") int id){

        Optional<User> user = userServiceImpl.findById(id);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id")int id, @RequestBody UserDTO userDTO){
        UserDTO update =  userServiceImpl.updateUser(id, userDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") int id){
        userServiceImpl.deleteUser(id);

        return ResponseEntity.ok("Пользователь с id {" + id + "} удален");

    }
}
