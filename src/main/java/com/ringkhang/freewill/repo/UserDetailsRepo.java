package com.ringkhang.freewill.repo;

import com.ringkhang.freewill.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepo extends JpaRepository<User, Long>{

    User getUserDetailsByUsername(String username);

}