package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            System.out.println("Error (comment adding): "+e.getMessage());
            return new ResponseEntity<>("Failed to add comment",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}