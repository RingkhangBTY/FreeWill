package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.DTO.CommentDTO;
import com.ringkhang.freewill.helperClasses.AnyResponse;
import com.ringkhang.freewill.services.CommentService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(
            @RequestParam @NotBlank String commentText ,
            @RequestParam @NotNull Long postId) {

        commentService.addComment(commentText, postId);

        return new ResponseEntity<>("Added successful", HttpStatus.OK);
    }

    //Updates the comment.
    @PutMapping("/update")
    public ResponseEntity<AnyResponse<CommentDTO>> updateComment(
            @RequestParam @NotBlank String comment,
            @RequestParam @NotNull Long cId){

        CommentDTO commentDTO = commentService.updateComment(comment,cId);

        return ResponseEntity.status(HttpStatus.OK).
                body(new AnyResponse<>("Comment updated successfully",commentDTO));
    }

    // Delete comment
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteComment (
            @RequestParam @NotNull(message = "CId can't be null") Long cId){

        commentService.deleteCommentPermanent(cId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Comment deleted successfully");
    }
}