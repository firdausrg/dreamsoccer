package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    @Autowired
    private HttpSession httpSession;

    @GetMapping("/admin_dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("currentUrl", "/admin_dashboard");
        return "admin_dashboard";
    }

    @GetMapping("/lapangan")
    public String adminLapangan(Model model) {
        model.addAttribute("currentUrl", "/lapangan");
        return "lapangan";
    }

    @GetMapping("/booking")
    public String adminBooking(Model model) {
        model.addAttribute("currentUrl", "/booking");
        return "booking";
    }

    @GetMapping("/user")
    public String adminUser(Model model) {
        model.addAttribute("currentUrl", "/user");
        return "user";
    }

    @GetMapping("/reward")
    public String adminReward(Model model) {
        model.addAttribute("currentUrl", "/reward");
        return "reward";
    }

    @GetMapping("/laporan")
    public String adminLaporan(Model model) {
        model.addAttribute("currentUrl", "/laporan");
        return "laporan";
    }
    @GetMapping("/profile_admin")
    public String getProfileAdmin(Model model, Authentication authentication) {
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
        model.addAttribute("currentUrl", "/profile_admin");

        if (loggedInUser != null) {
            String userPhoto = loggedInUser.getFoto();
            boolean fileExists = isFileExist(userPhoto);

            if (userPhoto != null && !fileExists) {
                userPhoto = "default.png";
            }

            model.addAttribute("user", loggedInUser);
            model.addAttribute("userPhoto", userPhoto);
        }

        return "profile_admin";
    }
    public boolean isFileExist(String filename) {
        if (filename == null) {
            return false;
        }
        Path path = Paths.get("uploads/foto-profil", filename);
        return Files.exists(path);
    }
    @GetMapping("/iklan")
    public String getIklan(Model model) {
        model.addAttribute("currentUrl", "/iklan");
        return "iklan";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/login?logout=true";
    }

    @GetMapping("/404")
    public String handleAccessDenied() {
        return "404";
    }

    @GetMapping("/back")
    public String goBack(Authentication authentication) {
        if (authentication != null) {
            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining());

            if (role.contains("ROLE_USER")) {
                return "redirect:/user_dashboard";
            }
            else if (role.contains("ROLE_ADMIN")) {
                return "redirect:/admin_dashboard";
            }
        }

        return "redirect:/404";
    }

    @GetMapping("/user_dashboard")
    public String userDashboard(Model model) {
        model.addAttribute("currentUrl", "/user_dashboard");
        return "user_dashboard";
    }

    @GetMapping("/event")
    public String event(Model model) {
        model.addAttribute("currentUrl", "/event");
        return "event";
    }

    @GetMapping("/detail_event")
    public String infoevent(Model model) {
        model.addAttribute("currentUrl", "/event/detail-event");
        return "detail_event";
    }

    @GetMapping("/data_lapangan")
    public String lapangan(Model model) {
        model.addAttribute("currentUrl", "/data_lapangan");
        return "data_lapangan";
    }

    @GetMapping("/detail_lapangan")
    public String infolapangan(Model model) {
        model.addAttribute("currentUrl", "/data_lapangan/detail-lapangan");
        return "detail_lapangan";
    }

    @GetMapping("/booking_lapangan")
    public String bookingLapangan(Model model) {
        model.addAttribute("currentUrl", "/booking_lapangan");
        return "booking_lapangan";
    }

    @GetMapping("/history_booking")
    public String daftarPesanan(Model model) {
        model.addAttribute("currentUrl", "/history_booking");
        return "history_booking";
    }

    @GetMapping("/daftar_reward")
    public String daftarReward(Model model) {
        model.addAttribute("currentUrl", "/daftar_reward");
        return "daftar_reward";
    }

    @GetMapping("/history_penukaran_reward")
    public String penukaranReward(Model model) {
        model.addAttribute("currentUrl", "/history_penukaran_reward");
        return "history_penukaran_reward";
    }

    @GetMapping("/profile_user")
    public String profileUser(Model model) {
        model.addAttribute("currentUrl", "/profile_user");
        return "profile_user";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("currentUrl", "/contact");
        return "contact";
    }
}
