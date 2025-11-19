package com.ringkhang.freewill.services;

import com.ringkhang.freewill.DTO.UserResponseDTO;
import com.ringkhang.freewill.models.MyUserPrincipal;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDetailsRepo userDetailsRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public User registerUser(User user) {
        if (userDetailsRepo.existsByUsername(user.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userDetailsRepo.save(user);
    }

    //Search for user in database for matching names
    public List<UserResponseDTO> getUsersByUsername(String username){
        return userDetailsRepo.getUsersByUsername(username);
    }

    //Returns current user id whose log-in
    public Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        return principal.getUser().getUserId();
    }
    // Get full user details including - id , pass and meta date.
    public User getCurrentUserDetails() {
        return userDetailsRepo.findById(getCurrentUserId()).orElse(new User());
    }
    // Get full user details including - id , pass and meta date.
    public User getCurrentUserByUserName(String username) {
        return userDetailsRepo.findByUsername(username).orElse(new User());
    }
}