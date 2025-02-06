package com.promptengineer.dreamsoccer.dto.validasi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ValLoginDTO {
    @NotNull(message = "Username Tidak Boleh Null")
    @NotEmpty(message = "Username Tidak Boleh Kosong")
    @NotBlank(message = "Username Tidak Boleh Blank")
    @Size(min = 3, max = 50, message = "Username harus antara 3 dan 50 karakter")
    private String username;

    @NotNull(message = "Password Tidak Boleh Null")
    @NotEmpty(message = "Password Tidak Boleh Kosong")
    @NotBlank(message = "Password Tidak Boleh Blank")
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