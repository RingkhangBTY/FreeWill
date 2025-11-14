package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.models.UserDetails;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/register")
    public UserDetails userRegister (@RequestBody UserDetails userDetails){
        return userDetails;
    }

}