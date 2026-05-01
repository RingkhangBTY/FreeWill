package com.ringkhang.freewill.services;

import com.ringkhang.freewill.exception.CreateNewResourceException;
import com.ringkhang.freewill.exception.ResourceNotAvailable;
import com.ringkhang.freewill.models.Likes;
import com.ringkhang.freewill.models.Posts;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.LikesRepo;
import com.ringkhang.freewill.repo.PostsRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikesRepo likesRepo;
    private final PostsRepo postsRepo;
    private final UserServiceHelper userServiceHelper;

    public LikeService(LikesRepo likesRepo, PostsRepo postsRepo, UserServiceHelper userServiceHelper) {
        this.likesRepo = likesRepo;
        this.postsRepo = postsRepo;
        this.userServiceHelper = userServiceHelper;
    }


    /**
     * To add like to an POST
     *
     * @param postId The post I'd which the current user trying to like
     */
    @Transactional
    public void addLike(
            @NotNull(message = "Post id can't be null") Long postId) {

        Posts post = postsRepo.findById(postId)
                .orElseThrow(
                        ()-> new ResourceNotAvailable("No post available. ")
                );

        User user = userServiceHelper.getCurrentUserDetails();

        if (existsLike(user.getUserId(), postId)) {
            throw new CreateNewResourceException("User already liked the current post");
        }

        Likes likes = new Likes();
        likes.setPost(post);
        likes.setUser(user);

        try {
            likesRepo.save(likes);
        } catch (Exception e) {
            throw new CreateNewResourceException("User already liked this post");
        }
    }

    /**
     * Remove like from given Post for current user
     * @param postId The post I'd from which the current user trying to remove like
     */
    @Transactional
    public void removeLike(
            @NotNull(message = "Post id can't be null") Long postId) {

        User user = userServiceHelper.getCurrentUserDetails();

        Likes like = likesRepo.getLikesByUserPostId(user.getUserId(),postId)
                .orElseThrow(
                        ()-> new ResourceNotAvailable(
                                "Fails to fetch post or user never liked the post")
                );

        likesRepo.delete(like);
    }

    private boolean existsLike(Long userId, Long postId){
        Long rowEffected =  likesRepo.checkLikeAlreadyExist(
                userId,postId
        );

        if (null == rowEffected) return false;

        return rowEffected > 0;
    }
}
