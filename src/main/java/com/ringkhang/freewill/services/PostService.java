package com.ringkhang.freewill.services;

import com.ringkhang.freewill.DTO.CommentPostResponseDTO;
import com.ringkhang.freewill.DTO.PostUploadDTO;
import com.ringkhang.freewill.DTO.PostsResponseDTO;
import com.ringkhang.freewill.models.Comments;
import com.ringkhang.freewill.models.Posts;
import com.ringkhang.freewill.repo.CommentRepo;
import com.ringkhang.freewill.repo.PostsRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final UserService userService;
    private final PostsRepo postsRepo;
    private final CommentRepo commentRepo;


    public PostService(UserService userService, PostsRepo postsRepo , CommentRepo commentRepo) {
        this.userService = userService;
        this.postsRepo = postsRepo;
        this.commentRepo = commentRepo;
    }

    //To upload new post
    public ResponseEntity<String> uploadNewPost(PostUploadDTO post){
        Posts p = new Posts();

        try{
            p.setUser(userService.getCurrentUserDetails());
            p.setPostText(post.getPostTest());

            Posts savePost = postsRepo.save(p);
            if (savePost.getPostId() != null) {
//                return ResponseEntity.ok("Post added successfully");
                return new ResponseEntity<>("Post added successfully",HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Failed to save post ",HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Failed to process the request",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Gives all posts for now // will apply custom post ranking system
    public List<PostsResponseDTO> getRankedPosts() {
        List<Object []> postList = postsRepo.getRankedPosts();
        List<PostsResponseDTO> result = new ArrayList<>();

        for (Object[] row : postList) {

            Long userId = ((Number) row[0]).longValue();
            Long postId = ((Number) row[1]).longValue();
            String username = (String) row[2];
            String postText = (String) row[3];
            Timestamp created = (Timestamp) row[4];
            Timestamp updated = (Timestamp) row[5];
            Long likeCount = ((Number) row[6]).longValue();

            List<Comments> comments = commentRepo.findCommentsByPostId(postId);
            List<CommentPostResponseDTO> commentPostResponseDTO = new ArrayList<>();
            for (Comments c : comments){
                Long commentUserId = c.getUser().getUserId();
                Long commentId = c.getCommentId();
                String text = c.getCommentText();
                LocalDateTime createTime = c.getCreatedDate();
                LocalDateTime updateTime = c.getUpdateDate();
                
                CommentPostResponseDTO commentDTO = new CommentPostResponseDTO(
                        commentUserId,commentId,text,createTime,updateTime
                );
                commentPostResponseDTO.add(commentDTO);
            }

            PostsResponseDTO dto = new PostsResponseDTO(
                    userId, postId, username, postText, created, updated, likeCount, commentPostResponseDTO
            );

            result.add(dto);
        }
        return result;
    }

    // Gives current users all available posts.
    public List<Posts> getCurrentUserPosts() {
        return postsRepo.findPostsByUserId(userService.getCurrentUserDetails().getUserId());
    }
}