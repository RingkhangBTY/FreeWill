package com.ringkhang.freewill.repo;

import com.ringkhang.freewill.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepo extends JpaRepository<Posts,Long> {

    @Query(value = "SELECT * FROM posts WHERE user_id = :userId", nativeQuery = true)
    List<Posts> findPostsByUserId(@Param("userId") Long userId);

    // Updates the post text
    @Modifying
    @Query(
            value = "UPDATE posts SET post_text = :newText WHERE post_id = :pId",
            nativeQuery = true
    )
    public void updatePostText(
            @Param("newText") String text,
            @Param("pId") Long id);


    // To get post based on basic algo
    @Query(value = """
        SELECT 
            p.user_id AS userId,
            p.post_id AS postId,
            u.username AS username,
            p.post_text AS postText,
            p.created_date AS postCreateTime,
            p.update_date AS postUpdateTime,
            COALESCE(l.like_count, 0) AS likeCount,
            COALESCE(c.comment_count, 0) AS commentCount,
            COALESCE(f.follower_count, 0) AS followerCount,
            (
                (COALESCE(l.like_count, 0) * 3) +
                (COALESCE(c.comment_count, 0) * 5) +
                (COALESCE(f.follower_count, 0) * 0.5) -
                (EXTRACT(EPOCH FROM (NOW() - p.created_date)) / 3600)
            ) AS rankScore
        FROM posts p
        JOIN user_details u ON u.userid = p.user_id
        LEFT JOIN (
            SELECT post_id, COUNT(*) AS like_count
            FROM likes
            GROUP BY post_id
        ) l ON p.post_id = l.post_id
        LEFT JOIN (
            SELECT post_id, COUNT(*) AS comment_count
            FROM comments
            GROUP BY post_id
        ) c ON p.post_id = c.post_id
        LEFT JOIN (
            SELECT follows_user_id AS author_id, COUNT(*) AS follower_count
            FROM followers
            GROUP BY follows_user_id
        ) f ON p.user_id = f.author_id
        WHERE p.is_deleted = false
        ORDER BY rankScore DESC
        LIMIT 50
        """, nativeQuery = true)
    List<Object[]> getRankedPosts();


}
