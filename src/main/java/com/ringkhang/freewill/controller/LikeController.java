package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.services.LikeService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * Add like to a Post
     *
     * @param postId id of {@code Posts} which the current user trying to like
     * @return Return a ResponseEntity object whit task succession status & fail/success message
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<?>likePost(
            @PathVariable @NotNull(message = "Post id can't be null") Long postId){

        likeService.addLike(postId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Liked successfully");
    }

    /**
     * Remove like from a post.
     * @param postId id of {@code Posts} from which the current user trying to remove like
     * @return Return a ResponseEntity object whit task succession status & fail/success message
     */
    @DeleteMapping("{postId}/un-like")
    public ResponseEntity<?> unlikePost(
            @PathVariable @NotNull(message = "Post id can't be null") Long postId){

        likeService.removeLike(postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Un liked successfully");
    }

}