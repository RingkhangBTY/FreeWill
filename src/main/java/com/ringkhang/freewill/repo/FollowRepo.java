package com.ringkhang.freewill.repo;

import com.ringkhang.freewill.models.Followers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepo extends JpaRepository<Followers,Long> {

    @Query(
            value = "SELECT * FROM followers WHERE follow_user_id = :id AND follows_user_id = :followsId",
            nativeQuery = true
    )
    Followers findByUserId(@Param("id") Long currentUserId, @Param("followsId") Long followUserId);
}
