package com.promptengineer.dreamsoccer.dto.validasi;

import jakarta.validation.constraints.NotEmpty;

public class ValOtpDTO {
    @NotEmpty(message = "Id tidak ditemukan")
    private Long userId;

    @NotEmpty(message = "OTP tidak boleh kosong")
    private String otp;

    public Long getUserId() {
        return userId;
    }

    public void setId(Long userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}