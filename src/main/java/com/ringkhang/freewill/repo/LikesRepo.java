package com.ringkhang.freewill.repo;

import com.ringkhang.freewill.models.Likes;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepo extends JpaRepository<Likes,Long> {


    @Query(
            value = "select count(*) from likes where post_id = :id",
            nativeQuery = true
    )
    Long getLikeCountByPost(@Param("id") Long postId);


    /**
     * @param userId Current user ID
     * @param postId Post which user want to give/add like
     * @return Row effected >0 means user already liked the post else no
     */
    @Query(
            value = "select * from likes where user_id = :userId AND post_id = :postId",
            nativeQuery = true
    )
    Long checkLikeAlreadyExist(@Param("userId") Long userId, @Param("postId") Long postId);


    /**
     *
     * @param userId Current longed user's id
     * @param postId Post from where user want to remove like
     * @return One {@code Likes } object if the user liked given post
     */
    @Query(
            value = "select * from likes where user_id = :userId AND post_id = :postId",
            nativeQuery = true
    )
    Optional<Likes> getLikesByUserPostId(
            @Param("userId") Long userId,
            @Param("postId")@NotNull(message = "Post id can't be null") Long postId);
}