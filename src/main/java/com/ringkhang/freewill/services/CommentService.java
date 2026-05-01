package com.ringkhang.freewill.services;

import com.ringkhang.freewill.DTO.CommentDTO;
import com.ringkhang.freewill.exception.*;
import com.ringkhang.freewill.models.Comments;
import com.ringkhang.freewill.models.Posts;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.CommentRepo;
import com.ringkhang.freewill.repo.PostsRepo;
import com.ringkhang.freewill.util.CommonUtilMethods;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserServiceHelper userServiceHelper;
    private final PostsRepo postsRepo;

    public CommentService(CommentRepo commentRepo , UserServiceHelper userServiceHelper, PostsRepo postsRepo) {
        this.commentRepo = commentRepo;
        this.userServiceHelper = userServiceHelper;
        this.postsRepo = postsRepo;
    }

    // Added new comment to the post
    @Transactional
    public void addComment(String commentText, Long postId) {

        if (commentText == null || commentText.isBlank()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }

        User user = userServiceHelper.getCurrentUserDetails();

        Posts post = postsRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotAvailable("No post found."));

        Comments comment = new Comments();
        comment.setCommentText(commentText);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedDate(LocalDateTime.now());

        commentRepo.save(comment);
    }

    // Updates the comment but only before 1hr
    @Transactional
    public CommentDTO updateComment(String comment, Long cId) {

        Comments commentsEntity = commentRepo.findById(cId).
                orElseThrow(()-> new ResourceNotAvailable("No comment found! ") );

        if (!Objects.equals(commentsEntity.getUser().getUserId(), userServiceHelper.getCurrentUserId())) {
            throw new UnauthorizedException("Can't edit this comment coz your not the author of this comment ");
        }
        if (commentsEntity.getCreatedDate()
                .plusHours(1)
                .isBefore(LocalDateTime.now())){
            throw new TimeOutException("Can't edit comment after 1 hr");
        }

        commentsEntity.setCommentText(comment);
        commentsEntity.setUpdateDate(LocalDateTime.now());

        return new CommentDTO(
                commentsEntity.getCommentId(),
                commentsEntity.getCommentText(),
                commentsEntity.getUser().getUserId(),
                commentsEntity.getPost().getPostId(),
                CommonUtilMethods.convertLocalDateTimeToTimestamp(commentsEntity.getCreatedDate()),
                CommonUtilMethods.convertLocalDateTimeToTimestamp(commentsEntity.getUpdateDate())
        );
    }

    // Delete comment permanently
    public void deleteCommentPermanent(Long cId) {

        Comments commentEntity = commentRepo.findById(cId).
                orElseThrow(()-> new ResourceNotAvailable("No comment found!"));

        if (!Objects.equals(commentEntity.getUser().getUserId(), userServiceHelper.getCurrentUserId())) {
            throw new UnauthorizedException(
                    "Can't delete this comment coz your not the author of this comment ");
        }

        if (commentEntity.getCreatedDate()
                .plusHours(12)
                .isBefore(LocalDateTime.now())){
            throw new TimeOutException("Can't delete comment after 12 hr");
        }

        int rowEffected = commentRepo.deleteComment(cId);

        if (rowEffected == 0 ){
            throw new RuntimeException("Fails tp delete comment");
        }
    }
}





