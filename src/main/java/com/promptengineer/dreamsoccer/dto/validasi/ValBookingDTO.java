package com.promptengineer.dreamsoccer.dto.validasi;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author pirda Pirdaus Ripa Atullah Gopur
Created on 05/02/2025 22:51
@Last Modified 05/02/2025 22:51
Version 1.0
*/
import jakarta.validation.constraints.NotNull;

public class ValBookingDTO {

    @NotNull
    private Long userId;

    @NotNull
    private String date;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @NotNull
    private Long courtId;

    @NotNull
    private String price;

    @NotNull
    private String dp;

    @NotNull
    private String remainingPayment;

    private String description;

    @NotNull
    private String paymentStatus;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getRemainingPayment() {
        return remainingPayment;
    }

    public void setRemainingPayment(String remainingPayment) {
        this.remainingPayment = remainingPayment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
