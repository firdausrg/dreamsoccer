package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.repository.UserRepository;
import com.promptengineer.dreamsoccer.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Controller
public class ProfileUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String uploadDir = "src/main/resources/static/uploads/foto-profil/";

    @PostMapping("/updateProfileUser")
    @Transactional
    public String updateProfile(@RequestParam String nama, @RequestParam String email,
                                @RequestParam String noTelpon, @RequestParam String alamat,
                                @RequestParam("foto") MultipartFile foto,
                                Authentication authentication, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            User userToUpdate = userRepository.findById(loggedInUser.getId())
                    .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

            userToUpdate.setNama(nama);
            userToUpdate.setEmail(email);
            userToUpdate.setNoTelpon(noTelpon);
            userToUpdate.setAlamat(alamat);

            if (foto != null && !foto.isEmpty()) {
                try {
                    String oldFileName = userToUpdate.getFoto();
                    String fileName = System.currentTimeMillis() + "_" + foto.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);

                    Files.createDirectories(filePath.getParent());
                    Files.write(filePath, foto.getBytes());

                    if (oldFileName != null && !"default.png".equals(oldFileName)) {
                        Path oldFilePath = Paths.get(uploadDir, oldFileName);
                        Files.deleteIfExists(oldFilePath);
                    }

                    userToUpdate.setFoto(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("uploadError", "Gagal mengunggah foto.");
                }
            } else {
                if (userToUpdate.getFoto() == null || "default.png".equals(userToUpdate.getFoto())) {
                    userToUpdate.setFoto("default.png");
                }
            }

            userToUpdate.setUpdateBy(loggedInUser.getNama());
            userService.updateUser(userToUpdate);

            httpSession.setAttribute("loggedInUser", userToUpdate);

            redirectAttributes.addFlashAttribute("updateSuccess", true);
        }

        return "redirect:/profile_user";
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> requestData) {

        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User tidak terautentikasi."));
        }

        String currentPassword = requestData.get("currentPassword");
        String newPassword = requestData.get("newPassword");
        String confirmNewPassword = requestData.get("confirmNewPassword");

        if (currentPassword == null || newPassword == null || confirmNewPassword == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Semua kolom harus diisi."));
        }

        if (!passwordEncoder.matches(currentPassword, loggedInUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Password lama salah."));
        }

        if (newPassword.length() < 8 || !newPassword.matches(".*[A-Z].*") || !newPassword.matches(".*\\d.*")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password baru harus minimal 8 karakter, mengandung huruf besar dan angka."));
        }

        if (!newPassword.equals(confirmNewPassword)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Konfirmasi password tidak cocok."));
        }

        loggedInUser.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(loggedInUser);

        httpSession.setAttribute("loggedInUser", loggedInUser);

        return ResponseEntity.ok(Map.of("message", "Password berhasil diperbarui."));
    }

}
