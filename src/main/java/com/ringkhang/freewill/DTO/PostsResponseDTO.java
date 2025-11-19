package com.ringkhang.freewill.DTO;

import com.ringkhang.freewill.models.Comments;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostsResponseDTO {

    private Long userId;
    private Long postId;
    private String username;
    private String postText;
    private LocalDateTime postCreateTime;
    private LocalDateTime postUpdateTime;
    private Long likeCount;
    private List<CommentPostResponseDTO> comments;

    public PostsResponseDTO(Long userId, Long postId, String username , String postText, Timestamp postCreateTime, Timestamp postUpdateTime, Long likeCount, List<CommentPostResponseDTO> comments) {
        this.userId = userId;
        this.postId = postId;
        this.username = username;
        this.postText = postText;
        this.postCreateTime = postCreateTime.toLocalDateTime();
        this.postUpdateTime = postUpdateTime.toLocalDateTime();
        this.likeCount = likeCount;
        this.comments = comments;
    }
}