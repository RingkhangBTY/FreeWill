package com.ringkhang.freewill.services;

import com.ringkhang.freewill.models.MyUserPrincipal;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.UserDetailsRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceHelper {
    private final UserDetailsRepo userDetailsRepo;
    private final BCryptPasswordEncoder encoder;

    public UserServiceHelper(UserDetailsRepo userDetailsRepo, BCryptPasswordEncoder encoder) {
        this.userDetailsRepo = userDetailsRepo;
        this.encoder = encoder;
    }

    //Returns current user id whose log-in
    public Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        return principal.getUser().getUserId();
    }

    // Get full user details including - id , pass and meta date.
    public User getCurrentUserByUserName(String username) {
        return userDetailsRepo.findByUsername(username).orElse(new User());
    }

    // Get full user details including - id , pass and meta date.
    public User getCurrentUserDetails() {
        return userDetailsRepo.findById(getCurrentUserId()).orElse(new User());
    }

    // validates user details
    User validateDeleteEditRequest(Long uId, String password){

        if (!uId.equals(getCurrentUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can't delete because you're not the actual user"
            );
        }

        User user = userDetailsRepo.findById(uId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No user found with user id"
                ));

        if (!encoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Wrong password!"
            );
        }

        return user;
    }
}