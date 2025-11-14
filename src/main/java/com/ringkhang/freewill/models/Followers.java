package com.ringkhang.freewill.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "followers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follow_user_id", "follows_user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Followers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    // Follower (the one who follows)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_user_id", nullable = false)
    private User follower;

    // The user being followed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follows_user_id", nullable = false)
    private User follows;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "update_date")
    private LocalDateTime updateDate = LocalDateTime.now();
}