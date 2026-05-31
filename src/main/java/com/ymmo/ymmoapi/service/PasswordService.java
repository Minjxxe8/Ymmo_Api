package com.ymmo.ymmoapi.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordService {
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String hashPassword(String unhashedPassword) {
        return passwordEncoder.encode(unhashedPassword);
    }

    public boolean verifyPassword(String unhashedPassword, String hashedPassword) {
        return passwordEncoder.matches(unhashedPassword, hashedPassword);
    }
}
