package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.dto.validasi.ValLoginDTO;
import com.promptengineer.dreamsoccer.dto.validasi.ValOtpDTO;
import com.promptengineer.dreamsoccer.dto.validasi.ValRegisDTO;
import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public String register(@Valid ValRegisDTO valRegisDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            User user = userService.registerUser(valRegisDTO.getUsername(), valRegisDTO.getEmail(), valRegisDTO.getPassword());
            userService.sendOtpEmail(user);
            model.addAttribute("userId", user.getId());
            model.addAttribute("message", "Pendaftaran berhasil!");
            return "otp";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/otp")
    public String otpPage() {
        return "otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam Long userId, @Valid ValOtpDTO valOtpDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "otp";
        }
        boolean success = userService.verifyOtp(userId, valOtpDTO.getOtp());
        if (success) {
            model.addAttribute("message", "Akun Anda telah berhasil diverifikasi.");
            return "login";
        }
        model.addAttribute("error", "OTP tidak valid atau telah kedaluwarsa.");
        return "otp";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid ValLoginDTO valLoginDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "login";
        }
        try {
            userService.loginUser(valLoginDTO.getUsername(), valLoginDTO.getPassword());
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestParam Long userId) {
        userService.resendOtp(userId);
        return ResponseEntity.ok(Map.of("success", true));
    }
}