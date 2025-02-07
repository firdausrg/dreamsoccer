package com.promptengineer.dreamsoccer.service;

import com.promptengineer.dreamsoccer.model.Role;
import com.promptengineer.dreamsoccer.model.Status;
import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username Sudah Terdaftar");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email Sudah Terdaftar");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setVerified(Status.TIDAK_AKTIF);
        user.setRole(Role.USER);

        userRepository.save(user);
        return user;
    }

    public void sendOtpEmail(User user) {
        try {
            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpCreatedAt(LocalDateTime.now());
            userRepository.save(user);

            String subject = "Kode OTP Verifikasi Akun Anda";
            String body = buildOtpEmailBody(user.getUsername(), otp);

            sendEmail(user.getEmail(), subject, body);
            System.out.println("Email OTP berhasil dikirim ke " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Gagal mengirim email OTP: " + e.getMessage());
        }
    }

    private String buildOtpEmailBody(String namaPengguna, String otp) {
        return "Halo " + namaPengguna + ",\n\n"
                + "Berikut adalah kode OTP Anda: *" + otp + "*\n\n"
                + "Kode ini berlaku selama 5 menit. Jangan berikan kepada siapa pun.\n\n"
                + "Salam,\n"
                + "Tim Dukungan Kami";
    }


    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 9000) + 1000);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public boolean verifyOtp(Long userId, String otp) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getOtp().equals(otp) && !isOtpExpired(user)) {
            user.setVerified(Status.AKTIF);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private boolean isOtpExpired(User user) {
        return Duration.between(user.getOtpCreatedAt(), LocalDateTime.now()).toMinutes() > 5;
    }

    public void loginUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("Username tidak terdaftar.");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Password salah.");
        }
        if (user.getVerified() != Status.AKTIF) {
            throw new IllegalArgumentException("Akun belum diverifikasi.");
        }
    }
    public void resendOtp(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Pengguna tidak ditemukan"));

            if (user.getOtpCreatedAt() != null && Duration.between(user.getOtpCreatedAt(), LocalDateTime.now()).toMinutes() < 1) {
                throw new IllegalStateException("Harap tunggu 1 menit sebelum meminta OTP lagi.");
            }

            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpCreatedAt(LocalDateTime.now());
            userRepository.save(user);

            String subject = "Kode OTP Baru untuk Verifikasi Akun Anda";
            String body = buildOtpEmailBody(user.getUsername(), otp);

            sendEmail(user.getEmail(), subject, body);
            System.out.println("OTP baru berhasil dikirim ke " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Gagal mengirim ulang OTP: " + e.getMessage());
        }
    }

}