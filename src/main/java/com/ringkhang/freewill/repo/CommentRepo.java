package com.ringkhang.freewill.repo;

import com.ringkhang.freewill.models.Comments;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comments,Long> {

    @Query(value = "SELECT * FROM comments WHERE post_id = :id ", nativeQuery = true)
    List<Comments> findCommentsByPostId(@Param("id") Long postId);

    // by default spring data assume that every @Query return data unless we explicitly tell
    // by using @Transactional & @Modifying
    @Transactional
    @Modifying
    @Query( value = """
            INSERT INTO comments (comment_text,user_id,post_id) 
                        values (
                                            :text,
                                            :uId,
                                            :pId
                                    )""", nativeQuery = true)
    void addComment(@Param("text") String commentText, @Param("pId") Long postId, @Param("uId") Long userId);

}