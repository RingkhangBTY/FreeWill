package com.ringkhang.freewill.DTO;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDTO {

    private Long userId;
    private String username;
    private String bio;
    private LocalDateTime userCreateDate;

    public UserResponseDTO(Long userId, String username, String bio, Timestamp createdDate) {
        this.userId = userId;
        this.username = username;
        this.bio = bio;
        this.userCreateDate = createdDate.toLocalDateTime();
    }
}