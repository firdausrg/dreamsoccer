package com.promptengineer.dreamsoccer.security;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author pirda Pirdaus Ripa Atullah Gopur
Created on 05/02/2025 17:59
@Last Modified 05/02/2025 17:59
Version 1.0
*/
public class SecurityPage {
    public static final String[] PUBLIC_URLS = {
            "/auth/register", "/auth/login","/auth/login?error", "/auth/otp",
            "/auth/verify-otp", "/auth/resend-otp", "/assets/**"
    };

    public static final String[] ADMIN_URLS = {
            "/admin_dashboard", "/lapangan", "/api/lapangan", "/user/list", "/booking", "/api/booking", "/reward",
            "/laporan", "/profile_admin", "/updateProfile", "/iklan"
    };

    public static final String[] USER_URLS = {
            "/user/list", "/api/booking", "/api/lapangan", "/user_dashboard", "/event", "/data-lapangan", "/booking_lapangan",
            "/history_booking", "/daftar_reward", "/history_penukaran_reward",
            "/profile_user", "/contact", "/event/detail-event", "/data_lapangan/detail-lapangan"
    };

    public static final String[] ADMIN_AND_USER_URLS = {
            "/user/list", "/api/booking", "/api/lapangan"
    };
}
