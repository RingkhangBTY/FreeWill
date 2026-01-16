package com.ringkhang.freewill.services;

import com.ringkhang.freewill.DTO.CommentDTO;
import com.ringkhang.freewill.helperClasses.AnyResponse;
import com.ringkhang.freewill.helperClasses.TimeUnit;
import com.ringkhang.freewill.models.Comments;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.CommentRepo;
import com.ringkhang.freewill.util.CommonUtilMethods;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserService userService;

    public CommentService(CommentRepo commentRepo , UserService userService) {
        this.commentRepo = commentRepo;
        this.userService = userService;
    }

    // Added new comment to the post
    @Transactional
    public void addComment(String commentText, Long postId) {
        Long userId = userService.getCurrentUserId();
        commentRepo.addComment(commentText,postId,userId);
    }



    // Updates the comment but only before 1hr
    @Transactional
    public ResponseEntity<AnyResponse<CommentDTO>> updateComment(String comment, Long cId) {
        Objects.requireNonNull(comment);
        Objects.requireNonNull(cId);

        Optional<Comments> c = commentRepo.findById(cId);

        if (c.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new AnyResponse<>(
                    "No comment found !",null)
                    );
        } else if (!Objects.equals(c.get().getUser().getUserId(), userService.getCurrentUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new AnyResponse<>(
                            "Can't edit this comment coz your not the author of this comment ",null)
                    );
        } else if (CommonUtilMethods.timeDifference(c.get().getCreatedDate(), TimeUnit.HOURS)>1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new AnyResponse<>(
                            "Can't edit comment after 1 hr",null)
                    );
        }

        commentRepo.updateComment(comment,cId, LocalDateTime.now());
        Optional<Comments> updatedComment = commentRepo.findById(cId);

        CommentDTO commentDTO = new CommentDTO(
                updatedComment.get().getCommentId(),
                updatedComment.get().getCommentText(),
                updatedComment.get().getUser().getUserId(),
                updatedComment.get().getPost().getPostId(),
                CommonUtilMethods.convertLocalDateTimeToTimestamp(updatedComment.get().getCreatedDate()),
                CommonUtilMethods.convertLocalDateTimeToTimestamp(updatedComment.get().getUpdateDate())
        );

        return ResponseEntity.status(HttpStatus.OK).
                body(new AnyResponse<>(
                        "Comment update successfully .",commentDTO)
                );
    }

    // Delete comment
    public ResponseEntity<String> deleteCommentPartial(Long cId) {

        if (cId == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Comment id must not be null");
        }

        Optional<Comments> c = commentRepo.findById(cId);

        if (c.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("No comment found !");
        } else if (!Objects.equals(c.get().getUser().getUserId(), userService.getCurrentUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body("Can't delete this comment coz your not the author of this comment ");
        } else if (CommonUtilMethods.timeDifference(c.get().getCreatedDate(), TimeUnit.HOURS)<12) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body("Can't delete comment after 12 hr");
        }

        int rowEffected = commentRepo.deleteComment(cId);
        if (rowEffected>=1){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).
                    body("Deleted comment successfully");
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Comment was already deleted");
    }
}





