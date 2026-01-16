package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.DTO.CommentDTO;
import com.ringkhang.freewill.helperClasses.AnyResponse;
import com.ringkhang.freewill.services.CommentService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(String commentText , Long postId){
        try{
            commentService.addComment(commentText,postId);
            return new ResponseEntity<>("Added successful",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add comment",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Updates the comment.
    @PutMapping("/update")
    public ResponseEntity<AnyResponse<CommentDTO>> updateComment(@RequestParam String comment, @RequestParam Long cId){
        return commentService.updateComment(comment,cId);
    }

    // Delete comment
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteComment (@RequestParam Long cId){
        return commentService.deleteCommentPartial(cId);
    }
}