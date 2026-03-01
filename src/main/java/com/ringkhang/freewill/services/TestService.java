package com.ringkhang.freewill.services;

import com.ringkhang.freewill.exception.NoUserFound;
import com.ringkhang.freewill.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public ResponseEntity<User> getUser(){
        throw new NoUserFound("No user found with this iss");
    }
}
