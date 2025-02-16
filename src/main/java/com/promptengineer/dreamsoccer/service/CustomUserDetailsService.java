package com.promptengineer.dreamsoccer.service;

import com.promptengineer.dreamsoccer.model.Status;
import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession httpSession;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan!"));

        if (user.getVerified() != Status.AKTIF) {
            throw new InternalAuthenticationServiceException("Akun Anda tidak aktif. Silakan hubungi admin.");
        }
        httpSession.setAttribute("loggedInUser", user);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getVerified() == Status.AKTIF,
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
