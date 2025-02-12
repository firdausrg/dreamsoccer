package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.Role;
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
        try {
            String loggedInUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User currentUser = userService.findByUsername(loggedInUsername)
                    .orElseThrow(() -> new RuntimeException("User Tidak Ada!"));
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setCreatedBy(currentUser.getNama());
            user.setUsername(user.getNama());
            user.setPassword(passwordEncoder.encode(user.getNama()));
            user.setRole(Role.USER);
            user.setFoto("default.png");
            userRepository.save(user);
            return ResponseEntity.ok("User berhasil ditambahkan!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan saat menambahkan user: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        try {
            if (userRepository.existsById(user.getId())) {
                String loggedInUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
                User currentUser = userService.findByUsername(loggedInUsername)
                        .orElseThrow(() -> new RuntimeException("User Tidak Ada!"));
                User userToUpdate = userRepository.findById(user.getId())
                        .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

                userToUpdate.setNama(user.getNama());
                userToUpdate.setEmail(user.getEmail());
                userToUpdate.setNoTelpon(user.getNoTelpon());
                userToUpdate.setAlamat(user.getAlamat());
                userToUpdate.setVerified(user.getVerified());
                userToUpdate.setUpdateBy(currentUser.getNama());

                userRepository.save(userToUpdate);
                return ResponseEntity.ok("User berhasil diperbarui.");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User tidak ditemukan");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan saat memperbarui user: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") Long id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return ResponseEntity.ok("User berhasil dihapus.");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User tidak ditemukan");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan saat menghapus user: " + e.getMessage());
        }
    }
}
