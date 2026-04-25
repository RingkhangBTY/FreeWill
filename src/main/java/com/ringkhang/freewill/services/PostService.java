package com.ringkhang.freewill.services;

import com.ringkhang.freewill.DTO.CommentPostResponseDTO;
import com.ringkhang.freewill.DTO.PostUploadDTO;
import com.ringkhang.freewill.DTO.PostsResponseDTO;
import com.ringkhang.freewill.exception.RequestedResourceNotAvailable;
import com.ringkhang.freewill.exception.TimeOutException;
import com.ringkhang.freewill.exception.UnauthorizedException;
import com.ringkhang.freewill.models.Comments;
import com.ringkhang.freewill.models.Posts;
import com.ringkhang.freewill.repo.CommentRepo;
import com.ringkhang.freewill.repo.LikesRepo;
import com.ringkhang.freewill.repo.PostsRepo;
import com.ringkhang.freewill.util.CommonUtilMethods;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.processing.Find;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostsRepo postsRepo;
    private final CommentRepo commentRepo;
    private final UserServiceHelper userServiceHelper;
    private final LikesRepo likesRepo;

    public PostService(PostsRepo postsRepo,
                       CommentRepo commentRepo,
                       UserServiceHelper userServiceHelper,
                       LikesRepo likesRepo) {
        this.postsRepo = postsRepo;
        this.commentRepo = commentRepo;
        this.userServiceHelper = userServiceHelper;
        this.likesRepo = likesRepo;
    }

    //uploads new post
    @Transactional
    public ResponseEntity<String> uploadNewPost(PostUploadDTO post){
        try{
            Posts p = new Posts();
            p.setUser(userServiceHelper.getCurrentUserDetails());
            p.setPostText(post.getPostTest());

            Posts savePost = postsRepo.save(p);
            if (savePost.getPostId() != null) {
                return new ResponseEntity<>("Post added successfully",HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Failed to save post ",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
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

    // updates the post text but only after 24hr after that it rejects the request.
    @Transactional
    public void editPost(String newText, Long postId) {
        Posts p = postsRepo.findById(postId)
                .orElseThrow(()-> new RequestedResourceNotAvailable("No post found"));

        if (p.getUser().getUserId() != userServiceHelper.getCurrentUserId()) {
            throw new UnauthorizedException("Can't edit this post coz your not the author of this post ");

        } else if (p.getCreatedDate().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new TimeOutException("Unable to edit post after 1 day");
        }

        postsRepo.updatePostText(newText,postId);
    }

    // Gives current users all available posts.
    public List<PostsResponseDTO> getCurrentUserPosts() {

        List<Posts> postsList = postsRepo
                .findPostsByUserId(userServiceHelper.getCurrentUserId());

        List<PostsResponseDTO> postsResponseDTOList = new ArrayList<>();

        for (Posts posts : postsList){

            List<Comments> commentsList = commentRepo.findCommentsByPostId(posts.getPostId());

            List<CommentPostResponseDTO> commentPostResponseDTOList =
                    new ArrayList<>();

            for (Comments c : commentsList){
                commentPostResponseDTOList.add(
                        new CommentPostResponseDTO(
                                c.getUser().getUserId(),
                                c.getCommentId(),
                                c.getCommentText(),
                                c.getCreatedDate(),
                                c.getUpdateDate()
                        )
                );
            }

            PostsResponseDTO responseDTO = new PostsResponseDTO(
                    posts.getUser().getUserId(),
                    posts.getPostId(),
                    posts.getUser().getUsername(),
                    posts.getPostText(),
                    CommonUtilMethods.convertLocalDateTimeToTimestamp(posts.getCreatedDate()),
                    CommonUtilMethods.convertLocalDateTimeToTimestamp(posts.getUpdateDate()),
                    getLikeCount(posts.getPostId()),
                    commentPostResponseDTOList
            );

            postsResponseDTOList.add(responseDTO);
        }

        return postsResponseDTOList;
    }

    // returns like count
    public Long getLikeCount(Long postId){
        return likesRepo.getLikeCountByPost(postId);
    }

}