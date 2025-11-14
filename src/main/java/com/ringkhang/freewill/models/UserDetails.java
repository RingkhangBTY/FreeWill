package com.ringkhang.freewill.models;

import com.ringkhang.freewill.config.JsonToMapConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "user_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @Column(name = "bio")
    private String bio;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @Convert(converter = JsonToMapConverter.class)
    private Map<String,Object> metadata;
//    private String metadata;   // OR use Map<String,Object> with custom converter

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "update_date")
    private LocalDateTime updateDate = LocalDateTime.now();
}