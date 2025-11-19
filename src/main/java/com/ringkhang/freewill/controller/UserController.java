package com.ringkhang.freewill.controller;

import com.ringkhang.freewill.DTO.UserResponseDTO;
import com.ringkhang.freewill.models.Posts;
import com.ringkhang.freewill.models.User;
import com.ringkhang.freewill.services.PostService;
import com.ringkhang.freewill.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService , PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("search-user")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByUsername(@RequestParam String username){
        try{
            List<UserResponseDTO> users = userService.getUsersByUsername(username);
            if (!users.isEmpty()){
                return new ResponseEntity<>(users,HttpStatus.OK);
            }
            return new ResponseEntity<>(users,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public User userRegister (@RequestBody User userDetails){
//        System.out.println(userDetails);
        if (userDetails==null) throw new IllegalArgumentException("Getting empty object");
        return userService.registerUser(userDetails);
    }

    @GetMapping("/details")
    public User getUserDetails(){
        return userService.getCurrentUserDetails();
    }

    @GetMapping("/posts")
    public List<Posts> userPosts(){
        return postService.getCurrentUserPosts();
    }

}