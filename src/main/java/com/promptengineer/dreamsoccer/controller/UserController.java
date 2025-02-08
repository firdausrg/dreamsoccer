package com.promptengineer.dreamsoccer.controller;
/*
IntelliJ IDEA 2024.3.2.2 (Community Edition)
Build #IC-243.23654.189, built on January 29, 2025
@Author TGS a.k.a. Dwitio Ahmad Pranoto
Java Developer
Created on 05/02/2025 7:32
@Last Modified 05/02/2025 7:32
Version 1.0
*/

import com.promptengineer.dreamsoccer.model.Role;
import com.promptengineer.dreamsoccer.model.Status;
import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.repository.UserRepository;
import com.promptengineer.dreamsoccer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Tidak Ada!"));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setCreatedBy(user.getNama());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getNama()));
        userRepository.save(user);
        return ResponseEntity.ok("User berhasil ditambahkan!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        if (userRepository.existsById(user.getId())) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User currentUser = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User Tidak Ada!"));
            user.setCreatedBy(user.getNama());
            userRepository.save(user);
            return ResponseEntity.ok("User berhasil diperbarui.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User tidak ditemukan");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User berhasil dihapus.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User tidak ditemukan");
    }
}