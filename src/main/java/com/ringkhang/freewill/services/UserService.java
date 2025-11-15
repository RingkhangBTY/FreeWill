package com.ringkhang.freewill.services;

import com.ringkhang.freewill.models.MyUserPrincipal;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        return principal.getUser().getUserId();
    }

    public User getCurrentUserDetails() {
        return userDetailsRepo.findById(getCurrentUserId()).orElse(new User());
    }
}