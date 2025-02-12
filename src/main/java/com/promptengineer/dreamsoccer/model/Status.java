package com.promptengineer.dreamsoccer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    SELESAI("Selesai"),
    BELUM_SELESAI("Belum Selesai"),
    MENUNGGU("Menunggu"),
    AKTIF("Aktif"),
    TIDAK_AKTIF("Tidak Aktif"),
    BELUM_LUNAS("Belum Lunas"),
    LUNAS("Lunas");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Status fromString(String value) {
        for (Status status : Status.values()) {
            if (status.value.equalsIgnoreCase(value.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }
}
