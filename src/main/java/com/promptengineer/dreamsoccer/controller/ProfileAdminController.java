package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.repository.UserRepository;
import com.promptengineer.dreamsoccer.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ProfileAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserRepository userRepository;

    private final String uploadDir = "src/main/resources/static/uploads/foto-profil/";

    @PostMapping("/updateProfile")
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

        return "redirect:/profile_admin";
    }

}
