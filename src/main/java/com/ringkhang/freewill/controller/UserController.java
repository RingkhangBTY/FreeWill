package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.models.Temp;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User userRegister (@RequestBody User userDetails){
        System.out.println(userDetails);

        if (userDetails==null) throw new IllegalArgumentException("Getting empty object");

        return userService.registerUser(userDetails);
    }

    @PostMapping("/test")
    public Temp test(@RequestBody Temp temp){
        System.out.println(temp);
        return new Temp("Default","default@gmail.com");
    }

    @GetMapping("/details")
    public User getUserDetails(){
        return userService.getCurrentUserDetails();
    }

}