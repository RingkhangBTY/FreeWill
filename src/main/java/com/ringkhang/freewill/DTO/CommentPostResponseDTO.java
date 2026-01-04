package com.ringkhang.freewill.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPostResponseDTO {
    private Long userId;
    private Long commentId;
    private String commentText;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
