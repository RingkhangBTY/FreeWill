package com.ringkhang.freewill.DTO;

import lombok.*;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String error;
    private String details;
}