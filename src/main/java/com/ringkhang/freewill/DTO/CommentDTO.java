package com.ringkhang.freewill.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter @Setter
public class CommentDTO {
    private Long commentId;
    private String commentText;
    private Long userId;
    private Long postId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public CommentDTO(Long commentId, String commentText, Long userId, Long postId, Timestamp createTime, Timestamp updateTime) {
        this.commentId = commentId;
        this.commentText = commentText;
        this.userId = userId;
        this.postId = postId;
        this.createTime = createTime.toLocalDateTime();
        this.updateTime = updateTime.toLocalDateTime();
    }
}
