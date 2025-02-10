package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.dto.validasi.ValLoginDTO;
import com.promptengineer.dreamsoccer.dto.validasi.ValOtpDTO;
import com.promptengineer.dreamsoccer.dto.validasi.ValRegisDTO;
import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.service.UserService;
import com.promptengineer.dreamsoccer.util.EncryptionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/register")
    public String registerPage(Model model)  {
        if (!model.containsAttribute("valRegisDTO")) {
            model.addAttribute("valRegisDTO", new ValRegisDTO());
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid ValRegisDTO valRegisDTO,
                           BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        String password = valRegisDTO.getPassword();
        if (!isValidPassword(password)) {
            redirectAttributes.addFlashAttribute("error", "Password harus mengandung huruf besar, huruf kecil, angka, dan simbol.");
            return "redirect:/auth/register";
        }
        try {
            User user = userService.registerUser(valRegisDTO.getNama(), valRegisDTO.getUsername(), valRegisDTO.getEmail(), valRegisDTO.getPassword());
            userService.sendOtpEmail(user);
            String encryptedUserId = Base64.getEncoder().encodeToString(user.getId().toString().getBytes());

            redirectAttributes.addFlashAttribute("success", "Pendaftaran berhasil! Mengarahkan ke halaman OTP...");
            redirectAttributes.addFlashAttribute("encryptedUserId", encryptedUserId);

            return "redirect:/auth/register";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "register";
        }
    }


    private boolean isValidPassword(String password) {
        return password.length() >= 6 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[!@#$%^&*(),.?\":{}|<>_].*");
    }

    @GetMapping("/otp")
    public String showOtpPage(@RequestParam String userId, Model model) {
        String decryptedUserId = EncryptionUtil.decrypt(userId);
        model.addAttribute("userId", decryptedUserId);
        return "otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam Long userId, @Valid ValOtpDTO valOtpDTO,
                            BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        if (result.hasErrors()) {
            session.setAttribute("userId", userId);
            return "otp";
        }
        boolean success = userService.verifyOtp(userId, valOtpDTO.getOtp());
        if (success) {
            redirectAttributes.addAttribute("verified", "true");
            return "redirect:/auth/login";
        }

        String encryptedUserId = Base64.getEncoder().encodeToString(userId.toString().getBytes());
        redirectAttributes.addFlashAttribute("error", "OTP tidak valid atau telah kedaluwarsa.");
        redirectAttributes.addFlashAttribute("encryptedUserId", encryptedUserId);

        return "redirect:/auth/otp?userId=" + encryptedUserId;
    }

    @GetMapping("/login")
    public String loginPage(Model model,
                            @RequestParam(value = "success", required = false) String success,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "message", required = false) String message,
                            @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {

        if (!model.containsAttribute("valLoginDTO")) {
            model.addAttribute("valLoginDTO", new ValLoginDTO());
        }

        if (success != null && success.equals("true")) {
            model.addAttribute("success", "Login berhasil! Mengarahkan ke dashboard...");
            model.addAttribute("redirectUrl", redirectUrl);
        }

        if (error != null && error.equals("true")) {
            model.addAttribute("error", "true");
            model.addAttribute("message", message != null ? message : "Terjadi kesalahan!");
        }

        return "login";
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestParam Long userId) {
        userService.resendOtp(userId);
        return ResponseEntity.ok(Map.of("success", true));
    }
}