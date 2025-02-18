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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ResponseEntity<?> register(@Valid @RequestBody ValRegisDTO valRegisDTO,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Data tidak valid"));
        }

        String password = valRegisDTO.getPassword();
        if (!isValidPassword(password)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password tidak valid"));
        }

        try {
            User user = userService.registerUser(valRegisDTO.getNama(), valRegisDTO.getUsername(),
                    valRegisDTO.getEmail(), password);
            userService.sendOtpEmail(user);
            String encryptedUserId = EncryptionUtil.encrypt(String.valueOf(user.getId()));

            return ResponseEntity.ok(Map.of("success", "Pendaftaran berhasil!", "encryptedUserId", encryptedUserId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Terjadi kesalahan saat mendaftar"));
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
//        System.out.println("Encrypted UserId: " + userId);
        String decryptedUserId = EncryptionUtil.decrypt(userId);
//        System.out.println("Decrypted UserId: " + decryptedUserId);
        model.addAttribute("userId", decryptedUserId);
        return "otp";
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<Object> verifyOtp(@RequestBody ValOtpDTO valOtpDTO,
                                            BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        Long userId = valOtpDTO.getUserId();
        if (!userService.isUserExists(userId)) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", "User ID tidak valid.")
            );
        }
        if (result.hasErrors()) {
            session.setAttribute("userId", userId);
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", "Invalid OTP data")
            );
        }

        boolean success = userService.verifyOtp(userId, valOtpDTO.getOtp());
        if (success) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "OTP verified successfully"));
        }

        String encryptedUserId = EncryptionUtil.encrypt(String.valueOf(userId));

        redirectAttributes.addFlashAttribute("error", "OTP tidak valid atau telah kedaluwarsa.");
        redirectAttributes.addFlashAttribute("encryptedUserId", encryptedUserId);

        return ResponseEntity.badRequest().body(
                Map.of("status", "error", "message", "OTP tidak valid atau telah kedaluwarsa.", "redirectUrl", "/auth/otp?userId=" + encryptedUserId)
        );
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