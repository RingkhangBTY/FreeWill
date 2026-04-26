package com.ringkhang.freewill.repo;

import com.ringkhang.freewill.DTO.UserResponseDTO;
import com.ringkhang.freewill.models.User;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDetailsRepo extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query(
            value = """
            SELECT 
                userid AS userId,
                username AS username,
                bio AS bio,
                created_date AS createdDate
            FROM user_details
            WHERE username LIKE '%' || :name || '%'
            """,
            nativeQuery = true
    )
    List<UserResponseDTO> getUsersByUsername(@Param("name") String username);


    // Updates the username & bio
    @Modifying
    @Query(
            value = "UPDATE public.user_details SET username = :username, bio = :bio WHERE userid = :uId",
            nativeQuery = true
    )
    int updateUserNameBio(@Param("username") String username,
                           @Param("bio") String bio,
                           @Param("uId") Long currentUserId);

    // Update username
    @Transactional
    @Modifying
    @Query(
            value = "UPDATE public.user_details SET username = :newUsername WHERE userid = :uId",
            nativeQuery = true
    )
    int updateUsername(
            @Param("newUsername") String newUsername,
            @Param("uId") Long id);

    // Update bio
    @Transactional
    @Modifying
    @Query(
            value = "UPDATE public.user_details SET bio = :newBio WHERE userid = :uId",
            nativeQuery = true
    )
    int updateBio(
            @Param("newBio") String newBio,
            @Param("uId") Long currentUserId
    );

    // delete user fully
    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM user_details WHERE userid = :uId",
            nativeQuery = true)
    int deleteUserFully(@Param("uId") Long uId);

    // disable user
    @Transactional
    @Modifying
    @Query(
            value = "UPDATE public.user_details SET is_active = false WHERE userid = :uId",
            nativeQuery = true
    )
    int deleteUserPartially(@Param("uId") Long uId);
}