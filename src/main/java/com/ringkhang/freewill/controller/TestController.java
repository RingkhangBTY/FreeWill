package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.exception.NoUserFound;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

//    @GetMapping("get-user")
    public ResponseEntity<User> getUser(){
        return testService.getUser();
    }
}
