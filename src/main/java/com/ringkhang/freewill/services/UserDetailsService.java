package com.ringkhang.freewill.services;


import com.ringkhang.freewill.models.MyUserPrincipal;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService  implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userDetails = userDetailsRepo.getUserDetailsByUsername(username);

        if (userDetails == null){
            throw new UsernameNotFoundException("User doesn't exist ");
        }

        return new MyUserPrincipal(userDetails);
    }
}
