package com.ringkhang.freewill.repo;

import com.ringkhang.freewill.models.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepo extends JpaRepository<Likes,Long> {

    @Query(
            value = "select count(*) from likes where post_id = :id",
            nativeQuery = true
    )
    public Long getLikeCountByPost(@Param("id") Long postId);
}
