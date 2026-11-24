package com.pawradise.lostandfoundservice.util;

public final class Coordinates {

    private Coordinates() {
    }

    /** Throws IllegalArgumentException (-> HTTP 400) if a coordinate is out of range. Null values are allowed. */
    public static void validate(Double latitude, Double longitude) {
        if (latitude != null && (latitude < -90 || latitude > 90)) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        }
        if (longitude != null && (longitude < -180 || longitude > 180)) {
            throw new IllegalArgumentException("longitude must be between -180 and 180");
        }
    }
}