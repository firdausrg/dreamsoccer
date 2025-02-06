package com.promptengineer.dreamsoccer.dto.validasi;

import jakarta.validation.constraints.NotEmpty;

public class ValOtpDTO {
    @NotEmpty(message = "OTP tidak boleh kosong")
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}