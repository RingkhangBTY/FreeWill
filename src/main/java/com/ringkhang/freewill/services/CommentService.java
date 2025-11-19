package com.ringkhang.freewill.services;

import com.ringkhang.freewill.models.Comments;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.CommentRepo;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserService userService;

    public CommentService(CommentRepo commentRepo , UserService userService) {
        this.commentRepo = commentRepo;
        this.userService = userService;
    }

    // Added new comment to the post
    public void addComment(String commentText, Long postId) {
        Long userId = userService.getCurrentUserId();
        commentRepo.addComment(commentText,postId,userId);
    }
}
