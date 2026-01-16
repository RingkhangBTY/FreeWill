package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.DTO.PostUploadDTO;
import com.ringkhang.freewill.DTO.PostsResponseDTO;
import com.ringkhang.freewill.services.PostService;
import com.ringkhang.freewill.helperClasses.AnyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //Uploads a post
    @PostMapping("/upload")
    public ResponseEntity<String> uploadNewPost(@RequestBody PostUploadDTO post){
        if (post.getPostTest().isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("Reachieved empty string");
        }
        return postService.uploadNewPost(post);
    }

    @GetMapping("/d-posts")
    public ResponseEntity<List<PostsResponseDTO>> getAllPost(){
        try{
            List<PostsResponseDTO> responseDTOList = postService.getRankedPosts();

            if (!responseDTOList.isEmpty()){
                return new ResponseEntity<>(responseDTOList, HttpStatus.OK);
            }

            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // to edit post test but only allow before 24hr letter user can't edit post
    @PutMapping("/edit")
    public ResponseEntity<AnyResponse<PostsResponseDTO>> editPost(@RequestParam String newText, @RequestParam Long postId){
        return postService.editPost(newText,postId);
    }

}