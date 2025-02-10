package com.promptengineer.dreamsoccer.dto.validasi;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ValLoginDTO {
    @NotEmpty(message = "Username Tidak Boleh Kosong")
    @Size(min = 3, max = 50, message = "Username harus antara 3 dan 50 karakter")
    private String username;

    @NotEmpty(message = "Password tidak boleh kosong")
    @Size(min = 6, max = 50, message = "Password harus antara 6 dan 50 karakter")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}