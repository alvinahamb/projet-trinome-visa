package com.itu.visabackoffice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    
    @JsonProperty("code")
    private int code;
    
    @JsonProperty("status")
    private String status; // "success" or "error"
    
    @JsonProperty("data")
    private T data; // null if status is error
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("error")
    private String error; // null if status is success
    
    /**
     * Factory method pour créer une réponse réussie
     */
    public static <T> ApiResponse<T> success(int code, T data, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .status("success")
                .data(data)
                .message(message)
                .error(null)
                .build();
    }
    
    /**
     * Factory method pour créer une réponse réussie avec code 200
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return success(200, data, message);
    }
    
    /**
     * Factory method pour créer une réponse d'erreur
     */
    public static <T> ApiResponse<T> error(int code, String errorMessage, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .status("error")
                .data(null)
                .message(message)
                .error(errorMessage)
                .build();
    }
    
    /**
     * Factory method pour créer une réponse d'erreur avec code 400
     */
    public static <T> ApiResponse<T> error(String errorMessage, String message) {
        return error(400, errorMessage, message);
    }
}
