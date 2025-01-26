package com.promptengineer.dreamsoccer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

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
    @GetMapping("/log-aktivitas")
    public String adminLogAktivitas(Model model) {
        model.addAttribute("currentUrl", "/log-aktivitas");
        return "log-aktivitas";
    }
    @GetMapping("/profile-admin")
    public String getProfileAdmin(Model model) {
        model.addAttribute("currentUrl", "/profile-admin");
        return "profile-admin";
    }
    @GetMapping("/iklan")
    public String getIklan(Model model) {
        model.addAttribute("currentUrl", "/iklan");
        return "iklan";
    }
    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
    @GetMapping("/user_dashboard")
    public String userDashboard(Model model) {
        model.addAttribute("currentUrl", "/user_dashboard");
        return "user_dashboard";
    }
}
