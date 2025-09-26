package com.garage_system.DTO;

public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Object metadata;

    public ApiResponse(String status, String message, T data, Object metadata) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.metadata = metadata;
    }

    // Getters and setters omitted for brevity
}
