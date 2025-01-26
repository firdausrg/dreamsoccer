package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String email, @RequestParam String password, Model model) {
        try {
            User user = userService.registerUser(username, email, password);
            model.addAttribute("message", "Pendaftaran berhasil! Silakan periksa email Anda untuk memverifikasi akun.");
            return "login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token, Model model) {
        boolean isVerified = userService.verifyAccount(token);
        if (isVerified) {
            model.addAttribute("message", "Akun Anda telah berhasil diverifikasi.");
        } else {
            model.addAttribute("error", "Token verifikasi tidak valid.");
        }
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        boolean success = userService.loginUser(username, password);
        if (success) {
            User user = userService.findUserByUsername(username).orElseThrow();
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Username atau password salah, atau akun belum diverifikasi.");
        return "login";
    }
}
